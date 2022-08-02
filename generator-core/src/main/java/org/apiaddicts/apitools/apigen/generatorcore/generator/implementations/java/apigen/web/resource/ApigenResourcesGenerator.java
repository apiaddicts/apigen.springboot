package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output.ApigenOutputResourceBuilderFactoryImpl;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.ResourcesGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input.InputResourceBuilderFactoryImpl;

public class ApigenResourcesGenerator<C extends ApigenContext> extends ResourcesGenerator<C> {
    public ApigenResourcesGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg, new InputResourceBuilderFactoryImpl<>(), new ApigenOutputResourceBuilderFactoryImpl<>());
    }
}
