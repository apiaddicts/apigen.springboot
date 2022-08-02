package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;

public abstract class ResponseBuilder<C extends ApigenContext> extends AbstractJavaClassBuilder<C> {

    public ResponseBuilder(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }
}
