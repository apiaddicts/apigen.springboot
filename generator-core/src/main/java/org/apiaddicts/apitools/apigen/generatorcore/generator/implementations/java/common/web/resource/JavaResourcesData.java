package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.web.resource.ResourcesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input.InputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.OutputResourceBuilder;

import java.util.*;

public class JavaResourcesData implements ResourcesData<TypeName>{
    protected final Map<String, Set<TypeName>> resourcesToEntity = new HashMap<>();
    protected final Map<String, Set<TypeName>> entityToResources = new HashMap<>();
    protected final Map<String, List<JavaSubResourcesData>> subResourcesToEntity = new HashMap<>();

    public JavaResourcesData(List<InputResourceBuilder> inputResourceBuilders, List<OutputResourceBuilder> outputResourceBuilders) {
        inputResourceBuilders.forEach(builder -> {
            String entityName = builder.getEntityName();
            resourcesToEntity.putIfAbsent(entityName, new HashSet<>());
            resourcesToEntity.get(entityName).add(builder.getTypeName());
            List<JavaSubResourcesData> subResourcesData = builder.getSubResourcesData();
            if(subResourcesToEntity.get(entityName) == null || !subResourcesData.isEmpty())
                subResourcesToEntity.put(entityName, subResourcesData);
        });
        outputResourceBuilders.forEach(builder -> {
            String entityName = builder.getEntityName();
            entityToResources.putIfAbsent(entityName, new HashSet<>());
            entityToResources.get(entityName).add(builder.getTypeName());
        });
    }

    @Override
    public Set<TypeName> getInputResources(String entityName) {
        return new HashSet<>(resourcesToEntity.getOrDefault(entityName, Collections.emptySet()));
    }

    @Override
    public Set<TypeName> getOutputResources(String entityName) {
        return new HashSet<>(entityToResources.getOrDefault(entityName, Collections.emptySet()));
    }

    @Override
    public List<JavaSubResourcesData> getSubResourcesData(String entityName) {
        return subResourcesToEntity.getOrDefault(entityName, new ArrayList<>());
    }
}
