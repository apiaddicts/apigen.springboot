package org.apiaddicts.apitools.apigen.generatorcore.generator.components.java;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.AbstractFunctionBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Context;

public abstract class AbstractJavaMethodBuilder<C extends Context> extends AbstractFunctionBuilder<C> {
    protected MethodSpec.Builder builder;

    public AbstractJavaMethodBuilder(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    public void apply(TypeSpec.Builder builder) {
        if (this.builder == null) {
            initialize();
        }
        builder.addMethod(this.builder.build());
    }

    protected abstract void initialize();
}
