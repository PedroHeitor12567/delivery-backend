package com.pedroferreira.deliveryapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.pedroferreira.deliveryapplication.infrastructure.persistence.entity")
@EnableJpaRepositories("com.pedroferreira.deliveryapplication.infrastructure.repository")
public class DeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }
}
