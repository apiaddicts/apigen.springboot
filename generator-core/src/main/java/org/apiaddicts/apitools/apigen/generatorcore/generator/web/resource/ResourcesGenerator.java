package org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Request;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.input.InputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.input.InputResourceBuilderFactory;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output.OutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output.OutputResourceBuilderFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class ResourcesGenerator extends AbstractGenerator {

    private List<InputResourceBuilder> inputBuilders = new ArrayList<>();
    private List<OutputResourceBuilder> outputBuilders = new ArrayList<>();
    private ResourcesData resourcesData;

    public ResourcesGenerator(Collection<Controller> controllers, String basePackage) {
        for (Controller controller : controllers) {
            for (Endpoint endpoint : controller.getEndpoints()) {
                processRequest(controller, endpoint, basePackage);
                processResponse(controller, endpoint, basePackage);
            }
        }
        resourcesData = new ResourcesData(inputBuilders, outputBuilders);
    }

    private void processRequest(Controller controller, Endpoint endpoint, String basePackage) {
        Request request = endpoint.getRequest();
        if (request == null) return;
        inputBuilders.add(InputResourceBuilderFactory.create(controller, endpoint, basePackage));
    }

    private void processResponse(Controller controller, Endpoint endpoint, String basePackage) {
        Response response = endpoint.getResponse();
        if (response == null || response.getAttributes() == null) return;
        outputBuilders.add(OutputResourceBuilderFactory.create(controller, endpoint, basePackage));
    }

    @Override
    protected Collection<AbstractClassBuilder> getBuilders() {
        ArrayList<AbstractClassBuilder> builders = new ArrayList<>();
        builders.addAll(inputBuilders);
        builders.addAll(outputBuilders);
        return builders;
    }

    public ResourcesData getResourcesData() {
        return resourcesData;
    }
}
