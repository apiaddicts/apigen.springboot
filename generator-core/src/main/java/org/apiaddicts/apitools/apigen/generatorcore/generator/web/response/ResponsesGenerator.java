package org.apiaddicts.apitools.apigen.generatorcore.generator.web.response;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Deprecated
@Slf4j
public class ResponsesGenerator extends AbstractGenerator {

    private List<ResponseBuilder> builders = new ArrayList<>();

    public ResponsesGenerator(Collection<Controller> controllers, String basePackage) {
        for (Controller controller : controllers) {
            for (Endpoint endpoint : controller.getEndpoints()) {
                Response response = endpoint.getResponse();
                if (response == null || response.getAttributes() == null || !response.getIsStandard()) continue;
                builders.add(ResponseBuilderFactory.create(controller, endpoint, basePackage));
            }
        }
    }

    @Override
    protected Collection<AbstractClassBuilder> getBuilders() {
        return new ArrayList<>(builders);
    }
}
