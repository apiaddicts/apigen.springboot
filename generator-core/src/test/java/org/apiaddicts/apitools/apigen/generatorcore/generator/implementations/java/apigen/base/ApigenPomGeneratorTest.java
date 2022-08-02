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

class ApigenPomGeneratorTest {

    Configuration cfg;
    ApigenContext context;
    @BeforeEach
    void prepareTest() {
        cfg = ConfigurationObjectMother.create();
        context = ApigenContextObjectMother.create();
    }

    @Test
    void thatGenerates(@TempDir File projectFolder) throws IOException {
        new ApigenPomGenerator<>(context, cfg).generate(projectFolder.toPath());
        assertPomExists(projectFolder);
    }

    private void assertPomExists(File projectFolder) {
        Path pomPath = Paths.get(projectFolder.getPath(), "/pom.xml");
        assertTrue(Files.exists(pomPath), "pom.xml file not generated");
    }
}
