package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.JavaPropertiesFileBuilder;

public class ApigenDevPropertiesFileBuilder<C extends ApigenContext> extends JavaPropertiesFileBuilder<C> {

    public ApigenDevPropertiesFileBuilder(C ctx, Configuration cfg) {
        super("application-dev.properties", ctx, cfg);
    }

    @Override
    protected void init() {
        addProperty("logging.level." + cfg.getBasePackage(), "debug");
        addProperty("spring.jpa.show-sql", "true");
        addProperty("spring.jpa.defer-datasource-initialization", "true");
    }
}
