package org.apiaddicts.apitools.apigen.generatorcore.generator.web.response;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output.ResourceOutputResourceBuilder;

public class ResourceListResponseBuilder extends ListResponseBuilder {

    private Mapping rootMapping;
    private Endpoint endpoint;

    public ResourceListResponseBuilder(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        super(
                endpoint.getResponse().getCollectionName(),
                ResourceOutputResourceBuilder.getTypeName(rootMapping, endpoint, basePackage),
                basePackage
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
