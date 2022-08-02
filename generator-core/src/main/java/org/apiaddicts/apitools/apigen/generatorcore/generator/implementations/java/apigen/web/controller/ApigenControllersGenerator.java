package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.ControllersGenerator;

public class ApigenControllersGenerator<C extends ApigenContext> extends ControllersGenerator<C> {

    public ApigenControllersGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg, new ApigenControllerBuilderFactoryImpl<>());
    }
}
