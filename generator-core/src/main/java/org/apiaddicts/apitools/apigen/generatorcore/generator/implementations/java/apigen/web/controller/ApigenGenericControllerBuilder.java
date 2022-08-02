package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints.ApigenGenericEndpointBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.GenericControllerBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints.EndpointBuilderFactory;

public class ApigenGenericControllerBuilder<C extends ApigenContext> extends GenericControllerBuilder<C> {

    public ApigenGenericControllerBuilder(Controller controller, C ctx, Configuration cfg) {
        super(controller, ctx, cfg, ApigenGenericEndpointBuilder::new);
    }

    public ApigenGenericControllerBuilder(Controller controller, C ctx, Configuration cfg, EndpointBuilderFactory<C> endpointFactory) {
        super(controller, ctx, cfg, endpointFactory);
    }
}
