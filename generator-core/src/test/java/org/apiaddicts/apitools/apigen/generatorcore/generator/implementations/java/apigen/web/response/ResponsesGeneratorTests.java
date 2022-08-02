package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response;

import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.ControllerObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponsesGeneratorTests {

    @Test
    void givingResponseGenerator_whenSimpleController_thenCreateSimpleFile(@TempDir Path filesRootPath) throws IOException {

        Controller controllerSimple = ControllerObjectMother.createControllerWithSimpleResponse("EntityName");

        List<Controller> listController = new ArrayList<>();
        listController.add(controllerSimple);

        new ApigenResponsesGenerator<>(
                ApigenContextObjectMother.create(), ConfigurationObjectMother.create(null, listController)).generate(filesRootPath);

        File projectFolder = filesRootPath.toFile();

        Path entityPath = Paths.get(
                projectFolder.getPath(),
                "src", "main", "java", "the", "group", "artifact", "entityname", "web", "EntityNameResponse.java"
        );
        assertTrue(Files.exists(entityPath), "EntityNameResponse.java not generated");
    }

    @Test
    void givingResponseGenerator_whenListController_thenCreateListFile(@TempDir Path filesRootPath) throws IOException {

        Controller controllerList = ControllerObjectMother.createControllerWithListResponse("EntityName");

        List<Controller> listController = new ArrayList<>();
        listController.add(controllerList);

        new ApigenResponsesGenerator<>(ApigenContextObjectMother.create(), ConfigurationObjectMother.create(null, listController)).generate(filesRootPath);

        File projectFolder = filesRootPath.toFile();

        Path entityPath = Paths.get(
                projectFolder.getPath(),
                "src", "main", "java", "the", "group", "artifact", "entityname", "web", "EntityNameListResponse.java"
        );
        assertTrue(Files.exists(entityPath), "EntityNameListResponse.java not generated");
    }

    @Test
    void givingResponseGenerator_whenSimpleAndListController_thenCreateSimpleAndListFile(@TempDir Path filesRootPath) throws IOException {

        Controller controllerSimpleAndList = ControllerObjectMother.createControllerWithSimpleAndListResponse("EntityName");

        List<Controller> listController = new ArrayList<>();
        listController.add(controllerSimpleAndList);

        new ApigenResponsesGenerator<>(ApigenContextObjectMother.create(), ConfigurationObjectMother.create(null, listController)).generate(filesRootPath);

        File projectFolder = filesRootPath.toFile();

        Path entitySimplePath = Paths.get(
                projectFolder.getPath(),
                "src", "main", "java", "the", "group", "artifact", "entityname", "web", "EntityNameResponse.java"
        );
        Path entityListPath = Paths.get(
                projectFolder.getPath(),
                "src", "main", "java", "the", "group", "artifact", "entityname", "web", "EntityNameListResponse.java"
        );
        assertTrue(Files.exists(entitySimplePath), "EntityNameResponse.java not generated");
        assertTrue(Files.exists(entityListPath), "EntityNameListResponse.java not generated");
    }
}
