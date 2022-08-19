package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.JavaConstants;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.base.AbstractPropertiesFileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.util.Map;

public class JavaPropertiesFileBuilder<C extends JavaContext> extends AbstractPropertiesFileBuilder<C> {

    public JavaPropertiesFileBuilder(String filename, C ctx, Configuration cfg)  {
        super(filename, ctx, cfg);
    }

    public JavaPropertiesFileBuilder(String filename, C ctx, Configuration cfg, Map<String, Object> extensions){
        super(filename, ctx, cfg, extensions);
    }

    @Override
    protected void init(){
        // Intentional blank
    }

    @Override
    protected String getDirectory() {
        return JavaConstants.RESOURCES_PATH;
    }
}
    