package net.cloudappi.apigen.generatorcore.config.parameter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import net.cloudappi.apigen.generatorcore.config.controller.Parameter;
import net.cloudappi.apigen.generatorcore.config.validation.Validation;
import net.cloudappi.apigen.generatorcore.config.validation.ValidationType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParameterObjectMother {

    public static Parameter getBasicPathParameter() {
        return getBasicPathParameter("basicPathParameter", "String");
    }

    public static Parameter getBasicPathParameter(String name) {
        return getBasicPathParameter(name, "String");
    }

    public static Parameter getBasicPathParameter(String name, String type) {
        Parameter parameter = new Parameter();
        parameter.setIn("path");
        parameter.setName(name);
        parameter.setType(type);
        parameter.setValidations(Collections.emptyList());
        return parameter;
    }

    public static Parameter getBasicIntegerPathParameter(String name) {
        Parameter parameter = new Parameter();
        parameter.setIn("path");
        parameter.setName("basicPathParameter");
        parameter.setType("string");
        parameter.setValidations(Collections.emptyList());
        return parameter;
    }

    public static Parameter getBasicQueryParameter() {
        Parameter parameter = new Parameter();
        parameter.setIn("query");
        parameter.setName("basicQueryParameter");
        parameter.setType("integer");
        parameter.setValidations(Collections.emptyList());
        return parameter;
    }

    public static Parameter getIntegerParameter() {
        Parameter parameter = new Parameter();
        parameter.setIn("query");
        parameter.setName("integerParameter");
        parameter.setType("integer");
        parameter.setFormat("int32");
        parameter.setRequired(true);
        parameter.setDefaultValue(5);
        parameter.setValidations(Arrays.asList(
                new Validation(ValidationType.MIN, 5L),
                new Validation(ValidationType.MAX, 10L)
        ));
        return parameter;
    }

    public static Parameter getLongParameter() {
        Parameter parameter = new Parameter();
        parameter.setIn("query");
        parameter.setName("longParameter");
        parameter.setType("integer");
        parameter.setFormat("int64");
        parameter.setRequired(true);
        parameter.setDefaultValue(5);
        parameter.setValidations(Arrays.asList(
                new Validation(ValidationType.MIN, 5L),
                new Validation(ValidationType.MAX, 10L)
        ));
        return parameter;
    }

    public static Parameter getDoubleParameter() {
        Parameter parameter = new Parameter();
        parameter.setIn("query");
        parameter.setName("doubleParameter");
        parameter.setType("number");
        parameter.setFormat("double");
        parameter.setRequired(true);
        parameter.setDefaultValue(5.0);
        parameter.setValidations(Arrays.asList(
                new Validation(ValidationType.DECIMAL_MIN, BigDecimal.valueOf(5), false),
                new Validation(ValidationType.DECIMAL_MAX, BigDecimal.valueOf(10), false)
        ));
        return parameter;
    }

    public static Parameter getFloatParameter() {
        Parameter parameter = new Parameter();
        parameter.setIn("query");
        parameter.setName("floatParameter");
        parameter.setType("number");
        parameter.setFormat("float");
        parameter.setRequired(true);
        parameter.setDefaultValue(5.0);
        parameter.setValidations(Arrays.asList(
                new Validation(ValidationType.DECIMAL_MIN, BigDecimal.valueOf(5), false),
                new Validation(ValidationType.DECIMAL_MAX, BigDecimal.valueOf(10), false)
        ));
        return parameter;
    }

    public static Parameter getStringParameter() {
        Parameter parameter = new Parameter();
        parameter.setIn("query");
        parameter.setName("stringParameter");
        parameter.setType("string");
        parameter.setRequired(true);
        parameter.setDefaultValue("test");
        parameter.setValidations(Arrays.asList(
                new Validation(ValidationType.SIZE, 5, 10),
                new Validation(ValidationType.PATTERN, "\\d{9}")
        ));
        return parameter;
    }

    public static Parameter getArrayParameter() {
        Parameter parameter = new Parameter();
        parameter.setIn("query");
        parameter.setName("arrayParameter");
        parameter.setType("string");
        parameter.setCollection(true);
        parameter.setRequired(true);
        ObjectMapper mapper = new ObjectMapper();
        List<String> arrayElements = Arrays.asList("primero", "segundo", "tercero", "cuarto", "quinto");
        ArrayNode arrayNode = mapper.valueToTree(arrayElements);
        parameter.setDefaultValue(arrayNode);
        parameter.setValidations(Collections.singletonList(
                new Validation(ValidationType.SIZE, 5, 10)
        ));
        return parameter;
    }

    public static Parameter getBooleanParameter() {
        Parameter parameter = new Parameter();
        parameter.setIn("query");
        parameter.setName("booleanParameter");
        parameter.setType("boolean");
        parameter.setRequired(true);
        parameter.setDefaultValue(true);
        parameter.setValidations(Collections.emptyList());
        return parameter;
    }

    public static Parameter createInitParameter(boolean required) {
        return createInitParameter(required, null, null, null);
    }

    public static Parameter createInitParameter(boolean required, String defaultValue) {
        return createInitParameter(required, defaultValue, null, null);
    }

    public static Parameter createInitParameter(boolean required, String defaultValue, BigDecimal minValue, BigDecimal maxValue) {
        return setAllParameters("query", "$init", "Integer", "int32", defaultValue, minValue, maxValue, required, false);
    }

    public static Parameter createLimitParameter(boolean required) {
        return createLimitParameter(required, null, null, null);
    }

    public static Parameter createLimitParameter(boolean required, String defaultValue) {
        return createLimitParameter(required, defaultValue, null, null);
    }

    public static Parameter createLimitParameter(boolean required, String defaultValue, BigDecimal minValue, BigDecimal maxValue) {
        return setAllParameters("query", "$limit", "Integer", "int32", defaultValue, minValue, maxValue, required, false);
    }

    public static Parameter createTotalParameter(boolean required) {
        return createTotalParameter(required, null, null, null);
    }

    public static Parameter createTotalParameter(boolean required, String defaultValue) {
        return createTotalParameter(required, defaultValue, null, null);
    }

    public static Parameter createTotalParameter(boolean required, String defaultValue, BigDecimal minValue, BigDecimal maxValue) {
        return setAllParameters("query", "$total", "boolean", null, defaultValue, minValue, maxValue, required, false);
    }

    public static Parameter createSelectParameter(boolean required) {
        return createSelectParameter(required, null);
    }

    public static Parameter createSelectParameter(boolean required, String defaultValue) {
        return setAllParameters("query", "$select", "string", null, defaultValue, null, null, required, true);
    }

    public static Parameter createExcludeParameter(boolean required) {
        return createExcludeParameter(required, null);
    }

    public static Parameter createExcludeParameter(boolean required, String defaultValue) {
        return setAllParameters("query", "$exclude", "string", null, defaultValue, null, null, required, true);
    }

    public static Parameter createExpandParameter(boolean required) {
        return createExpandParameter(required, null);
    }

    public static Parameter createExpandParameter(boolean required, String defaultValue) {
        return setAllParameters("query", "$expand", "string", null, defaultValue, null, null, required, true);
    }

    public static Parameter createOrderbyParameter(boolean required) {
        return createOrderbyParameter(required, null);
    }

    public static Parameter createOrderbyParameter(boolean required, String defaultValue) {
        return setAllParameters("query", "$orderby", "string", null, defaultValue, null, null, required, true);
    }

    public static Parameter setAllParameters(String in, String name, String type, String format, Object defaultValue,
                                             BigDecimal minValue, BigDecimal maxValue, boolean requiered, boolean collection) {
        Parameter parameter = new Parameter();
        parameter.setIn(in);
        parameter.setName(name);
        parameter.setType(type);
        parameter.setFormat(format);
        parameter.setDefaultValue(defaultValue);
        parameter.setRequired(requiered);
        parameter.setCollection(collection);
        parameter.setValidations(Collections.emptyList());
        return parameter;
    }

    public static List<Parameter> createGetByIdStandardParameters() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(createSelectParameter(false));
        parameters.add(createExcludeParameter(false));
        parameters.add(createExpandParameter(false));
        return parameters;
    }

    public static List<Parameter> createGetAllStandardParameters() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(createInitParameter(true, "0"));
        parameters.add(createLimitParameter(true, "25"));
        parameters.add(createTotalParameter(true, "false"));
        parameters.add(createSelectParameter(false));
        parameters.add(createExcludeParameter(false));
        parameters.add(createExpandParameter(false));
        parameters.add(createOrderbyParameter(false));
        return parameters;
    }
}
