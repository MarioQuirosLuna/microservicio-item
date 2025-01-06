package com.example.springcloud.microservicio.items.services;

import java.util.List;
import java.util.Optional;

import com.example.springcloud.microservicio.items.models.Item;

public interface ItemService {
    List<Item> findAll();
    Optional<Item> findById(Long id);
}
