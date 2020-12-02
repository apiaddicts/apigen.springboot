package net.cloudappi.apigen.generatorcore.config.mapper;

import net.cloudappi.apigen.generatorcore.generator.mapper.MapperBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class MapperBuilderObjectMother {

    public static MapperBuilder createMapper(String entityName, String basePackage, String basicAttribute, String relatedEntityName) {
        Set<String> basicAttributes = new HashSet<>(Collections.singletonList(basicAttribute));
        Set<String> relatedEntities = new HashSet<>(Collections.singletonList(relatedEntityName));
        return new MapperBuilder(entityName, basePackage, basicAttributes, relatedEntities, Collections.emptySet(), null);
    }

}
