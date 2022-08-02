package org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;


public abstract class AbstractGenerator<C extends Context> implements Generator {

    protected final C ctx;
    protected final Configuration cfg;

    public AbstractGenerator(C ctx, Configuration cfg) {
        this.ctx = ctx;
        this.cfg = cfg;
    }
}
