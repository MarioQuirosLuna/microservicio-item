package com.example.springcloud.microservicio.items.services;

import java.util.List;
import java.util.Optional;

import com.example.springcloud.microservicio.items.models.Item;
import com.example.springcloud.microservicio.items.models.ProductDto;

public interface ItemService {
    List<Item> findAll();
    Optional<Item> findById(Long id);
    ProductDto save(ProductDto productDto);
    ProductDto update(ProductDto productDto, Long id);
    void delete(Long id);
}
