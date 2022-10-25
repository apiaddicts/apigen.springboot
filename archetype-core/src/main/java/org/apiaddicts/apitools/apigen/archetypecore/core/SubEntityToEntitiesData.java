package org.apiaddicts.apitools.apigen.archetypecore.core;

import com.squareup.javapoet.TypeName;
import lombok.Data;

@Data
public class SubEntityToEntitiesData {
    public SubEntityToEntitiesData(String relatedEntity, TypeName resourceEntity, TypeName entityFieldName){
        this.relatedEntity = relatedEntity;
        this.resourceEntity = resourceEntity;
        this.entityFieldName = entityFieldName;
    }
    String relatedEntity;
    TypeName resourceEntity;
    TypeName entityFieldName;
}
