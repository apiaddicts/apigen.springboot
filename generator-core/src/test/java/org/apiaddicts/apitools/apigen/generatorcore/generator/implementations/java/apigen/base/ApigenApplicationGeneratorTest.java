package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApigenApplicationGeneratorTest {

    Configuration cfg;
    ApigenContext ctx = ApigenContextObjectMother.create();
    @BeforeEach
    void prepareTest() {
        cfg = ConfigurationObjectMother.create();
    }

    @Test
    void thatGenerates(@TempDir File projectFolder) throws IOException {
        new ApigenApplicationGenerator<>(ctx, cfg).generate(projectFolder.toPath());
        assertApplicationClassExists(projectFolder);
    }

    private void assertApplicationClassExists(File projectFolder) {
        Path applicationClassPath = Paths.get(
                projectFolder.getPath(),
                "/src/main/java/the/group/artifact/Application.java"
        );
        assertTrue(Files.exists(applicationClassPath), "Application.java not generated");
    }
}
