package com.smartchat.users.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonNullableCustomizer() {
        return builder -> builder.modules(new org.openapitools.jackson.nullable.JsonNullableModule());
    }
    @Bean
    public JsonNullableModule jsonNullableModule() {
        return new JsonNullableModule(); // teaches Jackson how to serialize JsonNullable<T>
    }

    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule(); // teaches Jackson how to serialize JsonNullable<T>
    }

}

