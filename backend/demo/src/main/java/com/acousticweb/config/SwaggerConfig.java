package com.acousticweb.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI acousticWebOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Servidor local de desarrollo");

        Contact contact = new Contact();
        contact.setName("AcousticWeb");
        contact.setEmail("soporte@acousticweb.local");

        Info info = new Info()
                .title("AcousticWeb API")
                .version("1.0.0")
                .description("API REST para medición, análisis, perfiles de ecualización y optimización acústica.")
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}