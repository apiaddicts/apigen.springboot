package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.ApplicationGenerator;

public class ApigenApplicationGenerator<C extends ApigenContext> extends ApplicationGenerator<C> {
    public ApigenApplicationGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    public void init() {
        builder = new ApigenApplicationBuilder<>(ctx, cfg);
    }
}
