package com.example.springcloud.microservicio.items.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcloud.microservicio.items.models.Item;
import com.example.springcloud.microservicio.items.services.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ItemController {

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
        Optional<Item> itemOptional = service.findById(id);
        if(itemOptional.isPresent()){
            return ResponseEntity.ok(itemOptional.get());
        }
        return ResponseEntity.status(404)
            .body(Collections.singletonMap(
                "message", 
                "No existe el producto en el microservicio microservicio-product"));
    }
}
