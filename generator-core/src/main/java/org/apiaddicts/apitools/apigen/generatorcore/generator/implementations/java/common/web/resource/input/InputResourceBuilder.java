package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.AbstractResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.JavaResourceDataSubEntity;

import java.util.List;

public abstract class InputResourceBuilder<C extends JavaContext> extends AbstractResourceBuilder<C> {

    public InputResourceBuilder(C ctx, Configuration configuration) {
        super(ctx, configuration);
    }

    public abstract String getEntityName();

    public abstract TypeName getTypeName();

    public abstract List<JavaResourceDataSubEntity> getResourceDataSubEntity();
}
