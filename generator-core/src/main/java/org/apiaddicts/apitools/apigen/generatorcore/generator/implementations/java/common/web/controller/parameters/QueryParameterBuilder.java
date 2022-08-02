package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.squareup.javapoet.AnnotationSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.LITERAL;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.*;

public class QueryParameterBuilder<C extends JavaContext> extends ParameterBuilder<C> {

    public QueryParameterBuilder(Parameter parameter, String javaName, C ctx, Configuration cfg) {
        super(parameter, javaName, ctx, cfg);
    }

    @Override
    protected void addAnnotations() {
        AnnotationSpec.Builder annotation =
                AnnotationSpec.builder(RequestParam.class).addMember(VALUE, STRING, parameter.getName());

        if (parameter.getRequired() != null) {
            annotation.addMember(REQUIRED, LITERAL, parameter.getRequired());
        }
        if (parameter.getDefaultValue() != null) {
            String defaultValue;
            if (parameter.isCollection()) {
                defaultValue = collectionToString(parameter.getDefaultValue());
            } else {
                defaultValue = parameter.getDefaultValue().toString();
            }
            annotation.addMember(DEFAULT_VALUE, STRING, defaultValue);
        }

        builder.addAnnotation(annotation.build());
    }

    private String collectionToString(Object collection) {
        ArrayNode arrayNode = (ArrayNode) collection;
        List<String> itemsArray = new ArrayList<>(arrayNode.size());
        arrayNode.forEach(jsonNode -> itemsArray.add(jsonNode.asText()));
        return String.join(",", itemsArray);
    }

}
