package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller;

import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData;

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
