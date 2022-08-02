package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.GenericOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

public class ResourceListResponseBuilder<C extends ApigenContext> extends ListResponseBuilder<C> {

    protected final Mapping rootMapping;
    protected final Endpoint endpoint;

    public ResourceListResponseBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(
                endpoint.getResponse().getCollectionName(),
                GenericOutputResourceBuilder.getTypeName(rootMapping, endpoint, cfg.getBasePackage()),
                ctx,
                cfg
        );
        this.rootMapping = rootMapping;
        this.endpoint = endpoint;
    }

    public static TypeName getTypeName(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        return ClassName.get(getPackage(rootMapping, basePackage), getName(rootMapping, endpoint));
    }

    private static String getName(Mapping rootMapping, Endpoint endpoint) {
        Mapping mapping = new Mapping(endpoint.getMapping());
        return endpoint.getMethod().prefix + rootMapping.toName() + mapping.toName() + "ListResponse";
    }

    private static String getPackage(Mapping rootMapping, String basePackage) {
        return concatPackage(basePackage, rootMapping.toName().toLowerCase(), "web");
    }

    @Override
    public String getPackage() {
        return getPackage(rootMapping, basePackage);
    }

    @Override
    protected String getName() {
        return getName(rootMapping, endpoint);
    }

    @Override
    protected String getContentName() {
        Mapping mapping = new Mapping(endpoint.getMapping());
        return rootMapping.toName() + mapping.toName() + "ListResponseContent";
    }
}
