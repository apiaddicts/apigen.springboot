package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GitIgnoreFileGeneratorTest {

    @Test
    void thatGenerates(@TempDir File projectFolder) throws IOException {
        GitIgnoreGenerator.generate(projectFolder);
        assertGitIgnoreExists(projectFolder);
    }

    private void assertGitIgnoreExists(File projectFolder) {
        Path path = Paths.get(projectFolder.getPath(), "/.gitignore");
        assertTrue(Files.exists(path), ".gitignore file not generated");
    }
}