package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContextObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LombokConfigFileGeneratorTest {

    Configuration cfg;
    JavaContext context = JavaContextObjectMother.create();

    @BeforeEach
    void prepareTest() {
        cfg = ConfigurationObjectMother.create();
    }

    @Test
    void thatGenerates(@TempDir File projectFolder) throws IOException {

        new LombokConfigFileGenerator<>(context, cfg).generate(projectFolder.toPath());
        assertLombokConfigExists(projectFolder);
    }

    private void assertLombokConfigExists(File projectFolder) {
        Path path = Paths.get(projectFolder.getPath(), "/lombok.config");
        assertTrue(Files.exists(path), "lombok.config file not generated");
    }
}
