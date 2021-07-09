package org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.input.InputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output.OutputResourceBuilder;

import java.util.*;

public class ResourcesData {
    private Map<String, Set<TypeName>> resourcesToEntity = new HashMap<>();
    private Map<String, Set<TypeName>> entityToResources = new HashMap<>();

    public ResourcesData(List<InputResourceBuilder> inputResourceBuilders, List<OutputResourceBuilder> outputResourceBuilders) {
        inputResourceBuilders.forEach(builder -> {
            String entityName = builder.getEntityName();
            resourcesToEntity.putIfAbsent(entityName, new HashSet<>());
            resourcesToEntity.get(entityName).add(builder.getTypeName());
        });
        outputResourceBuilders.forEach(builder -> {
            String entityName = builder.getEntityName();
            entityToResources.putIfAbsent(entityName, new HashSet<>());
            entityToResources.get(entityName).add(builder.getTypeName());
        });
    }


    public Set<TypeName> getInputResources(String entityName) {
        return resourcesToEntity.getOrDefault(entityName, Collections.emptySet());
    }

    public Set<TypeName> getOutputResources(String entityName) {
        return entityToResources.getOrDefault(entityName, Collections.emptySet());
    }
}
