package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.Openapi2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaMethodParameterBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.util.List;

public abstract class ParameterBuilder<C extends JavaContext> extends AbstractJavaMethodParameterBuilder<C> {

    public final Parameter parameter;
    public final String javaName;
    public final TypeName typeName;

    public ParameterBuilder(Parameter parameter, String javaName, C ctx, Configuration cfg) {
        this(parameter, null, javaName, ctx, cfg);
    }

    public ParameterBuilder(Parameter parameter, TypeName typeName, String javaName, C ctx, Configuration cfg) {
        super(ctx, cfg);
        this.parameter = parameter;
        this.typeName = typeName == null ? getTypeName(parameter) : typeName;
        this.javaName = javaName;
    }

    protected TypeName getTypeName(Parameter parameter) {
        TypeName typeName = Openapi2JavapoetType.transformSimpleType(parameter.getType(), parameter.getFormat());
        if (parameter.isCollection()) typeName = ParameterizedTypeName.get(ClassName.get(List.class), typeName);
        return typeName;
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addAnnotations();
        addValidations();
    }
    protected void initializeBuilder() {
        builder = ParameterSpec.builder(typeName, javaName);
    }

    protected void addAnnotations() {
        // Intentional blank
    }

    protected void addValidations() {
        parameter.getValidations().forEach(v -> v.apply(builder));
    }
}
