package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.parameters;

import com.squareup.javapoet.ParameterSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.config.parameter.ParameterObjectMother;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Deprecated
class ParameterBuilderTest {

    @Test
    void generatePathParameterTest() {
        Parameter parameter = ParameterObjectMother.getBasicPathParameter();
        ParameterBuilder parameterBuilder = new PathParameterBuilder(parameter, parameter.getName());

        ParameterSpec generatedParameter = parameterBuilder.build();

        assertEquals("basicPathParameter", generatedParameter.name);
        assertEquals("java.lang.String", generatedParameter.type.toString());
        assertEquals("[@org.springframework.web.bind.annotation.PathVariable(\"basicPathParameter\")]", generatedParameter.annotations.toString());
    }

    @Test
    void generateQueryParameterTest() {
        Parameter parameter = ParameterObjectMother.getBasicQueryParameter();
        ParameterBuilder parameterBuilder = new QueryParameterBuilder(parameter, parameter.getName());

        ParameterSpec generatedParameter = parameterBuilder.build();

        assertEquals("basicQueryParameter", generatedParameter.name);
        assertEquals("java.lang.Long", generatedParameter.type.toString());
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(\"basicQueryParameter\")]", generatedParameter.annotations.toString());
    }

    @Test
    void generateCompleteIntegerParameterTest() {
        Parameter parameter = ParameterObjectMother.getIntegerParameter();
        ParameterBuilder parameterBuilder = new QueryParameterBuilder(parameter, parameter.getName());

        ParameterSpec generatedParameter = parameterBuilder.build();

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
        ParameterBuilder parameterBuilder = new QueryParameterBuilder(parameter, parameter.getName());

        ParameterSpec generatedParameter = parameterBuilder.build();

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
        ParameterBuilder parameterBuilder = new QueryParameterBuilder(parameter, parameter.getName());

        ParameterSpec generatedParameter = parameterBuilder.build();

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
        ParameterBuilder parameterBuilder = new QueryParameterBuilder(parameter, parameter.getName());

        ParameterSpec generatedParameter = parameterBuilder.build();

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
        ParameterBuilder parameterBuilder = new QueryParameterBuilder(parameter, parameter.getName());

        ParameterSpec generatedParameter = parameterBuilder.build();

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
        ParameterBuilder parameterBuilder = new QueryParameterBuilder(parameter, parameter.getName());

        ParameterSpec generatedParameter = parameterBuilder.build();

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
        ParameterBuilder parameterBuilder = new QueryParameterBuilder(parameter, parameter.getName());

        ParameterSpec generatedParameter = parameterBuilder.build();

        assertEquals("booleanParameter", generatedParameter.name);
        assertEquals("java.lang.Boolean", generatedParameter.type.toString());
        assertEquals("[" +
                "@org.springframework.web.bind.annotation.RequestParam(value = \"booleanParameter\", required = true, defaultValue = \"true\")" +
                "]", generatedParameter.annotations.toString());
    }
}
