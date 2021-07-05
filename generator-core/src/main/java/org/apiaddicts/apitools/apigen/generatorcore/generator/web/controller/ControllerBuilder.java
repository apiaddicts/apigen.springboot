package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.EndpointBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.EndpointBuilderFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.Modifier;
import java.util.List;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.TAGS;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

public abstract class ControllerBuilder extends AbstractClassBuilder {

    protected String basePackage;
    protected Mapping requestMapping;
    protected EntitiesData entitiesData;
    protected List<Endpoint> endpoints;

    public ControllerBuilder(Controller controller, EntitiesData entitiesData, String basePackage) {
        this.basePackage = basePackage;
        this.entitiesData = entitiesData;
        this.requestMapping = new Mapping(controller.getMapping());
        this.endpoints = controller.getEndpoints();
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
                        AnnotationSpec.builder(Api.class)
                                .addMember(TAGS, STRING, getTag())
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
        EndpointBuilder endpointBuilder = EndpointBuilderFactory.create(mapping, endpoint, entitiesData, basePackage);
        endpointBuilder.apply(builder);
    }
}
