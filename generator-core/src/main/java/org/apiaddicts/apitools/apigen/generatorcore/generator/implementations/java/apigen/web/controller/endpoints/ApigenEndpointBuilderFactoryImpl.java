package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints.EndpointBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints.EndpointBuilderFactory;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

public class ApigenEndpointBuilderFactoryImpl<C extends ApigenContext> implements EndpointBuilderFactory<C> {

    public EndpointBuilder<C> create(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        Mapping mapping = new Mapping(endpoint.getMapping());
        Endpoint.Method method = endpoint.getMethod();

        boolean isStandardWithEntity = (endpoint.getResponse() == null || endpoint.getResponse().getIsStandard()) && endpoint.getRelatedEntity() != null;

        boolean isGetAll = isStandardWithEntity && method == Endpoint.Method.GET && mapping.isEmpty();
        if (isGetAll) return new GetAllEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isGetAllMoreLevels = isStandardWithEntity && method == Endpoint.Method.GET && mapping.isGetAllMoreLevels();
        if (isGetAllMoreLevels) return new GetAllMoreLevelsEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isGetOne = isStandardWithEntity && method == Endpoint.Method.GET && mapping.isById();
        if (isGetOne) return new GetByIdEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isGetOneMoreLevels = isStandardWithEntity && method == Endpoint.Method.GET && mapping.isByIdMoreLevels();
        if (isGetOneMoreLevels) return new GetByIdMoreLevelsEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isPost = isStandardWithEntity && method == Endpoint.Method.POST && mapping.isEmpty();
        if (isPost) return new PostEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isSearch = isStandardWithEntity && method == Endpoint.Method.POST && mapping.isSearch();
        if (isSearch) return new PostSearchEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isPut = isStandardWithEntity && method == Endpoint.Method.PUT && mapping.isById();
        if (isPut) return new PutEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isDelete = isStandardWithEntity && method == Endpoint.Method.DELETE && mapping.isById();
        if (isDelete) return new DeleteEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        return new ApigenGenericEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);
    }
}
