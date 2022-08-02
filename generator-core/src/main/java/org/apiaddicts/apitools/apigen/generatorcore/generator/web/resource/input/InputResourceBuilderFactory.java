package org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.input;

import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

@Deprecated
public class InputResourceBuilderFactory {

    private InputResourceBuilderFactory() {
        // Intentionally blank
    }

    public static InputResourceBuilder create(Controller controller, Endpoint endpoint, String basePackage) {
        Mapping mapping = new Mapping(controller.getMapping());
        return new AllInputResourceBuilder(mapping, endpoint, basePackage);
    }
}
