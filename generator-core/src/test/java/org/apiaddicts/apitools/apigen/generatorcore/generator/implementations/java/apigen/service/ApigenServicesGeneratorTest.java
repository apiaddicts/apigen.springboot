package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.service;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

class ApigenServicesGeneratorTest {

    @Test
    void givenValidData_whenGenerateService_thenServiceFileIsGenerated(@TempDir Path filesRootPath) throws IOException {
        Entity testEntity = EntityObjectMother.createSimpleEntityWithStringAsPrimaryKey();
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        Mockito.when(entitiesData.getBasicAttributes(anyString())).thenReturn(Collections.emptySet());
        Mockito.when(entitiesData.getIDType(anyString())).thenReturn(TypeName.get(Long.class));
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        Configuration cfg = ConfigurationObjectMother.create(Collections.singletonList(testEntity), null);
        new ApigenServicesGenerator<>(ctx, cfg).generate(filesRootPath);

        File projectFolder = filesRootPath.toFile();

        Path entityPath = Paths.get(
                projectFolder.getPath(),
                "src", "main", "java", "the", "group", "artifact", "simpletestentity", "SimpleTestEntityService.java"
        );
        assertTrue(Files.exists(entityPath), "SimpleTestEntityService.java not generated");
    }

}
