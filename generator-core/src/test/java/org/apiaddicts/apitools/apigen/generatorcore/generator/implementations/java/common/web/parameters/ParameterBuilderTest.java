package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.parameters;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.config.parameter.ParameterObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters.ParameterBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters.PathParameterBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters.QueryParameterBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterBuilderTest {

    private static final JavaContext CTX = JavaContextObjectMother.create();
    private static final Configuration CFG = ConfigurationObjectMother.create();

    private MethodSpec.Builder methodBuilder;
    private ArgumentCaptor<ParameterSpec> captor;

    @BeforeEach
    public void beforeEach() {
        methodBuilder = Mockito.mock(MethodSpec.Builder.class);
        captor = ArgumentCaptor.forClass(ParameterSpec.class);
        Mockito.when(methodBuilder.addParameter(captor.capture())).thenReturn(methodBuilder);
    }

    @Test
    void generatePathParameterTest() {
        Parameter parameter = ParameterObjectMother.getBasicPathParameter();
        ParameterBuilder<JavaContext> parameterBuilder = new PathParameterBuilder<>(parameter, parameter.getName(), CTX, CFG);

        parameterBuilder.apply(methodBuilder);
        ParameterSpec generatedParameter = captor.getValue();

        assertEquals("basicPathParameter", generatedParameter.name);
        assertEquals("java.lang.String", generatedParameter.type.toString());
        assertEquals("[@org.springframework.web.bind.annotation.PathVariable(\"basicPathParameter\")]", generatedParameter.annotations.toString());
    }

    @Test
    void generateQueryParameterTest() {
        Parameter parameter = ParameterObjectMother.getBasicQueryParameter();
        ParameterBuilder<JavaContext> parameterBuilder = new QueryParameterBuilder<>(parameter, parameter.getName(), CTX, CFG);

        parameterBuilder.apply(methodBuilder);
        ParameterSpec generatedParameter = captor.getValue();

        assertEquals("basicQueryParameter", generatedParameter.name);
        assertEquals("java.lang.Long", generatedParameter.type.toString());
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(\"basicQueryParameter\")]", generatedParameter.annotations.toString());
    }

    @Test
    void generateCompleteIntegerParameterTest() {
        Parameter parameter = ParameterObjectMother.getIntegerParameter();
        ParameterBuilder<JavaContext> parameterBuilder = new QueryParameterBuilder<>(parameter, parameter.getName(), CTX, CFG);

        parameterBuilder.apply(methodBuilder);
        ParameterSpec generatedParameter = captor.getValue();

        assertEquals("integerParameter", generatedParameter.name);
        assertEquals("java.lang.Integer", generatedParameter.type.toString());
        assertEquals("[" +
                "@org.springframework.web.bind.annotation.RequestParam(value = \"integerParameter\", required = true, defaultValue = \"5\"), " +
                "@javax.validation.constraints.Min(5), " +
                "@javax.validation.constraints.Max(10)" +
                "]", generatedParameter.annotations.toString());
    }

    @Test
    void generateCompleteLongParameterTest() {
        Parameter parameter = ParameterObjectMother.getLongParameter();
        ParameterBuilder<JavaContext> parameterBuilder = new QueryParameterBuilder<>(parameter, parameter.getName(), CTX, CFG);

        parameterBuilder.apply(methodBuilder);
        ParameterSpec generatedParameter = captor.getValue();

        assertEquals("longParameter", generatedParameter.name);
        assertEquals("java.lang.Long", generatedParameter.type.toString());
        assertEquals("[" +
                "@org.springframework.web.bind.annotation.RequestParam(value = \"longParameter\", required = true, defaultValue = \"5\"), " +
                "@javax.validation.constraints.Min(5), " +
                "@javax.validation.constraints.Max(10)" +
                "]", generatedParameter.annotations.toString());
    }

    @Test
    void generateCompleteDoubleParameterTest() {
        Parameter parameter = ParameterObjectMother.getDoubleParameter();
        ParameterBuilder<JavaContext> parameterBuilder = new QueryParameterBuilder<>(parameter, parameter.getName(), CTX, CFG);

        parameterBuilder.apply(methodBuilder);
        ParameterSpec generatedParameter = captor.getValue();

        assertEquals("doubleParameter", generatedParameter.name);
        assertEquals("java.lang.Double", generatedParameter.type.toString());
        assertEquals("[" +
                "@org.springframework.web.bind.annotation.RequestParam(value = \"doubleParameter\", required = true, defaultValue = \"5.0\"), " +
                "@javax.validation.constraints.DecimalMin(value = \"5\", inclusive = false), " +
                "@javax.validation.constraints.DecimalMax(value = \"10\", inclusive = false)" +
                "]", generatedParameter.annotations.toString());

    }

    @Test
    void generateCompleteFloatParameterTest() {
        Parameter parameter = ParameterObjectMother.getFloatParameter();
        ParameterBuilder<JavaContext> parameterBuilder = new QueryParameterBuilder<>(parameter, parameter.getName(), CTX, CFG);

        parameterBuilder.apply(methodBuilder);
        ParameterSpec generatedParameter = captor.getValue();

        assertEquals("floatParameter", generatedParameter.name);
        assertEquals("java.lang.Float", generatedParameter.type.toString());
        assertEquals("[" +
                "@org.springframework.web.bind.annotation.RequestParam(value = \"floatParameter\", required = true, defaultValue = \"5.0\"), " +
                "@javax.validation.constraints.DecimalMin(value = \"5\", inclusive = false), " +
                "@javax.validation.constraints.DecimalMax(value = \"10\", inclusive = false)" +
                "]", generatedParameter.annotations.toString());
    }

    @Test
    void generateCompleteStringParameterTest() {
        Parameter parameter = ParameterObjectMother.getStringParameter();
        ParameterBuilder<JavaContext> parameterBuilder = new QueryParameterBuilder<>(parameter, parameter.getName(), CTX, CFG);

        parameterBuilder.apply(methodBuilder);
        ParameterSpec generatedParameter = captor.getValue();

        assertEquals("stringParameter", generatedParameter.name);
        assertEquals("java.lang.String", generatedParameter.type.toString());
        assertEquals("[" +
                "@org.springframework.web.bind.annotation.RequestParam(value = \"stringParameter\", required = true, defaultValue = \"test\"), " +
                "@javax.validation.constraints.Size(min = 5, max = 10), " +
                "@javax.validation.constraints.Pattern(regexp = \"\\\\d{9}\")" +
                "]", generatedParameter.annotations.toString());
    }

    @Test
    void generateCompleteArrayParameterTest() {
        Parameter parameter = ParameterObjectMother.getArrayParameter();
        ParameterBuilder<JavaContext> parameterBuilder = new QueryParameterBuilder<>(parameter, parameter.getName(), CTX, CFG);

        parameterBuilder.apply(methodBuilder);
        ParameterSpec generatedParameter = captor.getValue();

        assertEquals("arrayParameter", generatedParameter.name);
        assertEquals("java.util.List<java.lang.String>", generatedParameter.type.toString());
        assertEquals("[" +
                "@org.springframework.web.bind.annotation.RequestParam(value = \"arrayParameter\", required = true, defaultValue = \"primero,segundo,tercero,cuarto,quinto\"), " +
                "@javax.validation.constraints.Size(min = 5, max = 10)" +
                "]", generatedParameter.annotations.toString());
    }

    @Test
    void generateCompleteBooleanParameterTest() {
        Parameter parameter = ParameterObjectMother.getBooleanParameter();
        ParameterBuilder<JavaContext> parameterBuilder = new QueryParameterBuilder<>(parameter, parameter.getName(), CTX, CFG);

        parameterBuilder.apply(methodBuilder);
        ParameterSpec generatedParameter = captor.getValue();

        assertEquals("booleanParameter", generatedParameter.name);
        assertEquals("java.lang.Boolean", generatedParameter.type.toString());
        assertEquals("[" +
                "@org.springframework.web.bind.annotation.RequestParam(value = \"booleanParameter\", required = true, defaultValue = \"true\")" +
                "]", generatedParameter.annotations.toString());
    }
}
