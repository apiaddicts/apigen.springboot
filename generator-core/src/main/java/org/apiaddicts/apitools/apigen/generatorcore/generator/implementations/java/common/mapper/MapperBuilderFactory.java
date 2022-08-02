package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.mapper;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

@FunctionalInterface
public interface MapperBuilderFactory<C extends JavaContext> {
    MapperBuilder<C> create(Entity entity, C ctx, Configuration cfg);
}
