package org.apiaddicts.apitools.apigen.generatorcore.generator.web.response;

import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

@Deprecated
public class ResponseBuilderFactory {

    private ResponseBuilderFactory() {
        // Intentionally blank
    }

    public static ResponseBuilder create(Controller controller, Endpoint endpoint, String basePackage) {
        Response response = endpoint.getResponse();
        String entityName = response.getRelatedEntity();
        if (entityName != null) {
            if (response.getIsCollection()) {
                return new EntityListResponseBuilder(entityName, response.getCollectionName(), basePackage);
            } else {
                return new SimpleResponseBuilder(entityName, basePackage);
            }
        } else {
            Mapping mapping = new Mapping(controller.getMapping());
            if (response.getIsCollection()) {
                return new ResourceListResponseBuilder(mapping, endpoint, basePackage);
            } else {
                return new ResourceSimpleResponseBuilder(mapping, endpoint, basePackage);
            }
        }
    }
}
