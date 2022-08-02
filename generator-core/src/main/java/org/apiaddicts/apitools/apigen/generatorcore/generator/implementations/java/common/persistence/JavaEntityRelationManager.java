package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence;

import com.squareup.javapoet.FieldSpec;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Attribute;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Relation;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.RelationType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.persistence.AbstractEntityRelationManager;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.relations.*;

import java.util.Collection;

@Slf4j
public class JavaEntityRelationManager extends AbstractEntityRelationManager {

    public JavaEntityRelationManager(Collection<Entity> entities) {
        super(entities);
    }

    public void applyRelation(String entityName, Attribute attribute, FieldSpec.Builder fieldBuilder) {
        generateRelation(entityName, attribute).apply(fieldBuilder);
    }

    protected RelatedFieldBuilder generateRelation(String entityName, Attribute attribute) {
        String relatedEntityName = attribute.getRelation().getRelatedEntity();
        RelationType relationType = attribute.getRelation().getType();

        switch (relationType) {
            case ONE_TO_MANY:
                Attribute foreignAttribute = getRelatedEntityAttributeOneToMany(entityName, relatedEntityName);
                String foreignAttributeName = foreignAttribute != null ? foreignAttribute.getName() : "";
                return new OneToManyBuilder(foreignAttributeName);
            case MANY_TO_ONE:
                return new ManyToOneBuilder(toColumnRelations(attribute.getColumns()));
            case MANY_TO_MANY_OWNER:
                Relation relation = attribute.getRelation();
                String intermediateTable = relation.getIntermediateTable();
                return new ManyToManyOwnerBuilder(intermediateTable, toColumnRelations(relation.getColumns()),
                        toColumnRelations(relation.getReverseColumns()));
            case MANY_TO_MANY:
                String mappedByFieldNameManyToMany =
                        getRelatedEntityAttributeManyToMany(entityName, relatedEntityName).getName();
                return new ManyToManyBuilder(mappedByFieldNameManyToMany);
            case ONE_TO_ONE_OWNER:
                return new OneToOneOwnerBuilder(toColumnRelations(attribute.getColumns()));
            case ONE_TO_ONE:
                String mappedByFieldName = getRelatedEntityAttributeOneToOne(entityName, relatedEntityName).getName();
                return new OneToOneBuilder(mappedByFieldName);
            default:
                throw new IllegalArgumentException(String.format("Relation type %s not supported", relationType));
        }
    }
}
