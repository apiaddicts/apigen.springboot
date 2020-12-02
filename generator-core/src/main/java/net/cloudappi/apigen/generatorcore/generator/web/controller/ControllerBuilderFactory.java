package net.cloudappi.apigen.generatorcore.generator.web.controller;

import net.cloudappi.apigen.generatorcore.config.controller.Controller;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntitiesData;

public class ControllerBuilderFactory {

    private ControllerBuilderFactory() {
        // Intentionally blank
    }

    public static ControllerBuilder create(Controller controller, EntitiesData entitiesData, String basePackage) {
        if (controller.getEntity() == null) {
            return new ResourceControllerBuilder(controller, entitiesData, basePackage);
        } else {
            return new EntityControllerBuilder(controller, entitiesData, basePackage);
        }
    }
}
