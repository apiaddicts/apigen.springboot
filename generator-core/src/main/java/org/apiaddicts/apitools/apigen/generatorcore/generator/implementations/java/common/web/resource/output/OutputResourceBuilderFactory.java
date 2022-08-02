package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

@FunctionalInterface
public interface OutputResourceBuilderFactory<C extends JavaContext> {
    OutputResourceBuilder create(Controller controller, Endpoint endpoint, C ctx, Configuration cfg);
}
