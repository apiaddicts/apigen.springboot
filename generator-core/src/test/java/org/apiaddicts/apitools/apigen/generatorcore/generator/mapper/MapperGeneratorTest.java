package org.apiaddicts.apitools.apigen.generatorcore.generator.mapper;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.ResourcesData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapperGeneratorTest {

    @Test
    void givenValidEntitiesData_whenGenerateMapper_thenMapperFileIsGenerated(@TempDir Path filesRootPath) throws IOException {

        Entity testEntity = EntityObjectMother.createEntityWithSimpleAttributes();

        EntitiesData entitiesData = createMockedEntitiesData(testEntity.getName());
        ResourcesData resourcesData = new ResourcesData(Collections.emptyList(), Collections.emptyList());

        boolean success = new MappersGenerator(Arrays.asList(testEntity), entitiesData, resourcesData, "the.base.package").generate(filesRootPath);

        assertTrue(success, "Mapper generation failed");

        File projectFolder = filesRootPath.toFile();

        Path entityPath = Paths.get(
                projectFolder.getPath(),
                "the", "base", "package", "testentitywithsimpleattributes", "TestEntityWithSimpleAttributesMapper.java"
        );
        assertTrue(Files.exists(entityPath), "TestEntityWithSimpleAttributesMapper.java not generated");
    }

    private EntitiesData createMockedEntitiesData(String entityName) {
        EntitiesData mockedEntitiesData = mock(EntitiesData.class);
        when(mockedEntitiesData.getRelatedEntities(entityName)).thenReturn(Collections.emptySet());
        when(mockedEntitiesData.getIDType(entityName)).thenReturn(TypeName.get(String.class));
        return mockedEntitiesData;
    }

}
