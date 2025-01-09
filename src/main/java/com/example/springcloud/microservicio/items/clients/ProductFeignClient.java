package com.example.springcloud.microservicio.items.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.springcloud.microservicio.items.models.ProductDto;

@FeignClient(name = "microservicio-product")
public interface ProductFeignClient {
    
    @GetMapping("")
    List<ProductDto> findAll();

    @GetMapping("/{id}")
    ProductDto details(@PathVariable Long id);

    @PostMapping
    public ProductDto create(@RequestBody ProductDto productDto);

    @PutMapping
    public ProductDto update(@RequestBody ProductDto productDto, @PathVariable Long id);

    @DeleteMapping
    public void delete(@PathVariable Long id);
}
