package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.web.resource;

import com.squareup.javapoet.TypeName;
import lombok.Data;

import java.util.List;

@Data
public abstract class ResourceDataSubEntity<T> {
    public ResourceDataSubEntity(String relatedEntity, T resourceEntity, T entityFieldName) {
        this.relatedEntity = relatedEntity;
        this.resourceEntity = resourceEntity;
        this.entityFieldName = entityFieldName;
    }
    String relatedEntity;
    T resourceEntity;
    T entityFieldName;
}