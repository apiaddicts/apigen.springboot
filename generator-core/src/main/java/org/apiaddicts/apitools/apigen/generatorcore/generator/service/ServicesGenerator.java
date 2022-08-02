package org.apiaddicts.apitools.apigen.generatorcore.generator.service;

import com.squareup.javapoet.TypeName;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData;

import java.util.*;

@Deprecated
@Slf4j
public class ServicesGenerator extends AbstractGenerator {
    private Map<String, ServiceBuilder> builders = new HashMap<>();

    public ServicesGenerator(Collection<Entity> entities, EntitiesData entitiesData, String basePackage) {
        entities.forEach(entity -> {
            String name = entity.getName();
            Set<String> basicAttributes = entitiesData.getBasicAttributes(name);
            TypeName idTypeName = entitiesData.getIDType(name);
            builders.put(name, new ServiceBuilder(name, basePackage, basicAttributes, idTypeName));
        });
    }

    @Override
    protected Collection<AbstractClassBuilder> getBuilders() {
        return new ArrayList<>(builders.values());
    }
}
