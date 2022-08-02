package org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource;

import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.ControllerObjectMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Deprecated
class ResourceGeneratorTest {

    @Test
    void givenValidController_whenGenerateResource_thenResourceFileIsGenerated(@TempDir Path filesRootPath) throws IOException {
        List<Controller> controllerList = Arrays.asList(ControllerObjectMother.createControllerWithStandardEndpoints("EntityName", "/mapping"));

        ResourcesGenerator resourcesGenerator = new ResourcesGenerator(controllerList, "the.base.package");
        resourcesGenerator.generate(filesRootPath);

        File projectFolder = filesRootPath.toFile();

        Path entityPath = Paths.get(
                projectFolder.getPath(),
                "the", "base", "package", "entityname", "web", "EntityNameOutResource.java"
        );
        assertTrue(Files.exists(entityPath), "EntityNameOutResource.java not generated");
    }

}
