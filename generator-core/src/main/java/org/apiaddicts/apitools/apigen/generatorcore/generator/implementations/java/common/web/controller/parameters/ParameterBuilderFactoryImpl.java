package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters;

import com.squareup.javapoet.TypeName;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.CustomStringUtils;

@Slf4j
public class ParameterBuilderFactoryImpl<C extends JavaContext> implements ParameterBuilderFactory<C> {

    @Override
    public ParameterBuilder<C> create(Parameter param, TypeName type, C ctx, Configuration cfg) {
        if ("path".equalsIgnoreCase(param.getIn())) {
            return new PathParameterBuilder<>(param, type, getJavaParamName(param.getName()), ctx, cfg);
        } else if ("query".equalsIgnoreCase(param.getIn())) {
            return new QueryParameterBuilder<>(param, getJavaParamName(param.getName()), ctx, cfg);
        } else {
            log.error("Param not supported in {}", param.getIn());
            return null;
        }
    }

    protected String getJavaParamName(String name) {
        return CustomStringUtils.snakeCaseToCamelCase(name).replace("$", "");
    }
}
