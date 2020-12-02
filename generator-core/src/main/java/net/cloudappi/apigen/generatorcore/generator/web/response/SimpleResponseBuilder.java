package net.cloudappi.apigen.generatorcore.generator.web.response;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.Data;
import net.cloudappi.apigen.archetypecore.core.responses.ApiResponse;
import net.cloudappi.apigen.generatorcore.generator.web.resource.output.EntityOutputResourceBuilder;

public class SimpleResponseBuilder extends ResponseBuilder {

    private String basePackage;
    private String entityName;

    public SimpleResponseBuilder(String entityName, String basePackage) {
        this.basePackage = basePackage;
        this.entityName = entityName;
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
        TypeName resourceType = EntityOutputResourceBuilder.getTypeName(entityName, basePackage);
        this.builder = getClass(getName(entityName))
                .superclass(getResponseParentClass(resourceType))
                .addAnnotation(Data.class);
    }

    private void addConstructor() {
        TypeName resourceType = EntityOutputResourceBuilder.getTypeName(entityName, basePackage);
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
