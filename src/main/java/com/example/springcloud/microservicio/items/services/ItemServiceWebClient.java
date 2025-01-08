package com.example.springcloud.microservicio.items.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.springcloud.microservicio.items.models.Item;
import com.example.springcloud.microservicio.items.models.ProductDto;

//@Primary //Funciona para identificar cual implementacion es por defecto cual se tienen 2 beans (feign y WebClient)
@Service
public class ItemServiceWebClient implements ItemService{

    @Autowired
    private WebClient.Builder client;

    @Override
    public List<Item> findAll() {
        return this.client
            .build()
            .get()
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
                .bodyToFlux(ProductDto.class)
            .map(p -> new Item(p, new Random().nextInt(10)+1))
            .collectList()
                .block();
    }

    @Override
    public Optional<Item> findById(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        //try { //Como se maneja camino alternativo con Resilience4J se quita el manejo de exceptiones
            return Optional.of(this.client
                .build()
                .get()
                .uri("/{id}", params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                    .bodyToMono(ProductDto.class)
                .map(p -> new Item(p, new Random().nextInt(10)+1))
                    .block());
        // catch (WebClientResponseException e) {
        //    return Optional.empty();
        //}

    }
    
}
