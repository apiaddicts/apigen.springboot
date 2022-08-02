package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

public class InputResourceBuilderFactoryImpl<C extends JavaContext> implements InputResourceBuilderFactory<C> {

    public InputResourceBuilderFactoryImpl() {
        // Intentionally blank
    }

    public InputResourceBuilder create(Controller controller, Endpoint endpoint, C ctx, Configuration cfg) {
        Mapping mapping = new Mapping(controller.getMapping());
        return new GenericInputResourceBuilder(mapping, endpoint, ctx, cfg);
    }
}
