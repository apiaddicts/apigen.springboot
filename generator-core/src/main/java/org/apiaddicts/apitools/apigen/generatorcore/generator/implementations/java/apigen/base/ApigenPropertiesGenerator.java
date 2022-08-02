package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.JavaPropertiesFileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.JavaPropertiesGenerator;

import java.util.Arrays;

public class ApigenPropertiesGenerator<C extends ApigenContext> extends JavaPropertiesGenerator<C> {
    public ApigenPropertiesGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    public void init() {
        builders = Arrays.asList(
                new ApigenPropertiesFileBuilder<>(ctx, cfg),
                new ApigenDevPropertiesFileBuilder<>(ctx, cfg),
                new JavaPropertiesFileBuilder<>("application-pre.properties", ctx, cfg),
                new JavaPropertiesFileBuilder<>("application-pro.properties", ctx, cfg),
                new JavaPropertiesFileBuilder<>("application-test.properties", ctx, cfg)
        );
    }
}
