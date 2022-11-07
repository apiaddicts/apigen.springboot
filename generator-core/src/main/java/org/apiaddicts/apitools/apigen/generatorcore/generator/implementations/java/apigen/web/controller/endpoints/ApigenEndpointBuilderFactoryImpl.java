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

        boolean isParentChild = endpoint.getParentEntity() != null;
        boolean isStandardWithEntity = (endpoint.getResponse() == null || endpoint.getResponse().getIsStandard()) && endpoint.getRelatedEntity() != null;

        boolean isGetAll = isStandardWithEntity && method == Endpoint.Method.GET && mapping.isEmpty();
        boolean isGetAllParentChild = isGetAll && isParentChild;
        if (isGetAllParentChild) return new GetAllParentChildEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);
        if (isGetAll) return new GetAllEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isGetOne = isStandardWithEntity && method == Endpoint.Method.GET && mapping.isById();
        boolean isGetOneParentChild = isGetOne && isParentChild;
        if (isGetOneParentChild) return new GetByIdParentChildEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);
        if (isGetOne) return new GetByIdEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isPost = isStandardWithEntity && method == Endpoint.Method.POST && mapping.isEmpty();
        boolean isPostParentChild = isPost && isParentChild;
        if (isPostParentChild) return new PostParentChildEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);
        if (isPost) return new PostEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isSearch = isStandardWithEntity && method == Endpoint.Method.POST && mapping.isSearch();
        boolean isSearchParentChild = isSearch && isParentChild;
        if (isSearchParentChild) return new PostSearchParentChildEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);
        if (isSearch) return new PostSearchEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isPut = isStandardWithEntity && method == Endpoint.Method.PUT && mapping.isById();
        boolean isPutParentChild = isPut && isParentChild;
        if (isPutParentChild) return new PutParentChildEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);
        if (isPut) return new PutEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isPatch = isStandardWithEntity && method == Endpoint.Method.PATCH && mapping.isById();
        boolean isPatchParentChild = isPatch && isParentChild;
        if (isPatchParentChild) return new PatchParentChildEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);
        if (isPatch) return new PatchEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        boolean isDelete = isStandardWithEntity && method == Endpoint.Method.DELETE && mapping.isById();
        boolean isDeleteParentChild = isDelete && isParentChild;
        if (isDeleteParentChild) return new DeleteParentChildEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);
        if (isDelete) return new DeleteEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);

        return new ApigenGenericEndpointBuilder<>(rootMapping, endpoint, ctx, cfg);
    }
}
