package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.parameters;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.Openapi2JavapoetType;

import java.util.List;

public abstract class ParameterBuilder {

    protected Parameter parameter;
    protected String javaName;
    protected TypeName typeName;

    protected ParameterSpec.Builder builder;

    public ParameterBuilder(Parameter parameter, String javaName) {
        this.parameter = parameter;
        this.javaName = javaName;
        this.typeName = Openapi2JavapoetType.transformSimpleType(parameter.getType(), parameter.getFormat());
        if (parameter.isCollection()) typeName = ParameterizedTypeName.get(ClassName.get(List.class), typeName);
    }

    public ParameterBuilder(Parameter parameter, TypeName typeName, String javaName) {
        this.parameter = parameter;
        this.typeName = typeName;
        this.javaName = javaName;
    }

    protected abstract void initialize();

    public ParameterSpec build() {
        if (builder == null) initialize();
        return builder.build();
    }

    protected void initializeBuilder() {
        builder = ParameterSpec.builder(typeName, javaName);
    }

    protected void addValidations() {
        parameter.getValidations().forEach(v -> v.apply(builder));
    }
}
