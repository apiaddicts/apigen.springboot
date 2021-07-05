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

class SpringBootContextTestGeneratorTest {

    Configuration configuration;

    @BeforeEach
    void prepareTest() {
        configuration = ConfigurationObjectMother.createCompleteConfigurationWithoutEntitiesAndControllers();
    }

    @Test
    void thatGenerates(@TempDir File projectFolder) throws IOException {
        Path javaTestDir = Paths.get(projectFolder.getPath(), "src/test/java");
        SpringBootContextTestGenerator.generate(configuration.getBasePackage()).writeTo(javaTestDir);
        assertTestFolderExists(projectFolder);
        assertTestClassExists(projectFolder);
    }

    private void assertTestFolderExists(File projectFolder) {
        Path testPath = Paths.get(projectFolder.getPath(), "/src/test/");
        assertTrue(Files.exists(testPath), "Test file not generated");
    }

    private void assertTestClassExists(File projectFolder) {
        Path applicationClassPath = Paths.get(
                projectFolder.getPath(),
                "/src/test/java/the/group/artifact/ApplicationTests.java"
        );
        assertTrue(Files.exists(applicationClassPath), "NameApplicationTests.java not generated");
    }

}
