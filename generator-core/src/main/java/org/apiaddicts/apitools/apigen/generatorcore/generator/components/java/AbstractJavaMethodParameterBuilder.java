package org.apiaddicts.apitools.apigen.generatorcore.generator.components.java;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.AbstractFunctionParameterBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Context;

public abstract class AbstractJavaMethodParameterBuilder<C extends Context>
        extends AbstractFunctionParameterBuilder<C> {

    protected ParameterSpec.Builder builder;

    public AbstractJavaMethodParameterBuilder(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    public void apply(MethodSpec.Builder builder) {
        if (this.builder == null) {
            initialize();
        }
        builder.addParameter(this.builder.build());
    }

    protected abstract void initialize();
}
