package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.lang.model.element.Modifier;
import javax.servlet.*;
import java.awt.*;
import java.io.IOException;

public class ApiResponseFilterBuilder<C extends JavaContext> extends AbstractJavaClassBuilder<C> {

    public ApiResponseFilterBuilder(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    public String getPackage() {
        return cfg.getBasePackage();
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addDoFilter();
        addPerformYourOwnBusinessLogicIntoThisMethod();
        addMove();
        addCreate();
        addRemove();
    }

    protected void initializeBuilder() {
        builder = TypeSpec.classBuilder("ApiResponseFilter").addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Filter.class)
                .addAnnotation(Slf4j.class).addAnnotation(Component.class)
                .addAnnotation(AllArgsConstructor.class);
    }

    private void addDoFilter() {
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("doFilter")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ParameterSpec.builder(TypeName.get(ServletRequest.class), "request").build())
                .addParameter(ParameterSpec.builder(TypeName.get(ServletResponse.class), "response").build())
                .addParameter(ParameterSpec.builder(TypeName.get(FilterChain.class), "chain").build())
                .addException(IOException.class)
                .addException(ServletException.class)
                .returns(TypeName.VOID)
                .addStatement("final ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response)")
                .addStatement("chain.doFilter(request, responseWrapper)")
                .build();
        builder.addMethod(methodSpec);
    }

    private void addPerformYourOwnBusinessLogicIntoThisMethod() {
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("performYourOwnBusinessLogicIntoThisMethod")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(TypeName.get(Byte[].class),"originalData").build())
                .addException(IOException.class)
                .returns(JsonNode.class)
                .addStatement("if (originalData.length == 0) return null")
                .addStatement("final ObjectMapper mapper = new ObjectMapper()")
                .addStatement("final ObjectNode root = (ObjectNode) mapper.readTree(originalData)")
                .addStatement("JsonNode target = root.deepCopy()")
                .build();
        builder.addMethod(methodSpec);
    }

    private void addMove() {
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("move")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(TypeName.get(JsonNode.class), "root").build())
                .addParameter(ParameterSpec.builder(TypeName.get(String.class), "pathOrigin").build())
                .addParameter(ParameterSpec.builder(TypeName.get(String.class), "pathTarget").build())
                .returns(TypeName.VOID)
                .addStatement("$1T node = root", JsonNode.class)
                //.addStatement("for (String prop : pathOrigin) { node = node == null ? node : node.get(prop); }")
                .addStatement("create(root, node, pathTarget)")
                .addStatement("remove(root, pathOrigin)")
                .build();
        builder.addMethod(methodSpec);
    }

    private void addCreate() {
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("create")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(TypeName.get(JsonNode.class), "root").build())
                .addParameter(ParameterSpec.builder(TypeName.get(JsonNode.class), "node").build())
                .addParameter(ParameterSpec.builder(TypeName.get(String.class), "path").build())
                .returns(TypeName.VOID)
                .addStatement("if (node == null) return")
                .build();
        builder.addMethod(methodSpec);
    }

    private void addRemove() {
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("remove")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(TypeName.get(JsonNode.class), "root").build())
                .addParameter(ParameterSpec.builder(TypeName.get(String.class), "path").build())
                .returns(TypeName.VOID)
                .build();
        builder.addMethod(methodSpec);
    }

}
