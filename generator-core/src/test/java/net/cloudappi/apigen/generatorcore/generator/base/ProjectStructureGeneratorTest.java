package net.cloudappi.apigen.generatorcore.generator.base;

import net.cloudappi.apigen.generatorcore.config.Configuration;
import net.cloudappi.apigen.generatorcore.config.ConfigurationObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectStructureGeneratorTest {


    Configuration configuration;
    File projectDirectory;

    @BeforeEach
    void prepareTest() {
        configuration = ConfigurationObjectMother.createCompleteConfigurationWithoutEntitiesAndControllers();
    }

    @Test
    void thatGenerates() throws IOException {
        projectDirectory = ProjectStructureGenerator.generate(configuration);
        assertBaseFoldersExists();
    }

    private void assertBaseFoldersExists() {
        Path resourcesPath = Paths.get(projectDirectory.getPath(), "/src/main/resources");
        assertTrue(Files.exists(resourcesPath), "Resources folder not generated");
        Path javaPath = Paths.get(projectDirectory.getPath(), "/src/main/java/the/group/artifact");
        assertTrue(Files.exists(javaPath), "Java folder not generated");
        Path testPath = Paths.get(projectDirectory.getPath(), "/src/test/java/the/group/artifact");
        assertTrue(Files.exists(testPath), "Test folder not generated");
    }
}