package org.apiaddicts.apitools.apigen.generatorcore.generator.components.java;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.AbstractGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Context;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.FileBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


public abstract class AbstractJavaGenerator<C extends Context> extends AbstractGenerator<C> {

    public AbstractJavaGenerator(C ctx, Configuration configuration) {
        super(ctx, configuration);
    }

    // FIXME: review visibility
    public abstract List<? extends FileBuilder> getBuilders();

    // FIXME: review visibility
    public abstract void init();

    @Override
    public void generate(Path projectPath) throws IOException {
        init(); // TODO: Add safety check to avoid muliple calls
        for (FileBuilder builder : getBuilders()) {
            if (builder != null) builder.generate(projectPath);
        }
    }

}
