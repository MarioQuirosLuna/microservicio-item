package com.example.springcloud.microservicio.items.controllers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcloud.microservicio.items.models.Item;
import com.example.springcloud.microservicio.items.models.ProductDto;
import com.example.springcloud.microservicio.items.services.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ItemController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CircuitBreakerFactory cBreakerFactory;

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
}
