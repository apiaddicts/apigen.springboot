package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;

@Slf4j
public class DeleteEndpointBuilder<C extends ApigenContext> extends ApigenAbstractEndpointBuilder<C> {

    public DeleteEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
    }

    @Override
    protected Class getMappingClass() {
        return DeleteMapping.class;
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.NO_CONTENT;
    }

    @Override
    protected void addStatements() {
        builder.addStatement("$L.delete($L)", SERVICE_NAME, firstPathParam);
    }

}
