package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;

@Slf4j
public class DeleteMoreLevelsEndpointBuilder<C extends ApigenContext> extends ApigenAbstractEndpointBuilder<C> {

    public DeleteMoreLevelsEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
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
        TypeName filterType = ClassName.get(Filter.class);
        builder.addStatement("$T filter = getParentFilter($L, $L, \"$L\")", filterType, pathParams.get(0), null, endpoint.getChildParentRelationProperty());
        builder.addStatement("List<String> expand = getParentExpand(null, \"$L\")", endpoint.getParentEntity());
        builder.addStatement("$L.search(Long.valueOf($L), null, null, $L, $L)", SERVICE_NAME, pathParams.get(1), "expand", "filter");
        builder.addStatement("$L.delete(Long.valueOf($L))", SERVICE_NAME, pathParams.get(1));
    }

}
