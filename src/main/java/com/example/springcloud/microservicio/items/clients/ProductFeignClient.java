package com.example.springcloud.microservicio.items.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.libs.microservicio.commons.entities.Product;

@FeignClient(name = "microservicio-product")
public interface ProductFeignClient {
    
    @GetMapping("")
    List<Product> findAll();

    @GetMapping("/{id}")
    Product details(@PathVariable Long id);

    @PostMapping
    public Product create(@RequestBody Product productDto);

    @PutMapping
    public Product update(@RequestBody Product productDto, @PathVariable Long id);

    @DeleteMapping
    public void delete(@PathVariable Long id);
}
