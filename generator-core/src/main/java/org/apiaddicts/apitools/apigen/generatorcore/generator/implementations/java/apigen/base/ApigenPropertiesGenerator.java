package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.JavaPropertiesFileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.JavaPropertiesGenerator;

import java.util.Arrays;
import java.util.Map;

public class ApigenPropertiesGenerator<C extends ApigenContext> extends JavaPropertiesGenerator<C> {

    private Map<String, Object> extensions;
    public ApigenPropertiesGenerator(C ctx, Configuration cfg, Map<String, Object> extensions) {
        super(ctx, cfg);
        this.extensions = extensions;
    }

    @Override
    public void init() {
        builders = Arrays.asList(
                new ApigenPropertiesFileBuilder<>(ctx, cfg, this.extensions),
                new ApigenDevPropertiesFileBuilder<>(ctx, cfg),
                new JavaPropertiesFileBuilder<>("application-pre.properties", ctx, cfg),
                new JavaPropertiesFileBuilder<>("application-pro.properties", ctx, cfg),
                new JavaPropertiesFileBuilder<>("application-test.properties", ctx, cfg)
        );
    }
}
