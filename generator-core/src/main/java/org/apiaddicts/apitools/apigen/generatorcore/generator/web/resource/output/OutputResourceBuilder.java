package org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.ResourceBuilder;

@Deprecated
public abstract class OutputResourceBuilder extends ResourceBuilder {
    public abstract String getEntityName();

    public abstract TypeName getTypeName();
}
