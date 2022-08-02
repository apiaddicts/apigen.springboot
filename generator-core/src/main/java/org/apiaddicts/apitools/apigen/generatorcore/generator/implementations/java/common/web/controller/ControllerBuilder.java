package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints.EndpointBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints.EndpointBuilderFactory;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints.EndpointBuilderFactoryImpl;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.Modifier;
import java.util.List;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.NAME;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

public abstract class ControllerBuilder<C extends JavaContext> extends AbstractJavaClassBuilder<C> {
    protected final Mapping requestMapping;
    protected final List<Endpoint> endpoints;
    protected final EndpointBuilderFactory<C> endpointBuilderFactory;

    public ControllerBuilder(Controller controller, C ctx, Configuration cfg) {
        this(controller, ctx, cfg, new EndpointBuilderFactoryImpl<>());
    }

    public ControllerBuilder(Controller controller, C ctx, Configuration cfg, EndpointBuilderFactory<C> endpointBuilderFactory) {
        super(ctx, cfg);
        this.requestMapping = new Mapping(controller.getMapping());
        this.endpoints = controller.getEndpoints();
        this.endpointBuilderFactory = endpointBuilderFactory;
    }

    protected abstract String getName();

    protected abstract String getTag();

    protected void initializeBuilder() {
        builder = TypeSpec
                .classBuilder(getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Slf4j.class)
                .addAnnotation(RestController.class)
                .addAnnotation(
                        AnnotationSpec.builder(RequestMapping.class)
                                .addMember(VALUE, STRING, requestMapping.getValue())
                                .build()
                ).addAnnotation(
                        AnnotationSpec.builder(Tag.class)
                                .addMember(NAME, STRING, getTag())
                                .build()
                )
                .addAnnotation(Validated.class);
    }

    protected void generateEndpoints() {
        for (Endpoint endpoint : endpoints) {
            generateEndpoint(requestMapping, endpoint);
        }
    }

    private void generateEndpoint(Mapping mapping, Endpoint endpoint) {
        EndpointBuilder endpointBuilder = endpointBuilderFactory.create(mapping, endpoint, ctx, cfg);
        endpointBuilder.apply(builder);
    }
}
