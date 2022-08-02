package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenSearchResult;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output.ApigenEntityOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.EntityListResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntityBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetAllEndpointBuilder<C extends ApigenContext> extends ApigenAbstractEndpointBuilder<C> {

    public GetAllEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
    }

    @Override
    protected Class getMappingClass() {
        return GetMapping.class;
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.PARTIAL_CONTENT;
    }

    @Override
    protected TypeName getReturnTypeName() {
        return EntityListResponseBuilder.getTypeName(entityName, cfg.getBasePackage());
    }

    @Override
    protected void addStatements() {
        TypeName entityType = EntityBuilder.getTypeName(entityName, cfg.getBasePackage());
        TypeName searchResultType = ParameterizedTypeName.get(ClassName.get(ApigenSearchResult.class), entityType);
        TypeName resourceType = ApigenEntityOutputResourceBuilder.getTypeName(entityName, cfg.getBasePackage());
        TypeName listResourceType = ParameterizedTypeName.get(ClassName.get(List.class), resourceType);
        TypeName responseType = EntityListResponseBuilder.getTypeName(entityName, cfg.getBasePackage());
        String translatorParams = pathParamsToString(Arrays.asList("select", "exclude", "expand", "orderby"));
        String params = pathParamsToString(Arrays.asList("select", "exclude", "expand", "filter", "orderby", "init", "limit", "total"));
        String pageParams = pathParamsToString(Arrays.asList("init", "limit"));
        builder.addStatement("$L.translate($L, $T.class)", NAMING_TRANSLATOR_NAME, translatorParams, resourceType);
        builder.addStatement("$T searchResult = $L.search($L)", searchResultType, SERVICE_NAME, params);
        builder.addStatement("$T result = $L.toResource(searchResult.getSearchResult())", listResourceType, MAPPER_NAME);
        builder.addStatement("return new $T(result).withMetadataPagination($L, searchResult.getTotal())", responseType, pageParams);
    }

    private String pathParamsToString(List<String> names) {
        Set<String> params = builder.parameters.stream().map(p -> p.name).collect(Collectors.toSet());
        return names.stream().map(n -> params.contains(n) ? n : "null").collect(Collectors.joining(", "));
    }
}
