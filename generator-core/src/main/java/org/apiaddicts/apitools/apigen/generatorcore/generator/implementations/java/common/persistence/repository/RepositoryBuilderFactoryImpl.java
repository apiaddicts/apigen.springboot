package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.repository;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

public class RepositoryBuilderFactoryImpl<C extends JavaContext> implements RepositoryBuilderFactory<C> {
    public RepositoryBuilder<C> create(Entity entity, C ctx, Configuration cfg) {
        return new RepositoryBuilder<>(entity, ctx, cfg);
    }
}
