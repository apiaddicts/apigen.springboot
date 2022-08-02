package org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractFileBuilder<C extends Context> implements FileBuilder {
    
    protected final C ctx;
    protected final Configuration cfg;

    public AbstractFileBuilder(C ctx, Configuration cfg) {
        this.ctx = ctx;
        this.cfg = cfg;
    }

    protected void writeToFile(Path propertiesDir, String fileName, String content) throws IOException {
        File file = Paths.get(propertiesDir.toString(), fileName).toFile();
        file.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
        }
    }
}
