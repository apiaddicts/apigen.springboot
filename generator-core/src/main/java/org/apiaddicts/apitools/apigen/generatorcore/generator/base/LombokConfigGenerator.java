package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LombokConfigGenerator {

    private LombokConfigGenerator() {
        // Intentional blank
    }

    public static void generate(File projectFolder) throws IOException {
        String folder = projectFolder.getPath();
        String fileName = "lombok.config";
        Path folderPath = Paths.get(folder);
        Path filePath = folderPath.resolve(fileName);

        List<String> lines = new ArrayList<>();
        lines.add("lombok.addLombokGeneratedAnnotation=true");

        Files.write(filePath, lines);
    }
}
