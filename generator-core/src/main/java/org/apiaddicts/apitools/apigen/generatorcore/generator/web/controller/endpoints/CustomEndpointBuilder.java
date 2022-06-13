package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.NotImplementedException;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.input.AllInputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output.ResourceOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.response.EntityListResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.response.ResourceListResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.response.ResourceSimpleResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.response.SimpleResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

public class CustomEndpointBuilder extends EndpointBuilder {

    public CustomEndpointBuilder(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        super(rootMapping, endpoint, basePackage);
    }

    @Override
    protected Class getMappingClass() {
        switch (endpoint.getMethod()) {
            case DELETE:
                return DeleteMapping.class;
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

        if (response.getIsStandard()) {
            return getStandardTypeName(response);
        } else {
            return getNonStandardTypeName(response);
        }
    }

    private TypeName getStandardTypeName(Response response) {
        String entityName = response.getRelatedEntity();
        boolean isCollection = response.getIsCollection();
        if (entityName != null) {
            if (isCollection) {
                return EntityListResponseBuilder.getTypeName(entityName, basePackage);
            } else {
                return SimpleResponseBuilder.getTypeName(entityName, basePackage);
            }
        } else {
            if (isCollection) {
                return ResourceListResponseBuilder.getTypeName(rootMapping, endpoint, basePackage);
            } else {
                return ResourceSimpleResponseBuilder.getTypeName(rootMapping, endpoint, basePackage);
            }
        }
    }

    private TypeName getNonStandardTypeName(Response response) {
        TypeName responseType = ResourceOutputResourceBuilder.getTypeName(rootMapping, endpoint, basePackage);
        if (response.getIsCollection()) {
            responseType = ParameterizedTypeName.get(ClassName.get(List.class), responseType);
        }
        return responseType;
    }

    @Override
    protected TypeName getBodyTypeName() {
        if (endpoint.getRequest() == null) return null;
        return AllInputResourceBuilder.getTypeName(rootMapping, endpoint, basePackage);
    }

    @Override
    protected void addStatements() {
        builder.addComment("TODO: Implement this non standard endpoint");
        builder.addStatement("throw new $T($S)", NotImplementedException.class, endpoint.getMethod() + " " + this.getMapping());
    }
}
