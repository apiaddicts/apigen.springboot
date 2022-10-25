package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.archetypecore.core.SubEntityToEntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.web.resource.ResourcesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input.InputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.OutputResourceBuilder;
import org.springframework.web.client.HttpServerErrorException;

import java.util.*;

public class JavaResourcesData implements ResourcesData<TypeName> {
    protected final Map<String, Set<TypeName>> resourcesToEntity = new HashMap<>();
    protected final Map<String, Set<TypeName>> entityToResources = new HashMap<>();
    protected final Map<String, List<SubEntityToEntitiesData>>  subEntityToEntity = new HashMap<>();

    public JavaResourcesData(List<InputResourceBuilder> inputResourceBuilders, List<OutputResourceBuilder> outputResourceBuilders) {
        inputResourceBuilders.forEach(builder -> {
            String entityName = builder.getEntityName();
            resourcesToEntity.putIfAbsent(entityName, new HashSet<>());
            resourcesToEntity.get(entityName).add(builder.getTypeName());
            List<SubEntityToEntitiesData> subEntityToEntitiesData = builder.subEntityToEntity();
            if(subEntityToEntity.get(entityName) == null || subEntityToEntitiesData.size() > 0)
                subEntityToEntity.put(entityName, subEntityToEntitiesData);
        });
        outputResourceBuilders.forEach(builder -> {
            String entityName = builder.getEntityName();
            entityToResources.putIfAbsent(entityName, new HashSet<>());
            entityToResources.get(entityName).add(builder.getTypeName());
        });
    }

    @Override
    public Set<TypeName> getInputResources(String entityName) {
        return resourcesToEntity.getOrDefault(entityName, Collections.emptySet());
    }

    @Override
    public Set<TypeName> getOutputResources(String entityName) {
        return entityToResources.getOrDefault(entityName, Collections.emptySet());
    }

    @Override
    public List<SubEntityToEntitiesData> getSubEntityToEntity(String entityName) {
        return subEntityToEntity.getOrDefault(entityName, new ArrayList<>());
    }
}
