package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.JavaConstants;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContextObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaProjectGeneratorTest {

    Configuration cfg;
    JavaContext context = JavaContextObjectMother.create();
    Path basePath;

    @BeforeEach
    void prepareTest() {
        cfg = ConfigurationObjectMother.create();
    }

    @Test
    void thatGenerates() throws IOException {
        basePath = Files.createTempDirectory("apigen-project-folder-" + System.nanoTime());
        new JavaProjectGenerator<>(context, cfg).generate(basePath);
        assertBaseFoldersExists();
    }

    private void assertBaseFoldersExists() {
        Path rootPath = Paths.get(basePath.toString(), cfg.getName());
        Path resourcesPath = Paths.get(rootPath.toString(), JavaConstants.RESOURCES_PATH);
        assertTrue(Files.exists(resourcesPath), "Resources folder not generated");
        Path javaPath = Paths.get(rootPath.toString(), JavaConstants.CLASSES_PATH, "/the/group/artifact");
        assertTrue(Files.exists(javaPath), "Java folder not generated");
        Path testPath = Paths.get(rootPath.toString(), JavaConstants.TEST_CLASSES_PATH, "/the/group/artifact");
        assertTrue(Files.exists(testPath), "Test folder not generated");
    }
}
