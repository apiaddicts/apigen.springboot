package net.cloudappi.apigen.generatorcore.generator.web.controller;

import net.cloudappi.apigen.generatorcore.config.controller.Controller;
import net.cloudappi.apigen.generatorcore.config.controller.ControllerObjectMother;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntitiesData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

class ControllersGeneratorTest {

    @Test
    void givenValidControllers_whenGenerateController_thenControllerFileIsGenerated(@TempDir Path filesRootPath) throws IOException {

        List<Controller> controllerList = Collections.singletonList(ControllerObjectMother.createControllerWithStandardEndpoints("EntityName", "/mapping"));
        EntitiesData entitiesData = Mockito.mock(EntitiesData.class);
        new ControllersGenerator(controllerList, entitiesData, "the.base.package").generate(filesRootPath);

        File projectFolder = filesRootPath.toFile();

        Path entityPath = Paths.get(
                projectFolder.getPath(),
                "the", "base", "package", "entityname", "web", "EntityNameController.java"
        );
        Assertions.assertTrue(Files.exists(entityPath), "EntityNameController.java not generated");
    }

}
