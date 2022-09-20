package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output.ApigenEntityOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.EntitySimpleResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntityBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetByIdMoreLevelsEndpointBuilder<C extends ApigenContext> extends ApigenAbstractEndpointBuilder<C> {


    public GetByIdMoreLevelsEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
    }

    @Override
    protected Class getMappingClass() {
        return GetMapping.class;
    }

    @Override
    protected TypeName getReturnTypeName() {
        return EntitySimpleResponseBuilder.getTypeName(endpoint.getResponse().getRelatedEntity(), cfg.getBasePackage());
    }

    @Override
    protected void addStatements() {
        TypeName entityType = EntityBuilder.getTypeName(entityName, cfg.getBasePackage());
        TypeName resourceType = ApigenEntityOutputResourceBuilder.getTypeName(entityName, cfg.getBasePackage());
        String translatorParams = pathParamsToString(Arrays.asList("select", "exclude", "expand"));
        String params = pathParamsToString(Arrays.asList("select", "exclude", "expand"));
        builder.addStatement("$L.translate($L, $T.class)", NAMING_TRANSLATOR_NAME, translatorParams, resourceType);
        builder.addStatement("$T searchResult = $L.search($L, $L)", entityType, SERVICE_NAME, pathParams.get(0), params);
        builder.addStatement("$T searchResultMapped = $L.toResource(searchResult)", resourceType, MAPPER_NAME);
        TypeName responseTypeMoreLevel = EntitySimpleResponseBuilder.getTypeName(endpoint.getResponse().getRelatedEntity(), cfg.getBasePackage());
        for(int i = 0; i < entityNameMoreLevels.size(); i++){
            TypeName resourceTypeMoreLevel = ApigenEntityOutputResourceBuilder.getTypeName(entityNameMoreLevels.get(i), cfg.getBasePackage());
            String searchChild = "get" + entityNameMoreLevels.get(i) + "s";
            String previousVariableName = i == 0 ? "searchResultMapped" : "searchResultMapped" + i;
            String variableName = i == entityNameMoreLevels.size() - 1 ?  "result" : previousVariableName + (i + 1);
            builder.addStatement("Optional<$T> $L = $L.$L().stream().filter(x -> $L.equals(x.getId())).findFirst()",
                    resourceTypeMoreLevel, variableName, previousVariableName, searchChild, pathParams.get(i + 1));
        }
        builder.addStatement("return new $T(result.get())", responseTypeMoreLevel);
    }

    private String pathParamsToString(List<String> names) {
        Set<String> params = builder.parameters.stream().map(p -> p.name).collect(Collectors.toSet());
        return names.stream().map(n -> params.contains(n) ? n : "null").collect(Collectors.joining(", "));
    }
}