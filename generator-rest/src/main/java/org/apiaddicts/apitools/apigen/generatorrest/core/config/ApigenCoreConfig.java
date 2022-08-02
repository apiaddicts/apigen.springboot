package org.apiaddicts.apitools.apigen.generatorrest.core.config;

import org.apiaddicts.apitools.apigen.generatorcore.generator.ProjectGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenGenerationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApigenCoreConfig {

    @Bean
    public ProjectGenerator<ApigenContext> apigenProjectGenerator(ApigenProperties properties) {
        Map<String, Object> globalConfig = new HashMap<>();
        globalConfig.put("parentGroup", properties.getParent().getGroup());
        globalConfig.put("parentArtifact", properties.getParent().getArtifact());
        globalConfig.put("parentVersion", properties.getParent().getVersion());
        return new ProjectGenerator<>(new ApigenGenerationStrategy(), globalConfig);
    }
}
