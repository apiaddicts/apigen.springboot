package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Deprecated
@Slf4j
public class SpringBootBaseGenerator {

    private SpringBootBaseGenerator() {
        // Intentional blank
    }

    public static void generate(Configuration configuration, File projectFolder) throws IOException {
        Path propertiesDir = Paths.get(projectFolder.getPath(), "/src/main/resources");
        Path javaDir = Paths.get(projectFolder.getPath(), "src/main/java");
        PropertiesGenerator.generate(propertiesDir, configuration);
        new ApplicationGenerator(configuration.getBasePackage()).generate(javaDir);
        Path javaTestDir = Paths.get(projectFolder.getPath(), "src/test/java");
        SpringBootContextTestGenerator.generate(configuration.getBasePackage()).writeTo(javaTestDir);
    }
}
