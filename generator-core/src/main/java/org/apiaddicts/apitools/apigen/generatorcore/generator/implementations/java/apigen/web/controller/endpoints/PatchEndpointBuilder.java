package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output.ApigenEntityOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.EntitySimpleResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntityBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input.GenericInputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Set;

public class PatchEndpointBuilder<C extends ApigenContext> extends ApigenAbstractEndpointBuilder<C> {

    public PatchEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected Class getMappingClass() {
        return PatchMapping.class;
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
        builder.addStatement("$T original = $L.safeGetOne($L)", entityType, SERVICE_NAME, firstPathParam);
        builder.addStatement("$L.partialUpdate(body, original)", MAPPER_NAME);
        builder.addStatement("$L.update($L, original)", SERVICE_NAME, firstPathParam);
        builder.addStatement("$T createResult = $L.search($L, null, null, null)", entityType, SERVICE_NAME, firstPathParam);
        builder.addStatement("$T result = $L.toResource(createResult)", resourceType, MAPPER_NAME);
        builder.addStatement("return new $T(result)", responseType);
    }
}
