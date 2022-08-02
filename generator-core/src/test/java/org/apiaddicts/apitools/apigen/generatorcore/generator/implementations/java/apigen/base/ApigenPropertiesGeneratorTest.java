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

class ApigenPropertiesGeneratorTest {
    Configuration cfg;
    ApigenContext context = ApigenContextObjectMother.create();
    @BeforeEach
    void prepareTest() {
        cfg = ConfigurationObjectMother.create();
    }

    @Test
    void thatGenerates(@TempDir File projectFolder) throws IOException {
        new ApigenPropertiesGenerator<>(context, cfg).generate(projectFolder.toPath());
        assertPropertiesFilesExists(projectFolder);
    }

    private void assertPropertiesFilesExists(File projectFolder) {
        String resourcesPath = projectFolder.toPath() + "/src/main/resources";
        Path prop = Paths.get(resourcesPath, "application.properties");
        assertTrue(Files.exists(prop), "Properties file not generated");
        String[] profiles = {"dev", "pre", "pro", "test"};
        for (String profile : profiles) {
            Path profProp = Paths.get(resourcesPath, "application-" + profile + ".properties");
            assertTrue(Files.exists(profProp), "Profile " + profile + " config file not generated");
        }
    }
}
