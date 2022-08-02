package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

public abstract class AbstractResourceBuilder<C extends JavaContext> extends AbstractJavaClassBuilder<C> {

    public AbstractResourceBuilder(C ctx, Configuration configuration) {
        super(ctx, configuration);
    }
}
