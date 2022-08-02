package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.FileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.base.AbstractPropertiesFileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class JavaPropertiesGenerator<C extends JavaContext> extends AbstractJavaGenerator<C> {

    protected List<AbstractPropertiesFileBuilder<C>> builders;

    public JavaPropertiesGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    public List<? extends FileBuilder> getBuilders() {
        return builders;
    }

    @Override
    public void init() {
        builders = Arrays.asList(
                new JavaPropertiesFileBuilder<>("application.properties", ctx, cfg),
                new JavaPropertiesFileBuilder<>("application-dev.properties", ctx, cfg),
                new JavaPropertiesFileBuilder<>("application-pre.properties", ctx, cfg),
                new JavaPropertiesFileBuilder<>("application-pro.properties", ctx, cfg),
                new JavaPropertiesFileBuilder<>("application-test.properties", ctx, cfg)
        );
    }
}
