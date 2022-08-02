package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

@FunctionalInterface
public interface EndpointBuilderFactory<C extends JavaContext> {

    EndpointBuilder<C> create(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg);
}
