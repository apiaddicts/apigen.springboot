package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.FileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.util.Collections;
import java.util.List;


public class ApplicationGenerator<C extends JavaContext> extends AbstractJavaGenerator<C> {

    protected FileBuilder builder;

    public ApplicationGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    public List<FileBuilder> getBuilders() {
        return Collections.singletonList(builder);
    }

    @Override
    public void init() {
        builder = new ApplicationBuilder<>(ctx, cfg);
    }

}
