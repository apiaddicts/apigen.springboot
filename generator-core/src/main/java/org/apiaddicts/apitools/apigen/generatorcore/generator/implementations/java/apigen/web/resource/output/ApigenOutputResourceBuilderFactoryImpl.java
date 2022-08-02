package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.GenericOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.OutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.OutputResourceBuilderFactory;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

public class ApigenOutputResourceBuilderFactoryImpl<C extends ApigenContext> implements OutputResourceBuilderFactory<C> {

    public OutputResourceBuilder<C> create(Controller controller, Endpoint endpoint, C ctx, Configuration cfg) {
        if (endpoint.getResponse().getRelatedEntity() == null) {
            Mapping mapping = new Mapping(controller.getMapping());
            return new GenericOutputResourceBuilder<>(mapping, endpoint, ctx, cfg);
        } else {
            return new ApigenEntityOutputResourceBuilder<>(endpoint, ctx, cfg);
        }
    }
}
