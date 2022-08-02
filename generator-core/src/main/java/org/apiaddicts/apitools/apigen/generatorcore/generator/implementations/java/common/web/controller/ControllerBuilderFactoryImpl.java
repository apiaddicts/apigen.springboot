package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

public class ControllerBuilderFactoryImpl<C extends JavaContext> implements ControllerBuilderFactory<C> {

    public ControllerBuilder<C> create(Controller controller, C ctx, Configuration cfg) {
        return new GenericControllerBuilder<>(controller, ctx, cfg);
    }
}
