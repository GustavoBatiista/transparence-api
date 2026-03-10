package dev.java.transparence.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()

                .tags(List.of(
                        new Tag().name("Auth").description("Authentication endpoints"),
                        new Tag().name("Users").description("API for managing users"),
                        new Tag().name("Dependents").description("API for managing dependents"),
                        new Tag().name("Contracts").description("API for managing contracts"),
                        new Tag().name("Incomes").description("API for managing incomes"),
                        new Tag().name("Expenses").description("API for managing expenses")
                ))

                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
