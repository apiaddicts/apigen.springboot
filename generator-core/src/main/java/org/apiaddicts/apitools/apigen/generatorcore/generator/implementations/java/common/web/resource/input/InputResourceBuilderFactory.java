package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

@FunctionalInterface
public interface InputResourceBuilderFactory<C extends JavaContext> {
    InputResourceBuilder create(Controller controller, Endpoint endpoint, C ctx, Configuration cfg);
}
