package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.AbstractGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class LombokConfigFileGenerator<C extends JavaContext> extends AbstractGenerator<C> {

    public LombokConfigFileGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    public void generate(Path projectPath) throws IOException {
        String fileName = "lombok.config";
        Path filePath = projectPath.resolve(fileName);
        Files.write(filePath, getLines());
    }

    protected List<String> getLines() {
        return Arrays.asList("lombok.addLombokGeneratedAnnotation=true");
    }


}
