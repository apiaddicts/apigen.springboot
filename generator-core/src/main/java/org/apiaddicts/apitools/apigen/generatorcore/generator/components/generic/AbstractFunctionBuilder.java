package org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;

public abstract class AbstractFunctionBuilder<C extends Context> {

    protected final C ctx;
    protected final Configuration cfg;

    public AbstractFunctionBuilder(C ctx, Configuration cfg) {
        this.ctx = ctx;
        this.cfg = cfg;
    }
}
