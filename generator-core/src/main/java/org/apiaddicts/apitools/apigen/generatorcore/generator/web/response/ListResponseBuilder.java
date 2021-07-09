package org.apiaddicts.apitools.apigen.generatorcore.generator.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.*;
import lombok.Data;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.content.ApiListResponseContent;
import org.apiaddicts.apitools.apigen.generatorcore.utils.CustomStringUtils;

import javax.lang.model.element.Modifier;
import java.util.List;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

public abstract class ListResponseBuilder extends ResponseBuilder {

    protected String basePackage;
    protected String jsonListName;
    protected String javaListName;
    protected TypeName resourceType;
    protected TypeSpec.Builder contentBuilder;

    public ListResponseBuilder(String jsonListName, TypeName resourceType, String basePackage) {
        this.jsonListName = jsonListName;
        this.javaListName = CustomStringUtils.snakeCaseToCamelCase(jsonListName);
        this.resourceType = resourceType;
        this.basePackage = basePackage;
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addConstructor();
        addContentType();
    }

    private void initializeBuilder() {
        builder = getClass(getName())
                .superclass(getParentClass())
                .addAnnotation(Data.class);
    }

    protected abstract String getName();

    private TypeName getParentClass() {
        return ParameterizedTypeName.get(
                ClassName.get(ApiResponse.class),
                ParameterizedTypeName.get(
                        ClassName.get(ApiListResponseContent.class),
                        resourceType
                )
        );
    }

    private void addConstructor() {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addParameter(ParameterizedTypeName.get(
                        ClassName.get(List.class),
                        resourceType
                ), javaListName)
                .addStatement("super(new " + getContentName() + "(" + javaListName + "))")
                .build();
        builder.addMethod(constructor);
    }

    private void addContentType() {
        generateContentBuilder();
        addContentConstructor();
        addContentGetter();
        builder.addType(contentBuilder.build());
    }

    private void generateContentBuilder() {
        contentBuilder = getInnerClass(getContentName())
                .superclass(getContentParentClass())
                .addAnnotation(Data.class);
    }

    protected abstract String getContentName();

    private TypeName getContentParentClass() {
        return ParameterizedTypeName.get(
                ClassName.get(ApiListResponseContent.class),
                resourceType
        );
    }

    private void addContentConstructor() {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addParameter(ParameterizedTypeName.get(
                        ClassName.get(List.class),
                        resourceType
                ), javaListName)
                .addStatement("super(" + javaListName + ")")
                .build();
        contentBuilder.addMethod(constructor);
    }

    private void addContentGetter() {
        MethodSpec get = MethodSpec.methodBuilder("getContent")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(getAnnotation(JsonProperty.class).addMember(VALUE, STRING, jsonListName).build())
                .returns(ParameterizedTypeName.get(
                        ClassName.get(List.class),
                        resourceType
                ))
                .addStatement("return content")
                .build();
        contentBuilder.addMethod(get);
    }
}
