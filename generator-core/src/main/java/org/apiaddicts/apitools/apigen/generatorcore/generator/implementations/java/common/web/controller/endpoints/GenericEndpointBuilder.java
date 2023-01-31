package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input.GenericInputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.GenericOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class GenericEndpointBuilder<C extends JavaContext> extends EndpointBuilder<C> {

    public GenericEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
    }

    @Override
    protected Class getMappingClass() {
        switch (endpoint.getMethod()) {
            case DELETE:
                return DeleteMapping.class;
            case PATCH:
                return PatchMapping.class;
            case PUT:
                return PutMapping.class;
            case POST:
                return PostMapping.class;
            case GET:
                return GetMapping.class;
            default:
                return null;
        }
    }

    @Override
    protected String getMapping() {
        if (mapping.isEmpty()) return null;
        return mapping.getValue();
    }

    @Override
    protected HttpStatus getResponseStatus() {
        HttpStatus status;
        if (endpoint.getResponse() == null) status = HttpStatus.NO_CONTENT;
        else status = HttpStatus.resolve(endpoint.getResponse().getDefaultStatusCode());
        if (status == null) status = HttpStatus.OK;
        return status;
    }

    @Override
    protected TypeName getReturnTypeName() {
        Response response = endpoint.getResponse();
        if (response == null || response.getAttributes() == null) return null;
        TypeName responseType = GenericOutputResourceBuilder.getTypeName(rootMapping, endpoint, cfg.getBasePackage());
        if (response.getIsCollection()) {
            responseType = ParameterizedTypeName.get(ClassName.get(List.class), responseType);
        }
        return responseType;
    }

    @Override
    protected TypeName getBodyTypeName() {
        if (endpoint.getRequest() == null) return null;
        return GenericInputResourceBuilder.getTypeName(rootMapping, endpoint, cfg.getBasePackage());
    }

    @Override
    protected void addStatements() {
        builder.addComment("TODO: Implement this endpoint");
    }
}
