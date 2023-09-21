package com.bdmtr.slotman.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig  {

    @Value("${openapi.dev-url}")
    private String devUrl;

    @Value("${openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("@gmail.com");
        contact.setName("bdmtr");
        contact.setUrl("https://github.com/bdmtr");

        Info info = new Info()
                .title("API for Slot management")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage slots budget").termsOfService("https://github.com/bdmtr");

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}