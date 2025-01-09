package com.example.springcloud.microservicio.items.controllers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcloud.microservicio.items.models.Item;
import com.example.springcloud.microservicio.items.models.ProductDto;
import com.example.springcloud.microservicio.items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RefreshScope
@RestController
public class ItemController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    @Autowired
    private CircuitBreakerFactory cBreakerFactory;

    @Value("${configuration.text}")
    private String text;

    @Autowired
    private Environment env;

    @Autowired
    private @Qualifier("itemServiceWebClient") ItemService service;//Qualifier define cual implemetacion se usa (feign y WebClient)

    //@Autowired
    //private @Qualifier("itemServiceFeign") ItemService service; //Qualifier define cual implemetacion se usa (feign y WebClient)

    @GetMapping("")
    public List<Item> list(@RequestParam(name="name", required=false) String name, @RequestHeader(name="token-request", required = false) String tokenRequest) {
        System.out.println("RequestParam name: " + name);
        System.out.println("RequestHeader token-request: " + tokenRequest);
        return service.findAll();
    }

    @GetMapping("/fetch-configs")
    public ResponseEntity<?> fetchConfig(@Value("${server.port}") String port) {
        Map<String, String> json = new HashMap<>();
        json.put("text", text);
        json.put("port", port);
        logger.info(port+" - "+text);

        if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")){
            json.put("autor.name", env.getProperty("configuration.autor.name"));
            json.put("autor.email", env.getProperty("configuration.autor.email"));
        }

        return ResponseEntity.ok(json);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {
        Optional<Item> itemOptional = cBreakerFactory.create("items").run(() -> service.findById(id));

        //AQUI SE PUEDE REDIRECCIONAR A OTRA APP CUANDO FALLE EL CIRCUIT BREAKER
        // Optional<Item> itemOptional = cBreakerFactory.create("items").run(() -> service.findById(id), e -> {
        //     System.out.println(e.getMessage());
        //     logger.error(e.getMessage());

        //     //PARA PRUEBAS SE HACE UN PRODUCTO FICTICIO
        //     ProductDto productDto = new ProductDto();
        //     productDto.setId(1L);
        //     productDto.setName("Camara Sony");
        //     productDto.setPrice(500.00);
        //     productDto.setCreateAt(LocalDate.now());
        //     return Optional.of(new Item(productDto, 5));
        // });
        if(itemOptional.isPresent()){
            return ResponseEntity.ok(itemOptional.get());
        }
        return ResponseEntity.status(404)
            .body(Collections.singletonMap(
                "message", 
                "No existe el producto en el microservicio microservicio-product"));
    }

    //SOLO CIRCUIT BREAKER
    @CircuitBreaker(name = "items", fallbackMethod = "getFallBackMethodProduct") //Solo con application.yml/application.properties
    @GetMapping("/details/{id}")
    public ResponseEntity<?> details2(@PathVariable Long id) {
        Optional<Item> itemOptional = service.findById(id);
        if(itemOptional.isPresent()){
            return ResponseEntity.ok(itemOptional.get());
        }
        return ResponseEntity.status(404)
            .body(Collections.singletonMap(
                "message", 
                "No existe el producto en el microservicio microservicio-product"));
    }

    //COMBINADO TIME OUT Y CIRCUIT BREAKER
    @CircuitBreaker(name="items", fallbackMethod = "getFallBackMethodProduct2")
    @TimeLimiter(name = "items")
    @GetMapping("/details3/{id}")
    public CompletableFuture<?> details3(@PathVariable Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Item> itemOptional = service.findById(id);
            
            if(itemOptional.isPresent()){
                return ResponseEntity.ok(itemOptional.get());
            }
            
            return ResponseEntity.status(404)
                .body(Collections.singletonMap(
                    "message", 
                    "No existe el producto en el microservicio microservicio-product"));
        }); 
    }

    public ResponseEntity<?> getFallBackMethodProduct(Throwable e) {
        System.out.println(e.getMessage());
        logger.error(e.getMessage());

        // PARA PRUEBAS SE HACE UN PRODUCTO FICTICIO
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Camara Sony");
        productDto.setPrice(500.00);
        productDto.setCreateAt(LocalDate.now());
        return ResponseEntity.ok(new Item(productDto, 5));
    }

    public CompletableFuture<?> getFallBackMethodProduct2(Throwable e) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());

            // PARA PRUEBAS SE HACE UN PRODUCTO FICTICIO
            ProductDto productDto = new ProductDto();
            productDto.setId(1L);
            productDto.setName("Camara Sony");
            productDto.setPrice(500.00);
            productDto.setCreateAt(LocalDate.now());
            return ResponseEntity.ok(new Item(productDto, 5));
        });
    }
}
