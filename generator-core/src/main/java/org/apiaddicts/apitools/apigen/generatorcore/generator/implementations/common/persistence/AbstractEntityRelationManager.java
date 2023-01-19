package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.*;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.relations.ColumnRelation;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AbstractEntityRelationManager {

    /* <ForeignEntityName, <EntityName, List<Attribute>>> */
    protected Map<String, Map<String, List<Attribute>>> manyToManyMappedEntities = new HashMap<>();

    protected Map<String, Map<String, List<Attribute>>> manyToOneMappedEntities = new HashMap<>();

    /* <ForeignEntityName, <EntityName, List<Attribute>>> */
    protected Map<String, Map<String, List<Attribute>>> oneToManyMappedEntities = new HashMap<>();

    /* <ForeignEntityName, <EntityName, List<Attribute>>> */
    protected Map<String, Map<String, List<Attribute>>> oneToOneMappedEntities = new HashMap<>();

    public AbstractEntityRelationManager(Collection<Entity> entities) {
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
        for (List<Attribute> foreignAttributes : oneToManyMappedEntities.getOrDefault(entityName, new HashMap<>()).values()) {
            for (Attribute foreignAttribute: foreignAttributes) {
                if (columnsMatch(attribute, foreignAttribute)) {
                    attribute.getRelation().setType(RelationType.MANY_TO_ONE);
                    registerManyToOne(attribute, entityName);
                    return;
                }
            }
        }

        // Try to find a correlation with a one to one
        for (List<Attribute> foreignAttributes : oneToOneMappedEntities.getOrDefault(entityName, new HashMap<>()).values()) {
            for (Attribute foreignAttribute: foreignAttributes) {
                if (columnsMatch(attribute, foreignAttribute)) {
                    attribute.getRelation().setType(RelationType.ONE_TO_ONE_OWNER);
                    registerOneToOne(attribute, entityName);
                    return;
                }
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

    private void registerManyToMany(Attribute attribute, String entityName) {
        Relation relation = attribute.getRelation();
        manyToManyMappedEntities.putIfAbsent(relation.getRelatedEntity(), new HashMap<>());
        manyToManyMappedEntities.get(relation.getRelatedEntity()).putIfAbsent(entityName, new LinkedList<>());
        manyToManyMappedEntities.get(relation.getRelatedEntity()).get(entityName).add(attribute);
    }

    private void registerManyToOne(Attribute attribute, String entityName) {
        Relation relation = attribute.getRelation();
        manyToOneMappedEntities.putIfAbsent(relation.getRelatedEntity(), new HashMap<>());
        manyToOneMappedEntities.get(relation.getRelatedEntity()).putIfAbsent(entityName, new LinkedList<>());
        manyToOneMappedEntities.get(relation.getRelatedEntity()).get(entityName).add(attribute);
    }

    private void registerOneToMany(Attribute attribute, String entityName) {
        Relation relation = attribute.getRelation();
        oneToManyMappedEntities.putIfAbsent(relation.getRelatedEntity(), new HashMap<>());
        oneToManyMappedEntities.get(relation.getRelatedEntity()).putIfAbsent(entityName, new ArrayList<>());
        oneToManyMappedEntities.get(relation.getRelatedEntity()).get(entityName).add(attribute);
    }

    private void registerOneToOne(Attribute attribute, String entityName) {
        Relation relation = attribute.getRelation();
        oneToOneMappedEntities.putIfAbsent(relation.getRelatedEntity(), new HashMap<>());
        oneToOneMappedEntities.get(relation.getRelatedEntity()).putIfAbsent(entityName, new ArrayList<>());
        oneToOneMappedEntities.get(relation.getRelatedEntity()).get(entityName).add(attribute);
    }

    protected List<ColumnRelation> toColumnRelations(List<Column> columns) {
        return columns.stream().map(c -> new ColumnRelation(c.getName(), c.getReferenceColumn())).collect(Collectors.toList());
    }

    protected Attribute getRelatedEntityAttributeOneToMany(String entityName, String relatedEntityName, Attribute entityAttribute) {
        if (!manyToOneMappedEntities.containsKey(entityName)) return null;
        return manyToOneMappedEntities.get(entityName).get(relatedEntityName).stream()
                .filter(relatedEntityAttribute -> columnsMatch(entityAttribute, relatedEntityAttribute))
                .findFirst().orElseGet(() -> manyToOneMappedEntities.get(entityName).get(relatedEntityName).stream().findFirst().orElse(null));
    }

    protected Attribute getRelatedEntityAttributeOneToOne(String entityName, String relatedEntityName, Attribute entityAttribute) {
        return oneToOneMappedEntities.get(entityName).get(relatedEntityName).stream()
                .filter(relatedEntityAttribute -> columnsMatch(entityAttribute, relatedEntityAttribute))
                .findFirst().orElseGet(() -> oneToOneMappedEntities.get(entityName).get(relatedEntityName).stream().findFirst().orElse(null));
    }

    protected Attribute getRelatedEntityAttributeManyToMany(String entityName, String relatedEntityName, Attribute entityAttribute) {
        return manyToManyMappedEntities.get(entityName).get(relatedEntityName).stream().filter(relatedEntityAttribute -> {
            Relation relEntityRel = relatedEntityAttribute.getRelation();
            Relation entityRel = entityAttribute.getRelation();
            return relEntityRel.getIntermediateTable().equals(entityRel.getIntermediateTable());
        }).findFirst().orElse(null);
    }
}
