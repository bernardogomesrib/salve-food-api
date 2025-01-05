package com.pp1.salve.configs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Salve", version = "1.0", description = "API do salve food, ta com fome manda um salve", contact = @Contact(name = "Bernardo", email = "bernardogomesrib@gmail.com", url = "https://naotenhosite.com/"), license = @License(name = "Licence name", url = "https://some-url.com"), termsOfService = "Terms of service"), servers = {
            @Server(url = "http://localhost:8080", description = "Local server"),
            @Server(url = "${public.server.url}:8080", description = "Remote server"),

}, security = {
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "bearerAuth2")
})

@SecurityScheme(name = "bearerAuth", description = "JWT auth description", scheme = "bearer", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(implicit = @OAuthFlow(authorizationUrl = "http://localhost:9080/realms/salve/protocol/openid-connect/auth", tokenUrl = "http://localhost:9080/realms/salve/protocol/openid-connect/token")

), bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
@SecurityScheme(name = "bearerAuth2", description = "JWT auth description", scheme = "bearer", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(implicit = @OAuthFlow(authorizationUrl = "${public.server.url}:9080/realms/salve/protocol/openid-connect/auth", tokenUrl = "${public.server.url}:9080/realms/salve/protocol/openid-connect/token")), bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
@Configuration
public class SwaggerConfig {
      @Bean
      public GroupedOpenApi customApi() {
            return GroupedOpenApi.builder()
                        .group("api")
                        .pathsToMatch("/api/**")
                        .pathsToExclude("/error", "/actuator/**")
                        .build();
      }/*  */
}