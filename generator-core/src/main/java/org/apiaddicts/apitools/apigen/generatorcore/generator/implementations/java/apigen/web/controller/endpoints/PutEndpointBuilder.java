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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.Set;

public class PutEndpointBuilder<C extends ApigenContext> extends ApigenAbstractEndpointBuilder<C> {

    public PutEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
    }

    @Override
    protected void initialize() {
        super.initialize();
        addUpdatedFieldsParam();
    }

    @Override
    protected Class getMappingClass() {
        return PutMapping.class;
    }

    @Override
    protected TypeName getReturnTypeName() {
        return EntitySimpleResponseBuilder.getTypeName(entityName, cfg.getBasePackage());
    }

    @Override
    protected TypeName getBodyTypeName() {
        return GenericInputResourceBuilder.getTypeName(endpoint, cfg.getBasePackage());
    }

    private void addUpdatedFieldsParam() {
        TypeName updatedFieldsType = ParameterizedTypeName.get(ClassName.get(Set.class), ClassName.get(String.class));
        builder.addParameter(ParameterSpec.builder(updatedFieldsType, "updatedFields").addAnnotation(RequestAttribute.class).build());
    }

    @Override
    protected void addStatements() {
        TypeName entityType = EntityBuilder.getTypeName(entityName, cfg.getBasePackage());
        TypeName resourceType = ApigenEntityOutputResourceBuilder.getTypeName(entityName, cfg.getBasePackage());
        TypeName responseType = EntitySimpleResponseBuilder.getTypeName(entityName, cfg.getBasePackage());
        builder.addStatement("$T updateRequest = $L.toEntity(body)", entityType, MAPPER_NAME);
        builder.addStatement("$L.update($L, updateRequest, updatedFields)", SERVICE_NAME, pathParams.get(0));
        builder.addStatement("$T createResult = $L.search($L, null, null, null)", entityType, SERVICE_NAME, pathParams.get(0));
        builder.addStatement("$T result = $L.toResource(createResult)", resourceType, MAPPER_NAME);
        builder.addStatement("return new $T(result)", responseType);
    }
}
