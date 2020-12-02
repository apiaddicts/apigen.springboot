package net.cloudappi.apigen.generatorcore.generator.base;

import net.cloudappi.apigen.generatorcore.generator.common.AbstractClassBuilder;
import net.cloudappi.apigen.generatorcore.generator.common.AbstractGenerator;

import java.util.Collection;
import java.util.Collections;


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
