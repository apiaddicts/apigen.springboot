package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output.ApigenEntityOutputResourceBuilder;

public class EntityListResponseBuilder<C extends ApigenContext> extends ListResponseBuilder<C> {

    protected final String entityName;

    public EntityListResponseBuilder(Response response, C ctx, Configuration cfg) {
        super(response.getCollectionName(), ApigenEntityOutputResourceBuilder.getTypeName(response.getRelatedEntity(), cfg.getBasePackage()), ctx, cfg);
        this.entityName = response.getRelatedEntity();
    }

    public static TypeName getTypeName(String entityName, String basePackage) {
        return ClassName.get(getPackage(entityName, basePackage), getName(entityName));
    }

    private static String getName(String entityName) {
        return entityName + "ListResponse";
    }

    private static String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName, "web");
    }

    @Override
    public String getPackage() {
        return getPackage(entityName, basePackage);
    }

    @Override
    protected String getName() {
        return getName(entityName);
    }

    @Override
    protected String getContentName() {
        return entityName + "ListResponseContent";
    }

}
