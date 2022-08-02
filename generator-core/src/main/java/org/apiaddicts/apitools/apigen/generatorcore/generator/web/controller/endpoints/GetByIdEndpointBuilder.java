package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntityBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output.EntityOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.response.SimpleResponseBuilder;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class GetByIdEndpointBuilder extends EndpointBuilder {

    private TypeName composedIdType;

    public GetByIdEndpointBuilder(Mapping rootMapping, Endpoint endpoint, TypeName composedIdType, String basePackage) {
        super(rootMapping, endpoint, basePackage);
        this.composedIdType = composedIdType;
    }

    @Override
    protected Class getMappingClass() {
        return GetMapping.class;
    }

    @Override
    protected String getMapping() {
        return mapping.getValue();
    }

    @Override
    protected TypeName getReturnTypeName() {
        return SimpleResponseBuilder.getTypeName(entityName, basePackage);
    }

    @Override
    protected void addStatements() {
        TypeName entityType = EntityBuilder.getTypeName(entityName, basePackage);
        TypeName resourceType = EntityOutputResourceBuilder.getTypeName(entityName, basePackage);
        TypeName responseType = SimpleResponseBuilder.getTypeName(entityName, basePackage);
        String translatorParams = pathParamsToString(Arrays.asList("select", "exclude", "expand"));
        String params = pathParamsToString(Arrays.asList("select", "exclude", "expand"));
        builder.addStatement("$L.translate($L, $T.class)", NAMING_TRANSLATOR_NAME, translatorParams, resourceType);
        builder.addStatement("$T searchResult = $L.search($L, $L)", entityType, SERVICE_NAME, identifierParam, params);
        builder.addStatement("$T result = $L.toResource(searchResult)", resourceType, MAPPER_NAME);
        builder.addStatement("return new $T(result)", responseType);
    }

    private String pathParamsToString(List<String> names) {
        return names.stream().map(n -> queryParams.contains(n) ? n : "null").collect(Collectors.joining(", "));
    }

    @Override
    protected TypeName getIdentifierPathParamType() {
        return composedIdType;
    }
}
