package com.odanylchuk.cosmocatsmarketplace.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cosmoCatsMarketplaceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cosmo Cats Intergalactic Marketplace API")
                        .description("API for managing products in an intergalactic marketplace for cosmic cats")
                        .version("1.2.0")
                        .contact(new Contact()
                                .name("Cosmo Cats Team")
                                .email("contact@cosmocats.galaxy")));
    }
}
