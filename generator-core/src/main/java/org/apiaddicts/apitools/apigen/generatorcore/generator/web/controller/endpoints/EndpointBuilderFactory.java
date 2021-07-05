package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

public class EndpointBuilderFactory {

    private EndpointBuilderFactory() {
        // Intentionally blank
    }

    public static EndpointBuilder create(Mapping rootMapping, Endpoint endpoint, EntitiesData entitiesData, String basePackage) {
        Mapping mapping = new Mapping(endpoint.getMapping());
        Endpoint.Method method = endpoint.getMethod();
        TypeName composedIdType = null;

        boolean isStandardWithEntity = (endpoint.getResponse() == null || endpoint.getResponse().getIsStandard()) && endpoint.getRelatedEntity() != null;
        if (isStandardWithEntity) {
            composedIdType = entitiesData.getComposedIDType(endpoint.getRelatedEntity());
        }

        boolean isGetAll = isStandardWithEntity && method == Endpoint.Method.GET && mapping.isEmpty();
        if (isGetAll) return new GetAllEndpointBuilder(rootMapping, endpoint, basePackage);

        boolean isGetOne = isStandardWithEntity && method == Endpoint.Method.GET && mapping.isById();
        if (isGetOne) return new GetByIdEndpointBuilder(rootMapping, endpoint, composedIdType, basePackage);

        boolean isPost = isStandardWithEntity && method == Endpoint.Method.POST && mapping.isEmpty();
        if (isPost) return new PostEndpointBuilder(rootMapping, endpoint, basePackage);

        boolean isSearch = isStandardWithEntity && method == Endpoint.Method.POST && mapping.isSearch();
        if (isSearch) return new PostSearchEndpointBuilder(rootMapping, endpoint, basePackage);

        boolean isPut = isStandardWithEntity && method == Endpoint.Method.PUT && mapping.isById();
        if (isPut) return new PutEndpointBuilder(rootMapping, endpoint, composedIdType, basePackage);

        boolean isDelete = isStandardWithEntity && method == Endpoint.Method.DELETE && mapping.isById();
        if (isDelete) return new DeleteEndpointBuilder(rootMapping, endpoint, composedIdType, basePackage);

        return new CustomEndpointBuilder(rootMapping, endpoint, basePackage);
    }
}
