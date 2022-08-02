package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.service;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ServicesGenerator<C extends JavaContext> extends AbstractJavaGenerator<C> {
    protected final Map<String, ServiceBuilder<C>> builders = new HashMap<>();
    protected final ServiceBuilderFactory<C> factory;

    public ServicesGenerator(C ctx, Configuration cfg) {
        this(ctx, cfg, new ServiceBuilderFactoryImpl<>());
    }

    public ServicesGenerator(C ctx, Configuration cfg, ServiceBuilderFactory<C> factory) {
        super(ctx, cfg);
        this.factory = factory;
    }

    @Override
    public List<ServiceBuilder<C>> getBuilders() {
        return new ArrayList<>(builders.values());
    }

    @Override
    public void init() {
        cfg.getEntities().forEach(entity -> builders.put(entity.getName(), factory.create(entity, ctx, cfg)));
    }
}
