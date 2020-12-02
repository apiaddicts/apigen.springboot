package net.cloudappi.apigen.generatorcore.generator.web.resource.input;

import net.cloudappi.apigen.generatorcore.config.controller.Controller;
import net.cloudappi.apigen.generatorcore.config.controller.Endpoint;
import net.cloudappi.apigen.generatorcore.utils.Mapping;

public class InputResourceBuilderFactory {

    private InputResourceBuilderFactory() {
        // Intentionally blank
    }

    public static InputResourceBuilder create(Controller controller, Endpoint endpoint, String basePackage) {
        Mapping mapping = new Mapping(controller.getMapping());
        return new AllInputResourceBuilder(mapping, endpoint, basePackage);
    }
}
