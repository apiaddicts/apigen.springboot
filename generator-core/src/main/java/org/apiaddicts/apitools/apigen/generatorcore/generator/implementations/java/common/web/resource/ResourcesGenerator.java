package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Request;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input.InputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input.InputResourceBuilderFactory;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input.InputResourceBuilderFactoryImpl;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.OutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.OutputResourceBuilderFactory;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.OutputResourceBuilderFactoryImpl;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ResourcesGenerator<C extends JavaContext> extends AbstractJavaGenerator<C> {

    protected final List<InputResourceBuilder> inputBuilders = new ArrayList<>();
    protected final List<OutputResourceBuilder> outputBuilders = new ArrayList<>();

    protected final InputResourceBuilderFactory<C> inputFactory;
    protected final OutputResourceBuilderFactory<C> outputFactory;

    public ResourcesGenerator(C ctx, Configuration cfg) {
        this(ctx, cfg, new InputResourceBuilderFactoryImpl<>(), new OutputResourceBuilderFactoryImpl<>());
    }

    public ResourcesGenerator(C ctx, Configuration cfg, InputResourceBuilderFactory<C> inputFactory, OutputResourceBuilderFactory<C> outputFactory) {
        super(ctx, cfg);
        this.inputFactory = inputFactory;
        this.outputFactory = outputFactory;
    }

    @Override
    public void init() {
        for (Controller controller : cfg.getControllers()) {
            for (Endpoint endpoint : controller.getEndpoints()) {
                processRequest(controller, endpoint);
                processResponse(controller, endpoint);
            }
        }
        ctx.setResourcesData(new JavaResourcesData(inputBuilders, outputBuilders));
    }

    private void processRequest(Controller controller, Endpoint endpoint) {
        Request request = endpoint.getRequest();
        if (request == null) return;
        inputBuilders.add(inputFactory.create(controller, endpoint, ctx, cfg));
    }

    private void processResponse(Controller controller, Endpoint endpoint) {
        Response response = endpoint.getResponse();
        if (response == null || response.getAttributes() == null) return;
        outputBuilders.add(outputFactory.create(controller, endpoint, ctx, cfg));
    }

    @Override
    public List<AbstractResourceBuilder> getBuilders() {
        ArrayList<AbstractResourceBuilder> builders = new ArrayList<>();
        builders.addAll(inputBuilders);
        builders.addAll(outputBuilders);
        return builders;
    }
}
