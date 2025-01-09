package com.example.springcloud.microservicio.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})//Evitar conexion default
@EnableFeignClients
@SpringBootApplication
public class MicroservicioItemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioItemsApplication.class, args);
	}

}
