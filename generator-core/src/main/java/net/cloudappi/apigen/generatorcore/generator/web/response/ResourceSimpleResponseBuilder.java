package net.cloudappi.apigen.generatorcore.generator.web.response;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.Data;
import net.cloudappi.apigen.archetypecore.core.responses.ApiResponse;
import net.cloudappi.apigen.generatorcore.config.controller.Endpoint;
import net.cloudappi.apigen.generatorcore.utils.Mapping;
import net.cloudappi.apigen.generatorcore.generator.web.resource.output.ResourceOutputResourceBuilder;

public class ResourceSimpleResponseBuilder extends ResponseBuilder {

    private Mapping rootMapping;
    private Endpoint endpoint;
    private String basePackage;

    public ResourceSimpleResponseBuilder(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        this.rootMapping = rootMapping;
        this.endpoint = endpoint;
        this.basePackage = basePackage;
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
        TypeName resourceType = ResourceOutputResourceBuilder.getTypeName(rootMapping, endpoint, basePackage);
        this.builder = getClass(getName(rootMapping, endpoint))
                .superclass(getResponseParentClass(resourceType))
                .addAnnotation(Data.class);
    }

    private void addConstructor() {
        TypeName resourceType = ResourceOutputResourceBuilder.getTypeName(rootMapping, endpoint, basePackage);
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
