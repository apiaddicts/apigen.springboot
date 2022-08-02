package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller;

import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData;

@Deprecated
public class ResourceControllerBuilder extends ControllerBuilder {

    public ResourceControllerBuilder(Controller controller, EntitiesData entitiesData, String basePackage) {
        super(controller, entitiesData, basePackage);
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
        return concatPackage(basePackage, requestMapping.toName().toLowerCase(), "web");
    }

    @Override
    public void initialize() {
        initializeBuilder();
        generateEndpoints();
    }
}
