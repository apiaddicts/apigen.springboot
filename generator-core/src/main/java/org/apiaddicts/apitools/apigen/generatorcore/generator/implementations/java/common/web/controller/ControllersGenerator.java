package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ControllersGenerator<C extends JavaContext> extends AbstractJavaGenerator<C> {

    protected final ArrayList<ControllerBuilder<C>> builders = new ArrayList<>();
    protected final ControllerBuilderFactory<C> factory;

    public ControllersGenerator(C ctx, Configuration cfg) {
        this(ctx, cfg, new ControllerBuilderFactoryImpl<>());
    }

    public ControllersGenerator(C ctx, Configuration cfg, ControllerBuilderFactory<C> factory) {
        super(ctx, cfg);
        this.factory = factory;
    }

    @Override
    public List<ControllerBuilder<C>> getBuilders() {
        return new ArrayList<>(builders);
    }

    @Override
    public void init() {
        cfg.getControllers().stream().map(c -> factory.create(c, ctx, cfg)).forEach(builders::add);
    }
}
