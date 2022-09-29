package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenSearchResult;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output.ApigenEntityOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.EntityListResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.EntitySimpleResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntityBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetAllMoreLevelsEndpointBuilder<C extends ApigenContext> extends ApigenAbstractEndpointBuilder<C> {

    public GetAllMoreLevelsEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
    }

    @Override
    protected Class getMappingClass() {
        return GetMapping.class;
    }

    @Override
    protected HttpStatus getResponseStatus() {
        HttpStatus status = HttpStatus.OK;
        if(null != this.endpoint.getResponse().getDefaultStatusCode() && this.endpoint.getResponse().getDefaultStatusCode() == 206){
            return HttpStatus.PARTIAL_CONTENT;
        }
        return status;
    }

    @Override
    protected TypeName getReturnTypeName() {
        return EntityListResponseBuilder.getTypeName(endpoint.getResponse().getRelatedEntity(), cfg.getBasePackage());
    }

    @Override
    protected void addStatements() {
        TypeName filterType = ClassName.get(Filter.class);
        TypeName entityType = EntityBuilder.getTypeName(entityName, cfg.getBasePackage());
        TypeName searchResultType = ParameterizedTypeName.get(ClassName.get(ApigenSearchResult.class), entityType);
        TypeName resourceType = ApigenEntityOutputResourceBuilder.getTypeName(entityName, cfg.getBasePackage());
        TypeName listResourceType = ParameterizedTypeName.get(ClassName.get(List.class), resourceType);
        String translatorParams = pathParamsToString(Arrays.asList("select", "exclude", "expand", "orderby"));
        String params = pathParamsToString(Arrays.asList("select", "exclude", "expand", "orderby", "init", "limit", "total"));
        String pageParams = pathParamsToString(Arrays.asList("init", "limit"));
        if(null != this.endpoint.getResponse().getDefaultStatusCode() && this.endpoint.getResponse().getDefaultStatusCode() == 200){
            params = pathParamsToString(Arrays.asList("select", "exclude", "expand", "orderby", "null", "null", "null"));
        }
        builder.addStatement("$L.translate($L, $T.class)", NAMING_TRANSLATOR_NAME, translatorParams, resourceType);
        builder.addStatement("$T filter = getParentFilter($L, $L, \"$L\")", filterType, pathParams.get(0), null, endpoint.getChildParentRelationProperty());
        builder.addStatement("expand = getparentExpand(expand, \"$L\")", endpoint.getParentEntity());
        builder.addStatement("$T searchResult = $L.search($L, $L)", searchResultType, SERVICE_NAME, params, "filter");
        builder.addStatement("$T result = $L.toResource(searchResult.getSearchResult())", listResourceType, MAPPER_NAME);
        TypeName responseTypeMoreLevel = EntityListResponseBuilder.getTypeName(endpoint.getResponse().getRelatedEntity(), cfg.getBasePackage());

        if(null != this.endpoint.getResponse().getDefaultStatusCode() && this.endpoint.getResponse().getDefaultStatusCode() == 200){
            builder.addStatement("return new $T(result)", responseTypeMoreLevel);
        }
        else{
            builder.addStatement("return new $T(result).withMetadataPagination($L, searchResult.getTotal())", responseTypeMoreLevel, pageParams);
        }
    }

    private String pathParamsToString(List<String> names) {
        Set<String> params = builder.parameters.stream().map(p -> p.name).collect(Collectors.toSet());
        return names.stream().map(n -> params.contains(n) ? n : "null").collect(Collectors.joining(", "));
    }
}
