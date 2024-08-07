package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.JavaConstants;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.base.AbstractPropertiesFileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

public class JavaPropertiesFileBuilder<C extends JavaContext> extends AbstractPropertiesFileBuilder<C> {

    public JavaPropertiesFileBuilder(String filename, C ctx, Configuration cfg) {
        super(filename, ctx, cfg);
    }

    @Override
    protected void init() {
        // Intentional blank
    }

    @Override
    protected String getDirectory() {
        return JavaConstants.RESOURCES_PATH;
    }
}
