package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.service;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

@FunctionalInterface
public interface ServiceBuilderFactory<C extends JavaContext> {
    ServiceBuilder<C> create(Entity entity, C ctx, Configuration cfg);
}
