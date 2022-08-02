package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.persistence.repository;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenRepository;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.repository.RepositoryBuilder;

public class ApigenRepositoryBuilder<C extends ApigenContext> extends RepositoryBuilder<C> {

    public ApigenRepositoryBuilder(Entity entity, C ctx, Configuration cfg) {
        super(entity, ctx, cfg);
    }

    @Override
    protected ParameterizedTypeName getInterface() {
        return ParameterizedTypeName.get(
                ClassName.get(ApigenRepository.class),
                entityType,
                identifierType
        );
    }
}
