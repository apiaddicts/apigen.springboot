package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.persistence;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.ComposedIdBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntitiesGenerator;

public class ApigenEntitiesGenerator<C extends ApigenContext> extends EntitiesGenerator<C> {
    public ApigenEntitiesGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg,
                ApigenEntityBuilder::new,
                ComposedIdBuilder::new
                );
    }
}
