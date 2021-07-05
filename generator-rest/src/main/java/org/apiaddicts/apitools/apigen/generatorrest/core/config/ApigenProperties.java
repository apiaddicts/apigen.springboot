package org.apiaddicts.apitools.apigen.generatorrest.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "apigen")
public class ApigenProperties {

    private Parent parent;

    @Data
    public static class Parent {
        private String group;
        private String artifact;
        private String version;
    }
}
