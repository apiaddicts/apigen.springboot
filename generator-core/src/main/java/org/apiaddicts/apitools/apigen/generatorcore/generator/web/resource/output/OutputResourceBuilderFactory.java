package org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output;

import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

@Deprecated
public class OutputResourceBuilderFactory {

    private OutputResourceBuilderFactory() {
        // Intentionally blank
    }

    public static OutputResourceBuilder create(Controller controller, Endpoint endpoint, String basePackage) {
        Response response = endpoint.getResponse();
        String entityName = response.getRelatedEntity();
        if (entityName == null) {
            Mapping mapping = new Mapping(controller.getMapping());
            return new ResourceOutputResourceBuilder(mapping, endpoint, basePackage);
        } else {
            return new EntityOutputResourceBuilder(response, basePackage);
        }
    }
}
