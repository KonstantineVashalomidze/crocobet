package com.crocobet.konstantinevashalomidze.configuration;

import com.crocobet.konstantinevashalomidze.repository.EndpointRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class Config {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(15))
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Dynamic Monitoring API")
                        .description("REST API for managing endpoint monitoring and health checks")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Konstantine Vashalomidze")
                                .email("vashalomidzekonstantine@gmail.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://github.com/KonstantineVashalomidze/crocobet/blob/main/LICENSE.md")));
    }

    @Bean
    public CommandLineRunner loadDefaultEndpoints(EndpointRepository endpointRepository) {
        return args -> {
            if (endpointRepository.count() == 0) {
                loadDefaultEndpoints(endpointRepository);
            }
        };
    }


}
