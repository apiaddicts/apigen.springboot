package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.Data;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output.ApigenEntityOutputResourceBuilder;

public class EntitySimpleResponseBuilder<C extends ApigenContext> extends ResponseBuilder<C> {

    protected final String basePackage;
    protected final String entityName;

    public EntitySimpleResponseBuilder(Response response, C ctx, Configuration cfg) {
        super(ctx, cfg);
        this.basePackage = cfg.getBasePackage();
        this.entityName = response.getRelatedEntity();
    }

    public static TypeName getTypeName(String entityName, String basePackage) {
        return ClassName.get(getPackage(entityName, basePackage), getName(entityName));
    }

    private static String getName(String entityName) {
        return entityName + "Response";
    }

    private static String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName, "web");
    }

    @Override
    public String getPackage() {
        return getPackage(entityName, basePackage);
    }

    @Override
    protected void initialize() {
        generateBuilder();
        addConstructor();
    }

    private void generateBuilder() {
        TypeName resourceType = ApigenEntityOutputResourceBuilder.getTypeName(entityName, basePackage);
        this.builder = getClass(getName(entityName))
                .superclass(getResponseParentClass(resourceType))
                .addAnnotation(Data.class);
    }

    private void addConstructor() {
        TypeName resourceType = ApigenEntityOutputResourceBuilder.getTypeName(entityName, basePackage);
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addParameter(resourceType, "data")
                .addStatement("super(data)")
                .build();
        builder.addMethod(constructor);
    }

    private TypeName getResponseParentClass(TypeName resourceType) {
        return ParameterizedTypeName.get(
                ClassName.get(ApiResponse.class),
                resourceType
        );
    }

}
