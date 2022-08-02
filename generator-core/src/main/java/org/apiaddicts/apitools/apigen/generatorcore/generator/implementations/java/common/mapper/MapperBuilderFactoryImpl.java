package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.mapper;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

public class MapperBuilderFactoryImpl<C extends JavaContext> implements MapperBuilderFactory<C> {
    public MapperBuilder<C> create(Entity entity, C ctx, Configuration cfg) {
        return new MapperBuilder<>(entity, ctx, cfg);
    }
}
