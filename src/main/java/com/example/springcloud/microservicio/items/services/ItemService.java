package com.example.springcloud.microservicio.items.services;

import java.util.List;
import java.util.Optional;

import com.example.libs.microservicio.commons.entities.Product;
import com.example.springcloud.microservicio.items.models.Item;

public interface ItemService {
    List<Item> findAll();
    Optional<Item> findById(Long id);
    Product save(Product productDto);
    Product update(Product productDto, Long id);
    void delete(Long id);
}
