package org.apiaddicts.apitools.apigen.archetypecore.core;

import com.fasterxml.jackson.databind.Module;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }
}