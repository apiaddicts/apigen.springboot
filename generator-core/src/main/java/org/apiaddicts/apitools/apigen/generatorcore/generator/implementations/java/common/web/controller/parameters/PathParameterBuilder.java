package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.springframework.web.bind.annotation.PathVariable;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

public class PathParameterBuilder<C extends JavaContext> extends ParameterBuilder<C> {

    public PathParameterBuilder(Parameter parameter, String javaName, C ctx, Configuration cfg) {
        super(parameter, javaName, ctx, cfg);
    }

    public PathParameterBuilder(Parameter parameter, TypeName typeName, String javaName, C ctx, Configuration cfg) {
        super(parameter, typeName, javaName, ctx, cfg);
    }

    @Override
    protected void addAnnotations() {
        AnnotationSpec.Builder annotation =
                AnnotationSpec.builder(PathVariable.class).addMember(VALUE, STRING, parameter.getName());
        builder.addAnnotation(annotation.build());
    }
}
