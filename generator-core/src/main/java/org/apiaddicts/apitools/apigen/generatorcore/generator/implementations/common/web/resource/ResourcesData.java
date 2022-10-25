package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.web.resource;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.archetypecore.core.SubEntityToEntitiesData;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ResourcesData<T> {
    Set<T> getInputResources(String entityName);
    Set<T> getOutputResources(String entityName);
    List<SubEntityToEntitiesData> getSubEntityToEntity(String entityName);
}
