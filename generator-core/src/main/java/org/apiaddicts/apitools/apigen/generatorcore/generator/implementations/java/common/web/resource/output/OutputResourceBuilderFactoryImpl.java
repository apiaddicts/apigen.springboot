package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

public class OutputResourceBuilderFactoryImpl<C extends JavaContext> implements OutputResourceBuilderFactory<C> {

    public OutputResourceBuilderFactoryImpl() {
        // Intentionally blank
    }

    public OutputResourceBuilder create(Controller controller, Endpoint endpoint, C ctx, Configuration cfg) {
        Mapping mapping = new Mapping(controller.getMapping());
        return new GenericOutputResourceBuilder(mapping, endpoint, ctx, cfg);
    }
}
