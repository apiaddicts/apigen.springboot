package net.cloudappi.apigen.generatorcore.generator.web.controller;

import net.cloudappi.apigen.generatorcore.config.controller.Controller;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntitiesData;

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
