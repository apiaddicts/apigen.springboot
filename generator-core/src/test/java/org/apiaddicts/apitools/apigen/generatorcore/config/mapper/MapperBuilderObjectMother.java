package org.apiaddicts.apitools.apigen.generatorcore.config.mapper;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.generator.mapper.MapperBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.ComposedIdBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output.EntityOutputResourceBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class MapperBuilderObjectMother {

    public static MapperBuilder createMapperWithDefaultOutResource(String entityName, String basePackage, String basicAttribute, String relatedEntityName) {
        Set<String> basicAttributes = new HashSet<>(Collections.singletonList(basicAttribute));
        Set<String> relatedEntities = new HashSet<>(Collections.singletonList(relatedEntityName));
        TypeName outResource = EntityOutputResourceBuilder.getTypeName(entityName, basePackage);
        return new MapperBuilder(entityName, basePackage, basicAttributes, relatedEntities, Collections.emptySet(), new HashSet<>(Collections.singletonList(outResource)), TypeName.get(Long.class));
    }
    public static MapperBuilder createMapperWithComposedID(String entityName, String basePackage, String basicAttribute, String relatedEntityName) {
        Set<String> basicAttributes = new HashSet<>(Collections.singletonList(basicAttribute));
        Set<String> relatedEntities = new HashSet<>(Collections.singletonList(relatedEntityName));
        TypeName outResource = EntityOutputResourceBuilder.getTypeName(entityName, basePackage);
        TypeName id = ComposedIdBuilder.getTypeName(entityName, basePackage);
        return new MapperBuilder(entityName, basePackage, basicAttributes, relatedEntities, Collections.emptySet(), new HashSet<>(Collections.singletonList(outResource)), id);
    }

}
