package org.apiaddicts.apitools.apigen.generatorcore.spec.components;

import lombok.Data;

@Data
public class ApigenProject {

    private String name;
    private String description;
    private String version;
    private Boolean partial = false;

    private JavaProperties javaProperties = new JavaProperties();

    @Data
    public static class JavaProperties {
        private String groupId;
        private String artifactId;
    }

}
