package net.cloudappi.apigen.generatorcore.generator.web.resource;

import com.squareup.javapoet.TypeName;
import net.cloudappi.apigen.generatorcore.generator.web.resource.input.InputResourceBuilder;

import java.util.*;

public class ResourcesData {
    private Map<String, Set<TypeName>> resourcesToEntity = new HashMap<>();

    public ResourcesData(List<InputResourceBuilder> inputResourceBuilders) {
        inputResourceBuilders.forEach(builder -> {
            String entityName = builder.getEntityName();
            resourcesToEntity.putIfAbsent(entityName, new HashSet<>());
            resourcesToEntity.get(entityName).add(builder.getTypeName());
        });
    }


    public Set<TypeName> getInputResources(String entityName) {
        return resourcesToEntity.getOrDefault(entityName, Collections.emptySet());
    }
}
