package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;


class SpringBootBaseGeneratorTest {

    Configuration configuration;

    @BeforeEach
    void prepareTest() {
        configuration = ConfigurationObjectMother.createCompleteConfigurationWithoutEntitiesAndControllers();
    }

    @Test
    void thatGenerates(@TempDir File projectFolder) throws IOException {
        SpringBootBaseGenerator.generate(configuration, projectFolder);
        assertPropertiesFilesExists(projectFolder);
        assertApplicationClassExists(projectFolder);
    }

    private void assertPropertiesFilesExists(File projectFolder) {
        Path resourcesPath = Paths.get(projectFolder.getPath(), "/src/main/resources");
        Path prop = Paths.get(resourcesPath.toString(), "application.properties");
        assertTrue(Files.exists(prop), "Properties file not generated");
        String[] profiles = {"dev", "pre", "pro", "test"};
        for (String profile : profiles) {
            Path profProp = Paths.get(resourcesPath.toString(), "application-" + profile + ".properties");
            assertTrue(Files.exists(profProp), "Profile " + profile + " config file not generated");
        }
    }

    private void assertApplicationClassExists(File projectFolder) {
        Path applicationClassPath = Paths.get(
                projectFolder.getPath(),
                "/src/main/java/the/group/artifact/Application.java"
        );
        assertTrue(Files.exists(applicationClassPath), "Application.java not generated");
    }
}
