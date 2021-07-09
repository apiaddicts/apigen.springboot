package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence;

import com.squareup.javapoet.FieldSpec;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.*;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.relations.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class EntityRelationManager {

    /* <ForeignEntityName, <EntityName, Attribute>> */
    private Map<String, Map<String, Attribute>> manyToManyMappedEntities = new HashMap<>();

    private Map<String, Map<String, Attribute>> manyToOneMappedEntities = new HashMap<>();

    /* <ForeignEntityName, <EntityName, Attribute>> */
    private Map<String, Map<String, Attribute>> oneToManyMappedEntities = new HashMap<>();

    /* <ForeignEntityName, <EntityName, Attribute>> */
    private Map<String, Map<String, Attribute>> oneToOneMappedEntities = new HashMap<>();

    public EntityRelationManager(Collection<Entity> entities) {
        detectRelations(entities);
    }

    private void detectRelations(Collection<Entity> entities) {
        detectAnyToManyRelations(entities);
        detectAnyToOneRelations(entities);
    }

    private void detectAnyToManyRelations(Collection<Entity> entities) {
        entities.stream()
                .flatMap(e -> e.getAttributes().stream().map(a -> Pair.of(a, e.getName())))
                .filter(p -> Objects.nonNull(p.getLeft().getRelation()))
                .filter(p -> p.getLeft().getIsCollection())
                .forEach(p -> identifyAnyToMany(p.getLeft(), p.getRight()));
    }

    private void detectAnyToOneRelations(Collection<Entity> entities) {
        entities.stream()
                .flatMap(e -> e.getAttributes().stream().map(a -> Pair.of(a, e.getName())))
                .filter(p -> Objects.nonNull(p.getLeft().getRelation()))
                .filter(p -> !p.getLeft().getIsCollection())
                .forEach(p -> identifyOneToOne(p.getLeft(), p.getRight()));
        entities.stream()
                .flatMap(e -> e.getAttributes().stream().map(a -> Pair.of(a, e.getName())))
                .filter(p -> Objects.nonNull(p.getLeft().getRelation()))
                .filter(p -> !p.getLeft().getIsCollection())
                .forEach(p -> identifyAnyToOne(p.getLeft(), p.getRight()));
    }

    private void identifyAnyToMany(Attribute attribute, String entityName) {
        Relation relation = attribute.getRelation();
        if (relation.getIntermediateTable() == null) {
            relation.setType(RelationType.ONE_TO_MANY);
            registerOneToMany(attribute, entityName);
        } else {
            if (relation.getOwner()) {
                relation.setType(RelationType.MANY_TO_MANY_OWNER);
            } else {
                relation.setType(RelationType.MANY_TO_MANY);
            }
            registerManyToMany(attribute, entityName);
        }
    }

    private void identifyOneToOne(Attribute attribute, String entityName) {
        if (attribute.getForeignColumns().isEmpty()) return;
        attribute.getRelation().setType(RelationType.ONE_TO_ONE);
        registerOneToOne(attribute, entityName);
    }

    private void identifyAnyToOne(Attribute attribute, String entityName) {
        if (!attribute.getForeignColumns().isEmpty()) return;

        // Try to find a correlation with a one to many
        for (Attribute foreignAttribute : oneToManyMappedEntities.getOrDefault(entityName, new HashMap<>()).values()) {
            if (columnsMatch(attribute, foreignAttribute)) {
                attribute.getRelation().setType(RelationType.MANY_TO_ONE);
                registerManyToOne(attribute, entityName);
                return;
            }
        }

        // Try to find a correlation with a one to one
        for (Attribute foreignAttribute : oneToOneMappedEntities.getOrDefault(entityName, new HashMap<>()).values()) {
            if (columnsMatch(attribute, foreignAttribute)) {
                attribute.getRelation().setType(RelationType.ONE_TO_ONE_OWNER);
                registerOneToOne(attribute, entityName);
                return;
            }
        }

        // Default to many to one
        attribute.getRelation().setType(RelationType.MANY_TO_ONE);
    }

    private boolean columnsMatch(Attribute attribute, Attribute foreignAttribute) {
        Set<String> columns = attribute.getColumns().stream().map(Column::getName).collect(Collectors.toSet());
        Set<String> foreignColumns = foreignAttribute.getForeignColumns().stream().map(Column::getName).collect(Collectors.toSet());
        return !foreignColumns.isEmpty() && foreignColumns.equals(columns);
    }

    private RelatedFieldBuilder generateRelation(String entityName, Attribute attribute) {
        String foreignClassName = attribute.getRelation().getRelatedEntity();
        RelationType relationType = attribute.getRelation().getType();

        switch (relationType) {
            case ONE_TO_MANY:
                String foreignAttributeName = manyToOneMappedEntities.get(entityName) != null ? manyToOneMappedEntities.get(entityName).get(foreignClassName).getName() : "";
                return new OneToManyBuilder(foreignAttributeName);
            case MANY_TO_ONE:
                return new ManyToOneBuilder(map(attribute.getColumns()));
            case MANY_TO_MANY_OWNER:
                Relation relation = attribute.getRelation();
                String intermediateTable = relation.getIntermediateTable();
                return new ManyToManyOwnerBuilder(intermediateTable, map(relation.getColumns()), map(relation.getReverseColumns()));
            case MANY_TO_MANY:
                String mappedByFieldNameManyToMany = manyToManyMappedEntities.get(entityName).get(foreignClassName).getName();
                return new ManyToManyBuilder(mappedByFieldNameManyToMany);
            case ONE_TO_ONE_OWNER:
                return new OneToOneOwnerBuilder(map(attribute.getColumns()));
            case ONE_TO_ONE:
                String mappedByFieldName = oneToOneMappedEntities.get(entityName).get(foreignClassName).getName();
                return new OneToOneBuilder(mappedByFieldName);
            default:
                throw new IllegalArgumentException(String.format("Relation type %s not supported", relationType));
        }
    }

    private List<ColumnRelation> map(List<Column> columns) {
        return columns.stream().map(c -> new ColumnRelation(c.getName(), c.getReferenceColumn())).collect(Collectors.toList());
    }

    public void applyRelation(String entityName, Attribute attribute, FieldSpec.Builder fieldBuilder) {
        generateRelation(entityName, attribute).apply(fieldBuilder);
    }

    private void registerManyToMany(Attribute attribute, String entityName) {
        Relation relation = attribute.getRelation();
        manyToManyMappedEntities.putIfAbsent(relation.getRelatedEntity(), new HashMap<>());
        manyToManyMappedEntities.get(relation.getRelatedEntity()).put(entityName, attribute);
    }

    private void registerManyToOne(Attribute attribute, String entityName) {
        Relation relation = attribute.getRelation();
        manyToOneMappedEntities.putIfAbsent(relation.getRelatedEntity(), new HashMap<>());
        manyToOneMappedEntities.get(relation.getRelatedEntity()).put(entityName, attribute);
    }

    private void registerOneToMany(Attribute attribute, String entityName) {
        Relation relation = attribute.getRelation();
        oneToManyMappedEntities.putIfAbsent(relation.getRelatedEntity(), new HashMap<>());
        oneToManyMappedEntities.get(relation.getRelatedEntity()).put(entityName, attribute);
    }

    private void registerOneToOne(Attribute attribute, String entityName) {
        Relation relation = attribute.getRelation();
        oneToOneMappedEntities.putIfAbsent(relation.getRelatedEntity(), new HashMap<>());
        oneToOneMappedEntities.get(relation.getRelatedEntity()).put(entityName, attribute);
    }

}
