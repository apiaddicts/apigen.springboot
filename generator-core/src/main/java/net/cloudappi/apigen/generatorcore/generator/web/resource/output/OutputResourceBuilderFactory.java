package net.cloudappi.apigen.generatorcore.generator.web.resource.output;

import net.cloudappi.apigen.generatorcore.config.controller.Controller;
import net.cloudappi.apigen.generatorcore.config.controller.Endpoint;
import net.cloudappi.apigen.generatorcore.config.controller.Response;
import net.cloudappi.apigen.generatorcore.utils.Mapping;

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
