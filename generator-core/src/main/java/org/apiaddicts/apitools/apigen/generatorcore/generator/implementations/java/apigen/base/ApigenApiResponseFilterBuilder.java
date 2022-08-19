package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base;

import com.squareup.javapoet.TypeSpec;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.ApiResponseFilterBuilder;

import javax.lang.model.element.Modifier;
import java.awt.*;
import javax.servlet.Filter;

public class ApigenApiResponseFilterBuilder<C extends ApigenContext> extends ApiResponseFilterBuilder<C> {

    public ApigenApiResponseFilterBuilder(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    protected void initializeBuilder() {
        builder = TypeSpec.classBuilder("ApiResponseFilter").addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Filter.class)
                .addAnnotation(Slf4j.class).addAnnotation(Component.class)
                .addAnnotation(AllArgsConstructor.class);
    }
}
