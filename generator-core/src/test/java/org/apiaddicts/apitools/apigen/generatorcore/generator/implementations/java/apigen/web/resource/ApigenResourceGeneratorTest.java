package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource;

import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.ControllerObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApigenResourceGeneratorTest {

    @Test
    void givenValidController_whenGenerateResource_thenResourceFileIsGenerated(@TempDir Path filesRootPath) throws IOException {
        List<Controller> controllers = Collections.singletonList(
                ControllerObjectMother.createControllerWithStandardEndpoints("EntityName", "/mapping"));

        ApigenResourcesGenerator<ApigenContext> resourcesGenerator = new ApigenResourcesGenerator<>(ApigenContextObjectMother.create(),
                ConfigurationObjectMother.create(null, controllers));
        resourcesGenerator.generate(filesRootPath);

        File projectFolder = filesRootPath.toFile();

        Path entityPath = Paths.get(
                projectFolder.getPath(),
                "src", "main", "java", "the", "group", "artifact", "entityname", "web", "EntityNameOutResource.java"
        );
        assertTrue(Files.exists(entityPath), "EntityNameOutResource.java not generated");
    }

}
