package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

@FunctionalInterface
public interface ParameterBuilderFactory<C extends JavaContext> {

    ParameterBuilder<C> create(Parameter param, TypeName type, C ctx, Configuration cfg);
}
