package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

public class EntityBuilderFactoryImpl<C extends JavaContext> implements EntityBuilderFactory<C> {
    public EntityBuilder<C> create(Entity entity, C ctx, Configuration cfg) {
        return new EntityBuilder<>(entity, ctx, cfg);
    }
}
