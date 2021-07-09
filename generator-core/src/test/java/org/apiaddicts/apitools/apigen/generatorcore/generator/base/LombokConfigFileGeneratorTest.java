package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LombokConfigFileGeneratorTest {

    @Test
    void thatGenerates(@TempDir File projectFolder) throws IOException {
        LombokConfigGenerator.generate(projectFolder);
        assertLombokConfigExists(projectFolder);
    }

    private void assertLombokConfigExists(File projectFolder) {
        Path path = Paths.get(projectFolder.getPath(), "/lombok.config");
        assertTrue(Files.exists(path), "lombok.config file not generated");
    }
}