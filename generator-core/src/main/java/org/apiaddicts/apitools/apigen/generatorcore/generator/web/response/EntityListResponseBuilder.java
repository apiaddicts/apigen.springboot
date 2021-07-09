package org.apiaddicts.apitools.apigen.generatorcore.generator.web.response;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output.EntityOutputResourceBuilder;

public class EntityListResponseBuilder extends ListResponseBuilder {

    private String entityName;

    public EntityListResponseBuilder(String entityName, String jsonListName, String basePackage) {
        super(jsonListName, EntityOutputResourceBuilder.getTypeName(entityName, basePackage), basePackage);
        this.entityName = entityName;
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
