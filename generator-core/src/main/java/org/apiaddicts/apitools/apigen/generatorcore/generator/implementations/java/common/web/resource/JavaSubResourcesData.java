package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.web.resource.SubResourcesData;

public class JavaSubResourcesData extends SubResourcesData<TypeName> {

    public JavaSubResourcesData(String relatedEntity, TypeName subResource) {
        super(relatedEntity, subResource);
    }
}
