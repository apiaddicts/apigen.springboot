package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.web.resource.ResourceDataSubEntity;

import java.util.*;

public class JavaResourceDataSubEntity extends ResourceDataSubEntity<TypeName>{

    public JavaResourceDataSubEntity(String relatedEntity, TypeName resourceEntity, TypeName entityFieldName) {
        super(relatedEntity, resourceEntity, entityFieldName);
    }
}