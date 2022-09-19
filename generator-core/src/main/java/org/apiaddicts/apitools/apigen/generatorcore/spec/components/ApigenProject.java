package org.apiaddicts.apitools.apigen.generatorcore.spec.components;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;

@Data
public class ApigenProject {

    private String name;
    private String description;
    private String version;
    private Boolean partial = false;

    private JavaProperties javaProperties = new JavaProperties();
    private ArrayNode standardResponseOperations;

    @Data
    public static class JavaProperties {
        private String groupId;
        private String artifactId;
        private String basePackage;
    }
}
