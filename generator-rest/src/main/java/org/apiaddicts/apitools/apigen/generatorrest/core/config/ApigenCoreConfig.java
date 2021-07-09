package org.apiaddicts.apitools.apigen.generatorrest.core.config;

import org.apiaddicts.apitools.apigen.generatorcore.generator.ApigenProjectGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApigenCoreConfig {

    @Bean
    public ApigenProjectGenerator apigenProjectGenerator(ApigenProperties properties) {
        return new ApigenProjectGenerator(
                properties.getParent().getGroup(),
                properties.getParent().getArtifact(),
                properties.getParent().getVersion()
        );
    }
}
