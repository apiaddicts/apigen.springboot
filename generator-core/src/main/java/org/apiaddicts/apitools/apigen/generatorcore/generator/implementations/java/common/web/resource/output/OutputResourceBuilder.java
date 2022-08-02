package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.AbstractResourceBuilder;

public abstract class OutputResourceBuilder<C extends JavaContext> extends AbstractResourceBuilder<C> {
    public OutputResourceBuilder(C ctx, Configuration configuration) {
        super(ctx, configuration);
    }

    public abstract String getEntityName();

    public abstract TypeName getTypeName();
}
