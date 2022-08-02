package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints.EndpointBuilderFactory;

public class GenericControllerBuilder<C extends JavaContext> extends ControllerBuilder<C> {

    public GenericControllerBuilder(Controller controller, C ctx, Configuration cfg) {
        super(controller, ctx, cfg);
    }

    public GenericControllerBuilder(Controller controller, C ctx, Configuration cfg, EndpointBuilderFactory<C> endpointFactory) {
        super(controller, ctx, cfg, endpointFactory);
    }

    @Override
    protected String getName() {
        return requestMapping.toName() + "Controller";
    }

    @Override
    protected String getTag() {
        return requestMapping.toName();
    }

    @Override
    public String getPackage() {
        return concatPackage(cfg.getBasePackage(), requestMapping.toName().toLowerCase(), "web");
    }

    @Override
    public void initialize() {
        initializeBuilder();
        generateEndpoints();
    }
}
