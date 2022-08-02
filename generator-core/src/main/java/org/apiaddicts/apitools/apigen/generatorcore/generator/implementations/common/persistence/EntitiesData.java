package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.persistence;

import java.util.Map;
import java.util.Set;

public interface EntitiesData<T> {
    Set<String> getRelatedEntities(String entityName);

    Set<String> getBasicAttributes(String entityName);

    T getIDType(String entityName);

    T getComposedIDType(String entityName);

    Map<String, AttributeData<T>> getAttributes(String entityName);
}
