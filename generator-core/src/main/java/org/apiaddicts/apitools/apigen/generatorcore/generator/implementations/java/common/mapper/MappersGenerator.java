package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class MappersGenerator<C extends JavaContext> extends AbstractJavaGenerator<C> {

    protected final Map<String, MapperBuilder<C>> builders = new HashMap<>();
    protected final MapperBuilderFactory<C> factory;

    public MappersGenerator(C ctx, Configuration cfg) {
        this(ctx, cfg, new MapperBuilderFactoryImpl<>());
    }

    public MappersGenerator(C ctx, Configuration cfg, MapperBuilderFactory<C> factory) {
        super(ctx, cfg);
        this.factory = factory;
    }

    @Override
    public List<MapperBuilder<C>> getBuilders() {
        return new ArrayList<>(builders.values());
    }

    @Override
    public void init() {
        cfg.getEntities().stream().filter(e -> !builders.containsKey(e.getName())).
                forEach(e -> builders.put(e.getName(), factory.create(e, ctx, cfg)));
    }
}
