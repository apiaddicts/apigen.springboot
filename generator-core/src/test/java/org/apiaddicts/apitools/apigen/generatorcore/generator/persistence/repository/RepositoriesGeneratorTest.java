package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.repository;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData;
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

@Deprecated
class RepositoriesGeneratorTest {

    @Test
    void givenValidData_whenGenerateRepository_thenRepositoryFileIsGenerated(@TempDir Path filesRootPath) throws IOException {

        Entity testEntity = EntityObjectMother.createSimpleEntityWithStringAsPrimaryKey();
        EntitiesData entitiesData = Mockito.mock(EntitiesData.class);
        Mockito.when(entitiesData.getIDType(anyString())).thenReturn(TypeName.get(Long.class));
        new RepositoriesGenerator(Collections.singletonList(testEntity), entitiesData, "the.base.package").generate(filesRootPath);

        File projectFolder = filesRootPath.toFile();

        Path entityPath = Paths.get(
                projectFolder.getPath(),
                "the", "base", "package", "simpletestentity", "SimpleTestEntityRepository.java"
        );
        assertTrue(Files.exists(entityPath), "SimpleTestEntityRepository.java not generated");
    }

}
