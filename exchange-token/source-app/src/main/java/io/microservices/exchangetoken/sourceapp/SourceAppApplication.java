package io.microservices.exchangetoken.sourceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SourceAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SourceAppApplication.class, args);
    }

}
