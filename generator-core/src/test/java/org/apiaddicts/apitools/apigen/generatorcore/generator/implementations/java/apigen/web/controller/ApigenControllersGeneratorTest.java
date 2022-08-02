package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.ControllerObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
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

class ApigenControllersGeneratorTest {

    @Test
    void givenValidControllers_whenGenerateController_thenControllerFileIsGenerated(@TempDir Path filesRootPath) throws IOException {

        List<Controller> controllerList = Collections.singletonList(ControllerObjectMother.createControllerWithStandardEndpoints("EntityName", "/mapping"));
        ApigenContext ctx = ApigenContextObjectMother.create();
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        ctx.setEntitiesData(entitiesData);
        Configuration cfg = ConfigurationObjectMother.create(null, controllerList);
        new ApigenControllersGenerator<>(ctx, cfg).generate(filesRootPath);

        File projectFolder = filesRootPath.toFile();

        Path entityPath = Paths.get(
                projectFolder.getPath(),
                "src", "main", "java", "the", "group", "artifact", "entityname", "web", "EntityNameController.java"
        );
        Assertions.assertTrue(Files.exists(entityPath), "EntityNameController.java not generated");
    }

}
