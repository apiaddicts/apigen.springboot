package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.*;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

public class ResponseBuilderFactoryImpl<C extends ApigenContext> implements ResponseBuilderFactory<C> {

    public ResponseBuilder generate(Controller controller, Endpoint endpoint, C ctx, Configuration cfg) {
        Response response = endpoint.getResponse();
        String entityName = response.getRelatedEntity();
        if (entityName != null) {
            if (response.getIsCollection()) {
                return new EntityListResponseBuilder(response, ctx, cfg);
            } else {
                return new EntitySimpleResponseBuilder(response, ctx, cfg);
            }
        } else {
            Mapping mapping = new Mapping(controller.getMapping());
            if (response.getIsCollection()) {
                return new ResourceListResponseBuilder(mapping, endpoint, ctx, cfg);
            } else {
                return new ResourceSimpleResponseBuilder(mapping, endpoint, ctx, cfg);
            }
        }
    }
}
