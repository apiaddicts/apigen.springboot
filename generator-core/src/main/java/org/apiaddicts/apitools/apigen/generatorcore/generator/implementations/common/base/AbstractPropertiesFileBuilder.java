package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.AbstractFileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Context;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractPropertiesFileBuilder<C extends Context> extends AbstractFileBuilder<C> {

    protected final LinkedHashMap<String, String> properties = new LinkedHashMap<>();
    protected final String filename;
    protected Map<String, Object> extensions = new HashMap<>();

    public AbstractPropertiesFileBuilder(String filename, C ctx, Configuration cfg)  {
        super(ctx, cfg);
        this.filename = filename;
        init();
    }

    public AbstractPropertiesFileBuilder(String filename, C ctx, Configuration cfg, Map<String, Object> extensions)  {
        super(ctx, cfg);
        this.filename = filename;
        this.extensions = extensions;
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
