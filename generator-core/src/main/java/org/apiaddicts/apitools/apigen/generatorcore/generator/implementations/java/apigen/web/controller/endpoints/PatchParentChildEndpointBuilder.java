package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output.ApigenEntityOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.EntitySimpleResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntityBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

public class PatchParentChildEndpointBuilder<C extends ApigenContext> extends PatchEndpointBuilder<C> {

    public PatchParentChildEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
    }

    @Override
    protected void addStatements() {
        TypeName filterType = ClassName.get(Filter.class);
        TypeName entityType = EntityBuilder.getTypeName(entityName, cfg.getBasePackage());
        TypeName resourceType = ApigenEntityOutputResourceBuilder.getTypeName(entityName, cfg.getBasePackage());
        TypeName responseType = EntitySimpleResponseBuilder.getTypeName(entityName, cfg.getBasePackage());
        builder.addStatement("$T filter = getParentFilter($L, $L, $S)", filterType, pathParams.get(0), null, endpoint.getChildParentRelationProperty());
        builder.addStatement("List<String> expand = getParentExpand(null, $S)", endpoint.getChildParentRelationProperty().split("\\.")[0]);
        builder.addStatement("$L.search($L, null, null, $L, $L)", SERVICE_NAME, pathParams.get(1), "expand", "filter");
        builder.addStatement("$T original = $L.safeGetOne($L)", entityType, SERVICE_NAME, pathParams.get(0));
        builder.addStatement("$L.partialUpdate(body, original)", MAPPER_NAME);
        builder.addStatement("$L.update($L, original)", SERVICE_NAME, pathParams.get(0));
        builder.addStatement("$T createResult = $L.search($L, null, null, null)", entityType, SERVICE_NAME, pathParams.get(0));
        builder.addStatement("$T result = $L.toResource(createResult)", resourceType, MAPPER_NAME);
        builder.addStatement("return new $T(result)", responseType);
    }
}
