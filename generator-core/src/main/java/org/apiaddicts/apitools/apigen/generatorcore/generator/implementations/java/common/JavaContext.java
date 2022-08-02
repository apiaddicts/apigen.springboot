package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common;

import lombok.Data;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Context;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntityRelationManager;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.JavaResourcesData;

@Data
public class JavaContext implements Context {
    
    private String parentGroup;
    private String parentArtifact;
    private String parentVersion;
    private JavaEntityRelationManager entityRelationManager;
    private JavaEntitiesData entitiesData;
    private JavaResourcesData resourcesData;

    public JavaContext(String parentGroup, String parentArtifact, String parentVersion){
        this.parentGroup = parentGroup;
        this.parentArtifact = parentArtifact;
        this.parentVersion = parentVersion;
    }
}
