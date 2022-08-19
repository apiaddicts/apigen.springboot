package org.apiaddicts.apitools.apigen.generatorcore.spec.components;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApigenProject {

    private String name;
    private String description;
    private String version;
    private Boolean partial = false;

    private JavaProperties javaProperties = new JavaProperties();
    private List<StandardResponseOperation> standardResponseOperation = new ArrayList<StandardResponseOperation>();

    @Data
    public static class JavaProperties {
        private String groupId;
        private String artifactId;
    }

    @Data
    public static class StandardResponseOperation {

        public StandardResponseOperation(String op, String path, String value){
            this.op = op;
            this.path = path;
            this.value = value;
        }
        private String op;
        private String path;
        private String value;
    }

}
