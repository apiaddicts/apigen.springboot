package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractGenerator;

import java.util.Collection;
import java.util.Collections;


@Deprecated
public class ApplicationGenerator extends AbstractGenerator {

    private ApplicationBuilder builder;

    public ApplicationGenerator(String basePackage) {
        builder = new ApplicationBuilder(basePackage);
    }

    @Override
    protected Collection<AbstractClassBuilder> getBuilders() {
        return Collections.singletonList(builder);
    }
}
