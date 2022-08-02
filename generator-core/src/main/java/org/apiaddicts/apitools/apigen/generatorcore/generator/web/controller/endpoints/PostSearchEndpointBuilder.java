package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenSearchResult;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.archetypecore.core.resource.FilterResource;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntityBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output.EntityOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.response.EntityListResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class PostSearchEndpointBuilder extends EndpointBuilder {

    public PostSearchEndpointBuilder(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        super(rootMapping, endpoint, basePackage);
    }

    @Override
    protected Class getMappingClass() {
        return PostMapping.class;
    }

    @Override
    protected String getMapping() {
        return mapping.getValue();
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.PARTIAL_CONTENT;
    }

    @Override
    protected TypeName getReturnTypeName() {
        return EntityListResponseBuilder.getTypeName(entityName, basePackage);
    }

    @Override
    protected TypeName getBodyTypeName() {
        return ClassName.get(FilterResource.class);
    }

    @Override
    protected void addStatements() {
        TypeName filterType = ClassName.get(Filter.class);
        TypeName entityType = EntityBuilder.getTypeName(entityName, basePackage);
        TypeName searchResultType = ParameterizedTypeName.get(ClassName.get(ApigenSearchResult.class), entityType);
        TypeName resourceType = EntityOutputResourceBuilder.getTypeName(entityName, basePackage);
        TypeName listResourceType = ParameterizedTypeName.get(ClassName.get(List.class), resourceType);
        TypeName responseType = EntityListResponseBuilder.getTypeName(entityName, basePackage);
        String translatorParams = pathParamsAndFilterToString(Arrays.asList("select", "exclude", "expand", "filter", "orderby"));
        String params = pathParamsAndFilterToString(Arrays.asList("select", "exclude", "expand", "filter", "orderby", "init", "limit", "total"));
        String pageParams = pathParamsAndFilterToString(Arrays.asList("init", "limit"));
        builder.addStatement("$T filter = body.getFilter()", filterType);
        builder.addStatement("$L.translate($L, $T.class)", NAMING_TRANSLATOR_NAME, translatorParams, resourceType);
        builder.addStatement("$T searchResult = $L.search($L)", searchResultType, SERVICE_NAME, params);
        builder.addStatement("$T result = $L.toResource(searchResult.getSearchResult())", listResourceType, MAPPER_NAME);
        builder.addStatement("return new $T(result).withMetadataPagination($L, searchResult.getTotal())", responseType, pageParams);
    }

    private String pathParamsAndFilterToString(List<String> names) {
        return names.stream().map(n -> queryParams.contains(n) || n.equals("filter") ? n : "null").collect(Collectors.joining(", "));
    }
}
