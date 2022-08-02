package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Column;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.*;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.ApigenExt2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.CustomStringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.lang.model.element.Modifier;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.*;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.*;

@Slf4j
public class EntityBuilder<C extends JavaContext> extends AbstractJavaClassBuilder<C> {

    protected final Entity entity;
    protected final JavaEntityRelationManager relationManager;
    protected final String basePackage;

    public EntityBuilder(Entity entity, C ctx, Configuration cfg) {
        super(ctx, cfg);
        this.entity = entity;
        this.relationManager = ctx.getEntityRelationManager();
        this.basePackage = cfg.getBasePackage();
    }

    public boolean hasComposedID() {
        return hasComposedID(entity);
    }

    protected static boolean hasComposedID(Entity entity) {
        return getComposedIDAttribute(entity).isPresent();
    }

    protected static Optional<Attribute> getComposedIDAttribute(Entity entity) {
        return entity.getAttributes().stream()
                .filter(EntityBuilder::isComposedPrimaryKey)
                .findFirst();
    }

    protected static boolean isComposedPrimaryKey(Attribute attribute) {
        return ApigenExt2JavapoetType.isComposedID(attribute.getType());
    }

    protected static Optional<Attribute> getSimpleIDAttribute(Entity entity) {
        return entity.getAttributes().stream().filter(EntityBuilder::isPrimaryKey).findFirst();
    }

    protected static boolean isPrimaryKey(Attribute attribute) {
        return attribute.getColumns().size() == 1 && attribute.getColumns().get(0).getPrimaryKey();
    }

    protected String getIDName(Entity entity) {
        Attribute idAttribute = getComposedIDAttribute(entity).orElse(null);
        if (idAttribute == null) idAttribute = getSimpleIDAttribute(entity).orElse(null);
        if (idAttribute == null) return null;
        else return idAttribute.getName();
    }

    public static TypeName getIDTypeName(Entity entity, String basePackage) {
        if (hasComposedID(entity)) return ComposedIdBuilder.getTypeName(entity.getName(), basePackage);
        Optional<Attribute> idAttribute = getSimpleIDAttribute(entity);
        return idAttribute.map(attribute -> ApigenExt2JavapoetType.transformSimpleType(attribute.getType()))
                .orElseGet(() -> ClassName.get(Long.class));
    }

    public static TypeName getTypeName(String entityName, String basePackage) {
        return ClassName.get(concatPackage(basePackage, entityName), entityName);
    }

    private String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName);
    }

    @Override
    public String getPackage() {
        return getPackage(entity.getName(), basePackage);
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addAttributes();
    }

    protected void initializeBuilder() {
        builder = getClass(entity.getName())
                .addAnnotation(Getter.class).addAnnotation(Setter.class)
                .addAnnotation(getAnnotation(javax.persistence.Entity.class).build())
                .addAnnotation(getAnnotation(Table.class).addMember(NAME, STRING, getTable()).build())
                .addAnnotation(NoArgsConstructor.class);
    }

    protected String getTable() {
        if (entity.getTable() == null) return CustomStringUtils.camelCaseToSnakeCase(entity.getName());
        return entity.getTable();
    }

    protected void addAttributes() {
        List<Attribute> attributes = entity.getAttributes();
        if (nonNull(attributes)) {
            attributes.forEach(this::addAttribute);
        }
    }

    protected void addAttribute(Attribute attribute) {
        log.debug("Parsing attribute '{}' of type '{}' in entity '{}' | {}", attribute.getName(), attribute.getType(), entity.getName(), attribute);
        FieldSpec.Builder fieldBuilder = getBasicFieldBuilder(attribute);
        if (nonNull(attribute.getRelation())) {
            relationManager.applyRelation(entity.getName(), attribute, fieldBuilder);
        } else if (isPrimaryKey(attribute)) {
            Column column = attribute.getColumns().get(0);
            addIdAnnotation(attribute.getType(), column, fieldBuilder);
            addColumnAnnotation(attribute.getName(), column, fieldBuilder);
        } else if (isComposedPrimaryKey(attribute)) {
            addComposedIdAnnotation(fieldBuilder);
        } else {
            Column column = attribute.getColumns().isEmpty() ? new Column() : attribute.getColumns().get(0);
            addColumnAnnotation(attribute.getName(), column, fieldBuilder);
        }
        attribute.getValidations().forEach(validation -> validation.apply(fieldBuilder));
        builder.addField(fieldBuilder.build());
    }

    protected FieldSpec.Builder getBasicFieldBuilder(Attribute attribute) {
        TypeName attributeType = getType(attribute);
        return FieldSpec.builder(attributeType, attribute.getName(), Modifier.PRIVATE);
    }

    protected TypeName getType(Attribute attribute) {
        String type = attribute.getType();
        Relation relation = attribute.getRelation();
        if (isComposedPrimaryKey(attribute)) return ComposedIdBuilder.getTypeName(entity.getName(), basePackage);
        if (isNull(relation)) return ApigenExt2JavapoetType.transformSimpleType(type);
        String relatedEntityName = relation.getRelatedEntity();
        TypeName relatedEntityType = EntityBuilder.getTypeName(relatedEntityName, basePackage);
        if (isToMany(relation)) {
            return ParameterizedTypeName.get(ClassName.get(Set.class), relatedEntityType);
        } else if (isToOne(relation)) {
            return relatedEntityType;
        } else {
            throw new IllegalArgumentException("Relation type " + relation.getType() + " not supported");
        }
    }

    protected boolean isToOne(Relation relation) {
        if (isNull(relation)) return false;
        RelationType type = relation.getType();
        return RelationType.MANY_TO_ONE.equals(type) || RelationType.ONE_TO_ONE.equals(type) || RelationType.ONE_TO_ONE_OWNER.equals(type);
    }

    protected boolean isToMany(Relation relation) {
        if (isNull(relation)) return false;
        RelationType type = relation.getType();
        return RelationType.ONE_TO_MANY.equals(type) || RelationType.MANY_TO_MANY.equals(type) || RelationType.MANY_TO_MANY_OWNER.equals(type);
    }

    protected void addColumnAnnotation(String javaName, Column column, FieldSpec.Builder fieldBuilder) {
        if (isNull(column) || isNull(fieldBuilder)) return;
        String columnName = column.getName();
        if (columnName == null) columnName = CustomStringUtils.camelCaseToSnakeCase(javaName);
        boolean isUnique = Boolean.TRUE.equals(column.getUnique());

        AnnotationSpec.Builder annotationBuilder = getAnnotation(javax.persistence.Column.class)
                .addMember(NAME, STRING, columnName);

        if (isUnique) {
            annotationBuilder.addMember(UNIQUE, LITERAL, true);
        }

        fieldBuilder.addAnnotation(annotationBuilder.build());
    }

    protected void addComposedIdAnnotation(FieldSpec.Builder fieldBuilder) {
        fieldBuilder.addAnnotation(EmbeddedId.class);
    }

    protected void addIdAnnotation(String type, Column column, FieldSpec.Builder fieldBuilder) {

        fieldBuilder.addAnnotation(Id.class);

        boolean isString = ApigenExt2JavapoetType.isString(type);
        boolean isNumeric = ApigenExt2JavapoetType.isNumeric(type);

        boolean isAutogenerated = column.getAutogenerated();
        boolean isSequence = nonNull(column.getSequence());

        isAutogenerated = isAutogenerated && (isString || isNumeric);

        if (isAutogenerated) {
            if (isString) {
                addUUIDIdGenerationAnnotations(fieldBuilder);
            } else {
                if (isSequence) {
                    addSequenceIdGenerationAnnotations(column.getSequence(), fieldBuilder);
                } else {
                    addNativeIdGenerationAnnotations(fieldBuilder);
                }
            }
        }
    }

    protected void addSequenceIdGenerationAnnotations(String sequence, FieldSpec.Builder fieldBuilder) {
        // @GeneratedValue(generator="sequence_name")
        // @SequenceGenerator(name="sequence_name", sequenceName="sequence")
        AnnotationSpec.Builder generatedValueAnnotation = getAnnotation(GeneratedValue.class)
                .addMember(GENERATOR, STRING, sequence + "_name");
        fieldBuilder.addAnnotation(generatedValueAnnotation.build());
        AnnotationSpec.Builder sequenceGeneratorAnnotation = getAnnotation(SequenceGenerator.class)
                .addMember(NAME, STRING, sequence + "_name")
                .addMember(SEQUENCE_NAME, STRING, sequence);
        fieldBuilder.addAnnotation(sequenceGeneratorAnnotation.build());
    }

    protected void addUUIDIdGenerationAnnotations(FieldSpec.Builder fieldBuilder) {
        // @GeneratedValue(generator="uuid")
        // @GenericGenerator(name = "uuid", strategy = "uuid2")
        AnnotationSpec.Builder generatedValueAnnotation = getAnnotation(GeneratedValue.class)
                .addMember(GENERATOR, STRING, "uuid");
        fieldBuilder.addAnnotation(generatedValueAnnotation.build());
        AnnotationSpec.Builder genericGeneratorAnnotationUUID = getAnnotation(GenericGenerator.class)
                .addMember(NAME, STRING, "uuid")
                .addMember(STRATEGY, STRING, "uuid2");
        fieldBuilder.addAnnotation(genericGeneratorAnnotationUUID.build());
    }

    private void addNativeIdGenerationAnnotations(FieldSpec.Builder fieldBuilder) {
        // @GeneratedValue(strategy = GenerationType.IDENTITY)
        // @GenericGenerator(name = "native", strategy = "native")
        AnnotationSpec.Builder generatedValueAnnotation = getAnnotation(GeneratedValue.class)
                .addMember(STRATEGY, ENUM_VALUE, GenerationType.class, GenerationType.IDENTITY);
        fieldBuilder.addAnnotation(generatedValueAnnotation.build());
    }

}
