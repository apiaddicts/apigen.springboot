package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.service;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ApigenRelationManagersGenerator<C extends ApigenContext> extends AbstractJavaGenerator<C> {

    protected final List<ApigenRelationManagerBuilder<C>> builders = new ArrayList<>();

    public ApigenRelationManagersGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    public void init() {
        cfg.getEntities().forEach(entity -> builders.add(new ApigenRelationManagerBuilder<>(entity, ctx, cfg)));
    }

    @Override
    public List<ApigenRelationManagerBuilder<C>> getBuilders() {
        return new ArrayList<>(builders);
    }
}
