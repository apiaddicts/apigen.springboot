package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.web.resource;

import java.util.List;
import java.util.Set;

public interface ResourcesData<T>{
    Set<T> getInputResources(String entityName);
    Set<T> getOutputResources(String entityName);
    List<? extends SubResourcesData<T>> getSubResourcesData(String entityName);

}
