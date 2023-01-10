package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output.ApigenEntityOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.EntitySimpleResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntityBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input.GenericInputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

public class PostEndpointBuilder<C extends ApigenContext> extends ApigenAbstractEndpointBuilder<C> {

    public PostEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
    }

    @Override
    protected Class getMappingClass() {
        return PostMapping.class;
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.CREATED;
    }

    @Override
    protected TypeName getReturnTypeName() {
        return EntitySimpleResponseBuilder.getTypeName(entityName, cfg.getBasePackage());
    }

    @Override
    protected void addRequestBody() {
        TypeName bodyType = GenericInputResourceBuilder.getTypeName(endpoint, cfg.getBasePackage());
        builder.addParameter(ParameterSpec.builder(bodyType, "body").addAnnotation(RequestBody.class).addAnnotation(Valid.class).build());
    }

    @Override
    protected void addStatements() {
        TypeName entityType = EntityBuilder.getTypeName(entityName, cfg.getBasePackage());
        TypeName resourceType = ApigenEntityOutputResourceBuilder.getTypeName(entityName, cfg.getBasePackage());
        TypeName responseType = EntitySimpleResponseBuilder.getTypeName(entityName, cfg.getBasePackage());
        builder.addStatement("$T createRequest = $L.toEntity(body)", entityType, MAPPER_NAME);
        builder.addStatement("$L.create(createRequest)", SERVICE_NAME);
        builder.addStatement("$T createResult = $L.search(createRequest.getId(), null, null, null)", entityType, SERVICE_NAME);
        builder.addStatement("$T result = $L.toResource(createResult)", resourceType, MAPPER_NAME);
        builder.addStatement("return new $T(result)", responseType);
    }
}
