package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

@Slf4j
public class DeleteParentChildEndpointBuilder<C extends ApigenContext> extends DeleteEndpointBuilder<C> {

    public DeleteParentChildEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
    }

    @Override
    protected void addStatements() {
        TypeName filterType = ClassName.get(Filter.class);
        builder.addStatement("$T filter = getParentFilter($L, $L, $S)", filterType, pathParams.get(0), null, endpoint.getChildParentRelationProperty());
        builder.addStatement("List<String> expand = getParentExpand(null, $S)", endpoint.getChildParentRelationProperty().split("\\.")[0]);
        builder.addStatement("$L.search($L, null, null, $L, $L)", SERVICE_NAME, pathParams.get(1), "expand", "filter");
        builder.addStatement("$L.delete($L)", SERVICE_NAME, pathParams.get(1));
    }

}
