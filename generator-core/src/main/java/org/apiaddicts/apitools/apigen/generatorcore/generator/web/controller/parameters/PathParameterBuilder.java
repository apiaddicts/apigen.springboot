package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.parameters;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.springframework.web.bind.annotation.PathVariable;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

public class PathParameterBuilder extends ParameterBuilder {

    public PathParameterBuilder(Parameter parameter, String javaName) {
        super(parameter, javaName);
    }

    public PathParameterBuilder(Parameter parameter, TypeName typeName, String javaName) {
        super(parameter, typeName, javaName);
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addPathVariableAnnotation();
        addValidations();
    }

    private void addPathVariableAnnotation() {
        AnnotationSpec.Builder annotation = AnnotationSpec.builder(PathVariable.class)
                .addMember(VALUE, STRING, parameter.getName());
        builder.addAnnotation(annotation.build());
    }

}
