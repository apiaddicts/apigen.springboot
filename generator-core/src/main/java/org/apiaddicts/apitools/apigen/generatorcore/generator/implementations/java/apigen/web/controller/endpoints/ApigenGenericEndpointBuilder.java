package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.NotImplementedException;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.EntityListResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.EntitySimpleResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints.GenericEndpointBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input.GenericInputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.GenericOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

import java.util.List;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Constants.JSON_MIME_TYPE;

public class ApigenGenericEndpointBuilder<C extends ApigenContext> extends GenericEndpointBuilder<C> {
    public ApigenGenericEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
    }

    @Override
    protected void addStatements() {
        builder.addComment("TODO: Implement this non standard endpoint");
        builder.addStatement("throw new $T($S)", NotImplementedException.class, endpoint.getMethod() + " " + this.getMapping());
    }

    @Override
    protected TypeName getReturnTypeName() {
        Response response = endpoint.getResponse();
        if (response == null || response.getAttributes() == null) return null;
        TypeName responseType;
        if (!JSON_MIME_TYPE.equals(response.getMimeType())) {
            responseType = TypeName.OBJECT;
        } else if (response.getRelatedEntity() != null) {
            if (response.getIsCollection()) {
                responseType = EntityListResponseBuilder.getTypeName(response.getRelatedEntity(), cfg.getBasePackage());
            } else {
                responseType = EntitySimpleResponseBuilder.getTypeName(response.getRelatedEntity(), cfg.getBasePackage());
            }
        } else {
            responseType = GenericOutputResourceBuilder.getTypeName(rootMapping, endpoint, cfg.getBasePackage());
            if (response.getIsCollection()) {
                responseType = ParameterizedTypeName.get(ClassName.get(List.class), responseType);
            }
        }
        return responseType;
    }

    @Override
    protected TypeName getBodyTypeName() {
        if (endpoint.getRequest() == null) return null;
        if (!JSON_MIME_TYPE.equals(endpoint.getRequest().getMimeType())) {
            return TypeName.OBJECT;
        }
        return GenericInputResourceBuilder.getTypeName(rootMapping, endpoint, cfg.getBasePackage());
    }
}
