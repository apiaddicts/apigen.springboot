package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base;

import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.archetypecore.autoconfigure.ApigenApplication;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.ApplicationBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.lang.model.element.Modifier;

public class ApigenApplicationBuilder<C extends ApigenContext> extends ApplicationBuilder<C> {

    public ApigenApplicationBuilder(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    protected void initializeBuilder() {
        builder = TypeSpec.classBuilder("Application").addModifiers(Modifier.PUBLIC)
                .addAnnotation(ApigenApplication.class).addAnnotation(SpringBootApplication.class);
    }
}
