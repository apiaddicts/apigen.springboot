package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.Data;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.GenericOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

public class ResourceSimpleResponseBuilder<C extends ApigenContext> extends ResponseBuilder<C> {

    protected final Mapping rootMapping;
    protected final Endpoint endpoint;
    protected final String basePackage;

    public ResourceSimpleResponseBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(ctx, cfg);
        this.rootMapping = rootMapping;
        this.endpoint = endpoint;
        this.basePackage = cfg.getBasePackage();
    }

    public static TypeName getTypeName(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        return ClassName.get(getPackage(rootMapping, basePackage), getName(rootMapping, endpoint));
    }

    private static String getName(Mapping rootMapping, Endpoint endpoint) {
        Mapping mapping = new Mapping(endpoint.getMapping());
        return endpoint.getMethod().prefix + rootMapping.toName() + mapping.toName() + "Response";
    }

    private static String getPackage(Mapping rootMapping, String basePackage) {
        return concatPackage(basePackage, rootMapping.toName().toLowerCase(), "web");
    }

    @Override
    public String getPackage() {
        return getPackage(rootMapping, basePackage);
    }

    @Override
    protected void initialize() {
        generateBuilder();
        addConstructor();
    }

    private void generateBuilder() {
        TypeName resourceType = GenericOutputResourceBuilder.getTypeName(rootMapping, endpoint, basePackage);
        this.builder = getClass(getName(rootMapping, endpoint))
                .superclass(getResponseParentClass(resourceType))
                .addAnnotation(Data.class);
    }

    private void addConstructor() {
        TypeName resourceType = GenericOutputResourceBuilder.getTypeName(rootMapping, endpoint, basePackage);
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
