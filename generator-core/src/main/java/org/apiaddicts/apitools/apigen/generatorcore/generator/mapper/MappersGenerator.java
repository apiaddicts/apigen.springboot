package org.apiaddicts.apitools.apigen.generatorcore.generator.mapper;

import com.squareup.javapoet.TypeName;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.ResourcesData;

import java.util.*;


@Deprecated
@Slf4j
public class MappersGenerator extends AbstractGenerator {

    private Map<String, MapperBuilder> builders = new HashMap<>();

    public MappersGenerator(Collection<Entity> entities, EntitiesData entitiesData, ResourcesData resourcesData, String basePackage) {
        entities.stream().filter(e -> !builders.containsKey(e.getName())).
                forEach(e -> {
                    String name = e.getName();
                    Set<String> basicAttributes = entitiesData.getBasicAttributes(name);
                    Set<String> relateEntities = entitiesData.getRelatedEntities(name);
                    Set<TypeName> inputResources = resourcesData.getInputResources(name);
                    Set<TypeName> outputResources = resourcesData.getOutputResources(name);
                    TypeName idType = entitiesData.getIDType(name);
                    builders.put(name, new MapperBuilder(name, basePackage, basicAttributes, relateEntities, inputResources, outputResources, idType));
                });
    }

    @Override
    protected Collection<AbstractClassBuilder> getBuilders() {
        return new ArrayList<>(builders.values());
    }
}
