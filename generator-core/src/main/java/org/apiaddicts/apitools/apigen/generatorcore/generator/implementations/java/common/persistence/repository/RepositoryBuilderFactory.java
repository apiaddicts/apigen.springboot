package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.repository;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

@FunctionalInterface
public interface RepositoryBuilderFactory<C extends JavaContext> {
    RepositoryBuilder<C> create(Entity entity, C ctx, Configuration cfg);
}
