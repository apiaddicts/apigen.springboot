package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.ControllerBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.ControllerBuilderFactory;

public class ApigenControllerBuilderFactoryImpl<C extends ApigenContext> implements ControllerBuilderFactory<C> {

    public ControllerBuilder<C> create(Controller controller, C ctx, Configuration cfg) {
        if (controller.getEntity() == null) {
            return new ApigenGenericControllerBuilder<>(controller, ctx, cfg);
        } else {
            return new ApigenEntityControllerBuilder<>(controller, ctx, cfg);
        }
    }
}
