package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.service;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

public class ServiceBuilderFactoryImpl<C extends JavaContext> implements ServiceBuilderFactory<C> {

    @Override
    public ServiceBuilder<C> create(Entity entity, C ctx, Configuration cfg) {
        return new ServiceBuilder<>(entity, ctx, cfg);
    }
}
