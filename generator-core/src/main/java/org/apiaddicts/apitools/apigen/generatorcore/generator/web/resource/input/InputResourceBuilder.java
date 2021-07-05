package org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.input;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.ResourceBuilder;

public abstract class InputResourceBuilder extends ResourceBuilder {

    public abstract String getEntityName();

    public abstract TypeName getTypeName();
}
