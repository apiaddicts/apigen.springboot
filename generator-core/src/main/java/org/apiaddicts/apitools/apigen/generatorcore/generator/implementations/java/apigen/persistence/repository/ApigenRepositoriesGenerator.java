package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.persistence.repository;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.repository.RepositoriesGenerator;

public class ApigenRepositoriesGenerator<C extends ApigenContext> extends RepositoriesGenerator<C> {
    public ApigenRepositoriesGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg, ApigenRepositoryBuilder::new);
    }
}
