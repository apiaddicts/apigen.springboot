package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.repository;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.FileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RepositoriesGenerator<C extends JavaContext> extends AbstractJavaGenerator<C> {

    protected final Map<String, RepositoryBuilder<C>> builders = new HashMap<>();
    protected final RepositoryBuilderFactory<C> factory;

    public RepositoriesGenerator(C ctx, Configuration cfg) {
        this(ctx, cfg, new RepositoryBuilderFactoryImpl<>());
    }

    public RepositoriesGenerator(C ctx, Configuration cfg, RepositoryBuilderFactory<C> factory) {
        super(ctx, cfg);
        this.factory = factory;
    }

    @Override
    public List<? extends FileBuilder> getBuilders() {
        return new ArrayList<>(builders.values());
    }

    @Override
    public void init() {
        cfg.getEntities().forEach(entity -> {
            String name = entity.getName();
            builders.put(name, factory.create(entity, ctx, cfg));
        });
    }
}
