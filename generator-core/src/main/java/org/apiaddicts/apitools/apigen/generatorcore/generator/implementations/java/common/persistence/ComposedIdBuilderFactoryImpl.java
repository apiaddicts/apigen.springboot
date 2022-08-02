package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

public class ComposedIdBuilderFactoryImpl<C extends JavaContext> implements ComposedIdBuilderFactory<C> {
    public ComposedIdBuilder create(Entity entity, C ctx, Configuration cfg) {
        return new ComposedIdBuilder(entity, ctx, cfg);
    }
}
