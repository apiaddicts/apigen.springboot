package net.cloudappi.apigen.generatorcore.generator.persistence;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;
import net.cloudappi.apigen.generatorcore.config.entity.Column;
import net.cloudappi.apigen.generatorcore.config.entity.Entity;
import net.cloudappi.apigen.generatorcore.config.entity.*;
import net.cloudappi.apigen.generatorcore.generator.common.AbstractClassBuilder;
import net.cloudappi.apigen.generatorcore.generator.common.ApigenExt2JavapoetType;
import net.cloudappi.apigen.generatorcore.utils.CustomStringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.lang.model.element.Modifier;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.cloudappi.apigen.generatorcore.generator.common.Formats.*;
import static net.cloudappi.apigen.generatorcore.generator.common.Members.*;

@Slf4j
public class EntityBuilder extends AbstractClassBuilder {

    private Entity entity;
    private EntityRelationManager relationManager;
    private String basePackage;

    public EntityBuilder(Entity entity, EntityRelationManager relationManager, String basePackage) {
        this.entity = entity;
        this.relationManager = relationManager;
        this.basePackage = basePackage;
    }

    public boolean hasComposedID() {
        return hasComposedID(entity);
    }

    private static boolean hasComposedID(Entity entity) {
        return getComposedIDAttribute(entity).isPresent();
    }

    private static Optional<Attribute> getComposedIDAttribute(Entity entity) {
        return entity.getAttributes().stream()
                .filter(EntityBuilder::isComposedPrimaryKey)
                .findFirst();
    }

    private static boolean isComposedPrimaryKey(Attribute attribute) {
        return ApigenExt2JavapoetType.isComposedID(attribute.getType());
    }

    private static Optional<Attribute> getSimpleIDAttribute(Entity entity) {
        return entity.getAttributes().stream().filter(EntityBuilder::isPrimaryKey).findFirst();
    }

    private static boolean isPrimaryKey(Attribute attribute) {
        return attribute.getColumns().size() == 1 && attribute.getColumns().get(0).getPrimaryKey();
    }

    private static String getIDName(Entity entity) {
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
        return ClassName.get(getPackage(entityName, basePackage), entityName);
    }

    private static String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName);
    }

    @Override
    public String getPackage() {
        return getPackage(entity.getName(), basePackage);
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addSetAndGetIdMethods();
        addAttributes();
        addIsReferenceMethod();
    }

    private void initializeBuilder() {
        builder = getClass(entity.getName())
                .superclass(getParentClass())
                .addAnnotation(Getter.class).addAnnotation(Setter.class)
                .addAnnotation(getAnnotation(javax.persistence.Entity.class).build())
                .addAnnotation(getAnnotation(Table.class).addMember(NAME, STRING, getTable()).build());
    }

    private String getTable() {
        if (entity.getTable() == null) return CustomStringUtils.camelCaseToSnakeCase(entity.getName());
        return entity.getTable();
    }

    private ParameterizedTypeName getParentClass() {
        return ParameterizedTypeName.get(
                ClassName.get(ApigenAbstractPersistable.class),
                EntityBuilder.getIDTypeName(entity, basePackage)
        );
    }

    private void addSetAndGetIdMethods() {
        String attributeName = getIDName(entity);
        if (attributeName == null) return;
        TypeName identifierType = getIDTypeName(entity, basePackage);

        builder.addMethod(MethodSpec.methodBuilder("getId")
                .addAnnotation(AnnotationSpec.builder(Override.class).build())
                .addModifiers(Modifier.PUBLIC)
                .returns(identifierType)
                .addStatement("return this.$N", attributeName)
                .build());

        builder.addMethod(MethodSpec.methodBuilder("setId")
                .addAnnotation(AnnotationSpec.builder(Override.class).build())
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(identifierType, attributeName)
                .addStatement("this.$N = $N", attributeName, attributeName)
                .build());
    }

    private void addAttributes() {
        List<Attribute> attributes = entity.getAttributes();
        if (nonNull(attributes)) {
            attributes.forEach(this::addAttribute);
        }
    }

    private void addAttribute(Attribute attribute) {
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

    private FieldSpec.Builder getBasicFieldBuilder(Attribute attribute) {
        TypeName attributeType = getType(attribute);
        return FieldSpec.builder(attributeType, attribute.getName(), Modifier.PRIVATE);
    }

    private TypeName getType(Attribute attribute) {
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

    private boolean isToOne(Relation relation) {
        if (isNull(relation)) return false;
        RelationType type = relation.getType();
        return RelationType.MANY_TO_ONE.equals(type) || RelationType.ONE_TO_ONE.equals(type) || RelationType.ONE_TO_ONE_OWNER.equals(type);
    }

    private boolean isToMany(Relation relation) {
        if (isNull(relation)) return false;
        RelationType type = relation.getType();
        return RelationType.ONE_TO_MANY.equals(type) || RelationType.MANY_TO_MANY.equals(type) || RelationType.MANY_TO_MANY_OWNER.equals(type);
    }

    private void addColumnAnnotation(String javaName, Column column, FieldSpec.Builder fieldBuilder) {
        if (isNull(column) || isNull(fieldBuilder)) return;
        String columnName = column.getName();
        if (columnName == null) columnName = CustomStringUtils.camelCaseToSnakeCase(javaName);
        Boolean isUnique = column.getUnique();

        AnnotationSpec.Builder annotationBuilder = getAnnotation(javax.persistence.Column.class)
                .addMember(NAME, STRING, columnName);

        if (nonNull(isUnique) && isUnique) {
            annotationBuilder.addMember(UNIQUE, LITERAL, true);
        }

        fieldBuilder.addAnnotation(annotationBuilder.build());
    }

    private void addComposedIdAnnotation(FieldSpec.Builder fieldBuilder) {
        fieldBuilder.addAnnotation(EmbeddedId.class);
    }

    private void addIdAnnotation(String type, Column column, FieldSpec.Builder fieldBuilder) {

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

    private void addSequenceIdGenerationAnnotations(String sequence, FieldSpec.Builder fieldBuilder) {
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

    private void addUUIDIdGenerationAnnotations(FieldSpec.Builder fieldBuilder) {
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
        // @GeneratedValue(generator="native")
        // @GenericGenerator(name = "native", strategy = "native")
        AnnotationSpec.Builder generatedValueAnnotation = getAnnotation(GeneratedValue.class)
                .addMember(GENERATOR, STRING, "native");
        fieldBuilder.addAnnotation(generatedValueAnnotation.build());
        AnnotationSpec.Builder genericGeneratorAnnotationNative = getAnnotation(GenericGenerator.class)
                .addMember(NAME, STRING, "native")
                .addMember(STRATEGY, STRING, "native");
        fieldBuilder.addAnnotation(genericGeneratorAnnotationNative.build());
    }

    private void addIsReferenceMethod() {
        String idAttributeName = getIDName(entity);
        List<String> otherAttributes = entity.getAttributes().stream()
                .map(Attribute::getName).filter(n -> !n.equals(idAttributeName)).collect(Collectors.toList());

        StringBuilder statement = new StringBuilder();
        statement.append("return getId() != null");
        otherAttributes.forEach(a -> statement.append(" && ").append(a).append(" == null"));

        builder.addMethod(MethodSpec.methodBuilder("isReference")
                .addAnnotation(AnnotationSpec.builder(Override.class).build())
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addStatement(statement.toString())
                .build());
    }
}
