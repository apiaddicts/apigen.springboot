package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitIgnoreFileGeneratorTest {

    Configuration cfg;
    Context context = null;

    @BeforeEach
    void prepareTest() {
        cfg = ConfigurationObjectMother.create();
    }

    @Test
    void thatGenerates(@TempDir File projectFolder) throws IOException {
        new GitIgnoreFileGenerator<>(context, cfg).generate(projectFolder.toPath());
        assertGitIgnoreExists(projectFolder);
    }

    private void assertGitIgnoreExists(File projectFolder) {
        Path path = Paths.get(projectFolder.getPath(), "/.gitignore");
        assertTrue(Files.exists(path), ".gitignore file not generated");
    }
}
