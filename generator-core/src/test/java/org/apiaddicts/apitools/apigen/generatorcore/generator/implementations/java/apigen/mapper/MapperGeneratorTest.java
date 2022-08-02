package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.mapper;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.JavaResourcesData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapperGeneratorTest {

    @Test
    void givenValidEntitiesData_whenGenerateMapper_thenMapperFileIsGenerated(@TempDir Path filesRootPath) throws IOException {

        Entity testEntity = EntityObjectMother.createEntityWithSimpleAttributes();

        Configuration cfg = ConfigurationObjectMother.create(Collections.singletonList(testEntity), Collections.emptyList());

        JavaEntitiesData entitiesData = createMockedEntitiesData(testEntity.getName());
        JavaResourcesData resourcesData = new JavaResourcesData(Collections.emptyList(), Collections.emptyList());

        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        ctx.setResourcesData(resourcesData);

        new ApigenMappersGenerator<>(ctx, cfg).generate(filesRootPath);

        File projectFolder = filesRootPath.toFile();

        Path entityPath = Paths.get(
                projectFolder.getPath(),
                "src", "main", "java", "the", "group", "artifact", "testentitywithsimpleattributes", "TestEntityWithSimpleAttributesMapper.java"
        );
        assertTrue(Files.exists(entityPath), "TestEntityWithSimpleAttributesMapper.java not generated");
    }

    private JavaEntitiesData createMockedEntitiesData(String entityName) {
        JavaEntitiesData mockedEntitiesData = mock(JavaEntitiesData.class);
        when(mockedEntitiesData.getRelatedEntities(entityName)).thenReturn(Collections.emptySet());
        when(mockedEntitiesData.getIDType(entityName)).thenReturn(TypeName.get(String.class));
        return mockedEntitiesData;
    }

}
