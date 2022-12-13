package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.web.resource;

import lombok.Data;

@Data
public abstract class SubResourcesData<T> {
    public SubResourcesData(String relatedEntity, T subResource) {
        this.relatedEntity = relatedEntity;
        this.subResource = subResource;
    }
    String relatedEntity;
    T resource;
    T subResource;
}
