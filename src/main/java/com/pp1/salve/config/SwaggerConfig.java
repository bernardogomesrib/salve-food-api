package com.pp1.salve.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(
    contact = @io.swagger.v3.oas.annotations.info.Contact(
        name = "API do salve FOOD",
        email = "bjgr@discente.ifpe.edu.br"),
    title = "Salve Food API", version = "1.0", description = "API do App Salve Food"),
    servers = {
        @Server(
        url = "http://localhost:8080",
        description = "Local server"
    ),
})
public class SwaggerConfig {


    @Bean
    public GroupedOpenApi customApi() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/**")
                .pathsToExclude("/error", "/actuator/**")
                .build();
    }
}

