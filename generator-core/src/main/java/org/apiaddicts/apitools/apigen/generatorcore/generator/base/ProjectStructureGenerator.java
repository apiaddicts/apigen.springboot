package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Deprecated
public class ProjectStructureGenerator {

    private ProjectStructureGenerator() {
        // Intentional blank
    }

    public static File generate(Configuration configuration) throws IOException {
        Path basePath = Files.createTempDirectory("apigen-project-folder-" + System.nanoTime());
        Path rootFolder = Paths.get(basePath.toString(), configuration.getName());
        String appFolder = configuration.getBasePackage().replace(".", "/");
        Files.createDirectories(Paths.get(rootFolder.toString(), "/src/main/resources"));
        Files.createDirectories(Paths.get(rootFolder.toString(), "/src/main/java/", appFolder));
        Files.createDirectories(Paths.get(rootFolder.toString(), "/src/test/java/", appFolder));
        return rootFolder.toFile();
    }
}
