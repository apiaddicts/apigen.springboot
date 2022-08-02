package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence;

import com.squareup.javapoet.TypeSpec;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class EntitiesGenerator<C extends JavaContext> extends AbstractJavaGenerator<C> {

    protected final List<EntityBuilder<C>> builders = new ArrayList<>();
    protected final List<ComposedIdBuilder<C>> idBuilders = new ArrayList<>();
    protected final EntityBuilderFactory<C> factory;
    protected final ComposedIdBuilderFactory<C> composedFactory;

    public EntitiesGenerator(C ctx, Configuration cfg) {
        this(ctx, cfg, new EntityBuilderFactoryImpl<>(), new ComposedIdBuilderFactoryImpl<>());
    }

    public EntitiesGenerator(C ctx, Configuration cfg, EntityBuilderFactory<C> factory, ComposedIdBuilderFactory<C> composedFactory) {
        super(ctx, cfg);
        this.factory = factory;
        this.composedFactory = composedFactory;
    }

    @Override
    public void init() {
        List<Entity> entities = cfg.getEntities();
        ctx.setEntityRelationManager(new JavaEntityRelationManager(entities));
        entities.forEach(e -> {
            EntityBuilder<C> entityBuilder = factory.create(e, ctx, cfg);
            builders.add(entityBuilder);
            if (entityBuilder.hasComposedID()) idBuilders.add(composedFactory.create(e, ctx, cfg));
        });
        List<TypeSpec> entitiesSpec = builders.stream().map(AbstractJavaClassBuilder::build).collect(Collectors.toList());
        ctx.setEntitiesData(new JavaEntitiesData(entitiesSpec));
    }

    @Override
    public List<AbstractJavaClassBuilder<C>> getBuilders() {
        List<AbstractJavaClassBuilder<C>> allBuilders = new ArrayList<>(builders);
        allBuilders.addAll(idBuilders);
        return allBuilders;
    }
}
