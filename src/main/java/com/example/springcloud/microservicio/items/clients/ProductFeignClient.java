package com.example.springcloud.microservicio.items.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.springcloud.microservicio.items.models.ProductDto;

@FeignClient(url = "localhost:8001")
public interface ProductFeignClient {
    
    @GetMapping
    List<ProductDto> findAll();

    @GetMapping("/{id}")
    ProductDto details(@PathVariable Long id);
}
