package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

public class EndpointBuilderFactoryImpl<C extends JavaContext> implements EndpointBuilderFactory<C> {

    public EndpointBuilder<C> create(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        return new GenericEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);
    }
}
