package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.ResponseBuilder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ApigenResponsesGenerator<C extends ApigenContext> extends AbstractJavaGenerator<C> {

    private final List<ResponseBuilder> builders = new ArrayList<>();
    private final ResponseBuilderFactory<C> factory;

    public ApigenResponsesGenerator(C ctx, Configuration cfg) {
        this(ctx, cfg, new ResponseBuilderFactoryImpl<>());
    }

    public ApigenResponsesGenerator(C ctx, Configuration cfg, ResponseBuilderFactory<C> factory) {
        super(ctx, cfg);
        this.factory = factory;
    }

    @Override
    public List<ResponseBuilder> getBuilders() {
        return new ArrayList<>(builders);
    }

    @Override
    public void init() {
        for (Controller controller : cfg.getControllers()) {
            for (Endpoint endpoint : controller.getEndpoints()) {
                Response response = endpoint.getResponse();
                if (response == null || response.getAttributes() == null || !response.getIsStandard()) continue;
                builders.add(factory.generate(controller, endpoint, ctx, cfg));
            }
        }
    }
}
