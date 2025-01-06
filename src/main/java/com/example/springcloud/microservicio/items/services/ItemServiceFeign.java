package com.example.springcloud.microservicio.items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.springcloud.microservicio.items.clients.ProductFeignClient;
import com.example.springcloud.microservicio.items.models.Item;
import com.example.springcloud.microservicio.items.models.ProductDto;

import feign.FeignException;

//@Primary //Funciona para identificar cual implementacion es por defecto cual se tienen 2 beans (feign y WebClient)
@Service
public class ItemServiceFeign implements ItemService{

    @Autowired
    private ProductFeignClient client;

    @Override
    public List<Item> findAll() {
        return client.findAll()
            .stream()
            .map(p -> new Item(p, new Random().nextInt(10)+1))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> findById(Long id) {
        try {
            ProductDto product = client.details(id);
            return Optional.of(new Item(product, new Random().nextInt(10)+1));
        } catch (FeignException e) {
            return Optional.empty();

        }
    }
}
