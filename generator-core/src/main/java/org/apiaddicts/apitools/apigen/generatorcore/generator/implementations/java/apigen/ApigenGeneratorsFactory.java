package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Generator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.GeneratorsAbstractFactory;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.base.GitIgnoreFileGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base.*;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.mapper.ApigenMappersGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.persistence.ApigenEntitiesGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.persistence.repository.ApigenRepositoriesGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.service.ApigenRelationManagersGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.service.ApigenServicesGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.ApigenControllersGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.ApigenResourcesGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.ApigenResponsesGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.ApplicationTestGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.JavaProjectGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.LombokConfigFileGenerator;

import java.util.Arrays;
import java.util.List;

public class ApigenGeneratorsFactory implements GeneratorsAbstractFactory<ApigenContext> {

    @Override
    public List<Generator> createDefault(ApigenContext ctx, Configuration cfg) {
        return Arrays.asList(
                new ApigenEntitiesGenerator<>(ctx, cfg),
                new ApigenRepositoriesGenerator<>(ctx, cfg),
                new ApigenRelationManagersGenerator<>(ctx, cfg),
                new ApigenServicesGenerator<>(ctx, cfg),
                new ApigenResourcesGenerator<>(ctx, cfg),
                new ApigenMappersGenerator<>(ctx, cfg),
                new ApigenResponsesGenerator<>(ctx, cfg),
                new ApigenControllersGenerator<>(ctx, cfg)
        );
    }

    @Override
    public List<Generator> createNonPartial(ApigenContext ctx, Configuration c) {
        return Arrays.asList(
                new JavaProjectGenerator<>(ctx, c),
                new GitIgnoreFileGenerator<>(ctx, c),
                new LombokConfigFileGenerator<>(ctx, c),
                new ApigenPropertiesGenerator<>(ctx, c),
                new ApigenPomGenerator<>(ctx, c),
                new ApigenApplicationGenerator<>(ctx, c),
                new ApplicationTestGenerator<>(ctx, c)
        );
    }

}
