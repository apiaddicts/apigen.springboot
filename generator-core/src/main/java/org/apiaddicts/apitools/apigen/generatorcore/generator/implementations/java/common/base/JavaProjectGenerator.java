package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.AbstractGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.JavaConstants;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaProjectGenerator<C extends JavaContext> extends AbstractGenerator<C> {

    public JavaProjectGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }


    public void generate(Path basePath) throws IOException {

        Path rootFolder = Paths.get(basePath.toString(), cfg.getName());
        String appFolder = cfg.getBasePackage().replace(".", "/");
        Files.createDirectories(Paths.get(rootFolder.toString(), JavaConstants.RESOURCES_PATH));
        Files.createDirectories(Paths.get(rootFolder.toString(), JavaConstants.CLASSES_PATH, appFolder));
        Files.createDirectories(Paths.get(rootFolder.toString(), JavaConstants.TEST_CLASSES_PATH, appFolder));

    }
}

