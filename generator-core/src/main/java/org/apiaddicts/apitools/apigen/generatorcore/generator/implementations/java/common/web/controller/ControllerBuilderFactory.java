package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

@FunctionalInterface
public interface ControllerBuilderFactory<C extends JavaContext> {
    ControllerBuilder<C> create(Controller controller, C ctx, Configuration cfg);
}
