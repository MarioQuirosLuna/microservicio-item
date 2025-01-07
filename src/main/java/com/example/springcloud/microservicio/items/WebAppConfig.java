package com.example.springcloud.microservicio.items;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class WebAppConfig {
    
    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> customizarCircuitBreaker(){

        return (factory) -> factory.configureDefault(id -> {
            //ESTA CONFIGURACION SE PUEDE HACER POR (application.yml)
            //LOS ARCHIVOS YML TIENEN PRIORIDAD
            return new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(
                    CircuitBreakerConfig
                        .custom()
                        .slidingWindowSize(10)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofSeconds(10L))
                        .permittedNumberOfCallsInHalfOpenState(5)
                        .slowCallDurationThreshold(Duration.ofSeconds(2L))//Llamada lenta 2seg
                        .slowCallRateThreshold(50)
                        .build())
                .timeLimiterConfig(
                    TimeLimiterConfig
                        .custom()
                        .timeoutDuration(Duration.ofSeconds(4L))//TimeOut 4Seg
                        .build())
                .build();
        });
    }
}
