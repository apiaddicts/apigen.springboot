package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.ResponseBuilder;

@FunctionalInterface
public interface ResponseBuilderFactory<C extends ApigenContext> {

    ResponseBuilder<C> generate(Controller controller, Endpoint endpoint, C ctx, Configuration cfg);
}
