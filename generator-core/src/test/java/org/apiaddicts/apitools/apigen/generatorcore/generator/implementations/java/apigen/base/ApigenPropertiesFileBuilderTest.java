package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.spec.components.ApigenProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApigenPropertiesFileBuilderTest {

    private Map<String, Object> extensions;

    @InjectMocks
    private ApigenPropertiesFileBuilder apigenPropertiesFileBuilder;

    @BeforeEach
    void prepareTest() {
        this.extensions = new HashMap<>();
        Map<String, Object> apigenProject = new HashMap<>();
        List<ApigenProject.StandardResponseOperation> standardResponseOperations = new ArrayList<>();
        ApigenProject.StandardResponseOperation standardResponseOperation =
                new ApigenProject.StandardResponseOperation("move", "/the", "");
        standardResponseOperations.add(standardResponseOperation);
        apigenProject.put("standard-response-operations", standardResponseOperations);
        this.extensions.put("x-apigen-project", apigenProject);
    }

    @Test()
    void givenValidExtensions_init_ExecutionSucessfull(){
        Entity testEntity = EntityObjectMother.createSimpleEntityWithStringAsPrimaryKey();
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        Configuration cfg = ConfigurationObjectMother.create(Collections.singletonList(testEntity), null);
        apigenPropertiesFileBuilder = new ApigenPropertiesFileBuilder<>(ctx, cfg, extensions);
        assertTrue(true);
    }

}
