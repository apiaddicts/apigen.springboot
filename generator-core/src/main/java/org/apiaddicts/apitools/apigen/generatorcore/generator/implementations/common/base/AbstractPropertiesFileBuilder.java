package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.AbstractFileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Context;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

public abstract class AbstractPropertiesFileBuilder<C extends Context> extends AbstractFileBuilder<C> {

    protected final LinkedHashMap<String, String> properties = new LinkedHashMap<>();
    protected final String filename;

    public AbstractPropertiesFileBuilder(String filename, C ctx, Configuration cfg)  {
        super(ctx, cfg);
        this.filename = filename;
        init();
    }

    protected abstract void init() ;

    protected void addProperty(String key, String value) {
        properties.put(key, value);
    }

    protected void removeProperty(String key) {
        properties.remove(key);
    }

    protected String getContent() {
        StringBuilder sb = new StringBuilder();
        properties.forEach((key, value) -> {
            sb.append(key);
            sb.append("=");
            sb.append(value);
            sb.append("\n");
        });
        return sb.toString();
    }

    protected String getFilename() {
        return filename;
    }

    protected String getDirectory() {
        return "";
    }

    @Override
    public void generate(Path projectPath) throws IOException {
        writeToFile(Paths.get(projectPath.toString(), getDirectory()), getFilename(), getContent());
    }
}
