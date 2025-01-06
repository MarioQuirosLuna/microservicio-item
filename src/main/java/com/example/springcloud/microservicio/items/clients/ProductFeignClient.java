package com.example.springcloud.microservicio.items.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.springcloud.microservicio.items.models.ProductDto;

@FeignClient(name = "microservicio-product")
public interface ProductFeignClient {
    
    @GetMapping("/api/products")
    List<ProductDto> findAll();

    @GetMapping("/api/products/{id}")
    ProductDto details(@PathVariable Long id);
}
