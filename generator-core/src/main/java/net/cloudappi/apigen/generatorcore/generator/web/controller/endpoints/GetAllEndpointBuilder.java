package net.cloudappi.apigen.generatorcore.generator.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import net.cloudappi.apigen.archetypecore.core.persistence.ApigenSearchResult;
import net.cloudappi.apigen.generatorcore.config.controller.Endpoint;
import net.cloudappi.apigen.generatorcore.utils.Mapping;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntityBuilder;
import net.cloudappi.apigen.generatorcore.generator.web.resource.output.EntityOutputResourceBuilder;
import net.cloudappi.apigen.generatorcore.generator.web.response.EntityListResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GetAllEndpointBuilder extends EndpointBuilder {

    public GetAllEndpointBuilder(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        super(rootMapping, endpoint, basePackage);
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
        return EntityListResponseBuilder.getTypeName(entityName, basePackage);
    }

    @Override
    protected void addStatements() {
        TypeName entityType = EntityBuilder.getTypeName(entityName, basePackage);
        TypeName searchResultType = ParameterizedTypeName.get(ClassName.get(ApigenSearchResult.class), entityType);
        TypeName resourceType = EntityOutputResourceBuilder.getTypeName(entityName, basePackage);
        TypeName listResourceType = ParameterizedTypeName.get(ClassName.get(List.class), resourceType);
        TypeName responseType = EntityListResponseBuilder.getTypeName(entityName, basePackage);
        String translatorParams = pathParamsToString(Arrays.asList("select", "exclude", "expand", "orderby"));
        String params = pathParamsToString(Arrays.asList("select", "exclude", "expand", "filter", "orderby", "init", "limit", "total"));
        String pageParams = pathParamsToString(Arrays.asList("init", "limit"));
        builder.addStatement("$L.translate($L, $T.class)", NAMING_TRANSLATOR_NAME, translatorParams, resourceType);
        builder.addStatement("$T searchResult = $L.search($L)", searchResultType, SERVICE_NAME, params);
        builder.addStatement("$T result = $L.toResource(searchResult.getSearchResult())", listResourceType, MAPPER_NAME);
        builder.addStatement("return new $T(result).withMetadataPagination($L, searchResult.getTotal())", responseType, pageParams);
    }

    private String pathParamsToString(List<String> names) {
        return names.stream().map(n -> queryParams.contains(n) ? n : "null").collect(Collectors.joining(", "));
    }
}
