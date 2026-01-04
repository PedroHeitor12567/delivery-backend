package com.pedroferreira.deliveryapplication.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI deliveryOpenAPI() {
        try {
            Server localServer = new Server();
            localServer.setUrl("http://localhost:8080");
            localServer.setDescription("Servidor Local");

            Contact contact = new Contact();
            contact.setName("Pedro Ferreira");
            contact.setEmail("pedro@deliveryapp.com");

            License license = new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT");

            Info info = new Info()
                    .title("Delivery Application API")
                    .version("1.0.0")
                    .contact(contact)
                    .description("API REST para sistema de delivery seguindo Clean Architecture")
                    .license(license);

            return new OpenAPI()
                    .info(info)
                    .servers(List.of(localServer));
        } catch (Exception e) {
            // Fallback em caso de erro
            return new OpenAPI()
                    .info(new Info()
                            .title("Delivery Application API")
                            .version("1.0.0")
                            .description("API REST para sistema de delivery"));
        }
    }
}