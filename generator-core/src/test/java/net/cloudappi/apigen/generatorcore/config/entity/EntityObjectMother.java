package net.cloudappi.apigen.generatorcore.config.entity;

import net.cloudappi.apigen.generatorcore.config.validation.Validation;
import net.cloudappi.apigen.generatorcore.config.validation.ValidationType;

import java.math.BigDecimal;
import java.util.*;

public class EntityObjectMother {

    public static Entity createSimpleEntityWithName() {
        return new Entity("SimpleTestEntity", "", new ArrayList<>());
    }

    public static Entity createSimpleEntityWithStringAsPrimaryKey() {
        Attribute primaryKeyAttribute = new Attribute("id", "String");
        primaryKeyAttribute.setColumns(Arrays.asList(new Column("primaryKeyColumn", true, true, null)));
        return new Entity("SimpleTestEntity", "", Collections.singletonList(primaryKeyAttribute));
    }

    public static Entity createEntityWithSimpleAttributes() {
        List<Attribute> attributeList = new ArrayList<>();

        Column column = new Column();
        column.setName("columnName");
        column.setPrimaryKey(false);

        Attribute longAttribute = new Attribute("longAttribute", "Long");
        longAttribute.setColumns(Arrays.asList(column));
        Attribute stringAttribute = new Attribute("stringAttribute", "String");
        stringAttribute.setColumns(Arrays.asList(column));

        attributeList.add(longAttribute);
        attributeList.add(stringAttribute);

        return new Entity("TestEntityWithSimpleAttributes", "tableName", attributeList);
    }

    public static Entity createEntityWithOneToManyAttributes(String foreignClassName) {
        List<Attribute> attributeList = new ArrayList<>();

        Column primaryKeyColumn = new Column("primaryKeyColumn", true, true, null);

        Column simpleColumn = new Column("simpleColumn", false);

        Column manyToOneRelationColumn = new Column("manyToOneRelationColumn", false);

        Attribute oneToManyRelationAttribute = new Attribute("oneToManyRelationAttribute", "Array");
        oneToManyRelationAttribute.setIsCollection(true);
        Relation relation = new Relation(foreignClassName);
        oneToManyRelationAttribute.setRelation(relation);
        oneToManyRelationAttribute.setType(foreignClassName);

        oneToManyRelationAttribute.setForeignColumns(Arrays.asList(manyToOneRelationColumn));

        Attribute longAttribute = new Attribute("longAttribute", "Long");
        longAttribute.setColumns(Arrays.asList(primaryKeyColumn));
        Attribute stringAttribute = new Attribute("stringAttribute", "String");
        stringAttribute.setColumns(Arrays.asList(simpleColumn));

        attributeList.add(longAttribute);
        attributeList.add(stringAttribute);
        attributeList.add(oneToManyRelationAttribute);

        return new Entity("TestEntityWithOneToManyAttributes", "tableName", attributeList);
    }

    public static List<Entity> createEntitiesWithOneToOneAttributes() {
        List<Entity> entitiesList = new ArrayList<>();

        List<Attribute> attributeListOwner = new ArrayList<>();
        List<Attribute> attributeList = new ArrayList<>();


        Column oneToOneOwnerRelationColumn = new Column("oneToOneOwnerRelationColumn", false);

        Attribute oneToOneOwnerRelationAttribute = new Attribute("oneToOneOwnerRelationAttribute", "TestEntityWithOneToOneAttributes");
        oneToOneOwnerRelationAttribute.setIsCollection(false);
        Relation relationOwner = new Relation("TestEntityWithOneToOneAttributes", true);

        oneToOneOwnerRelationAttribute.setRelation(relationOwner);
        oneToOneOwnerRelationAttribute.setType("TestEntityWithOneToOneAttributes");

        oneToOneOwnerRelationAttribute.setColumns(Arrays.asList(oneToOneOwnerRelationColumn));

        attributeListOwner.add(oneToOneOwnerRelationAttribute);

        Attribute oneToOneRelationAttribute = new Attribute("oneToOneRelationAttribute", "TestEntityWithOneToOneOwnerAttributes");
        oneToOneRelationAttribute.setIsCollection(false);

        Relation relation = new Relation("TestEntityWithOneToOneOwnerAttributes");
        oneToOneRelationAttribute.setRelation(relation);
        oneToOneRelationAttribute.setType("TestEntityWithOneToOneOwnerAttributes");

        oneToOneRelationAttribute.setForeignColumns(Arrays.asList(oneToOneOwnerRelationColumn));

        attributeList.add(oneToOneRelationAttribute);

        entitiesList.add(new Entity("TestEntityWithOneToOneOwnerAttributes", "tableName", attributeListOwner));
        entitiesList.add(new Entity("TestEntityWithOneToOneAttributes", "tableName", attributeList));

        return entitiesList;
    }

    public static List<Entity> createEntitiesWithOneToManyAndManyToOneAttributes() {
        List<Entity> entitiesList = new ArrayList<>();
        entitiesList.add(createEntityWithOneToManyAttributes("TestEntityWithManyToOneAttributes"));

        List<Attribute> attributeList = new ArrayList<>();

        Column oneToManyRelationColumn = new Column("manyToOneRelationColumn", false);

        Attribute manyToOneRelationAttribute = new Attribute("manyToOneRelationAttribute", "TestEntityWithOneToManyAttributes");
        manyToOneRelationAttribute.setIsCollection(false);
        Relation relation = new Relation("TestEntityWithOneToManyAttributes");
        manyToOneRelationAttribute.setRelation(relation);
        manyToOneRelationAttribute.setType("TestEntityWithOneToManyAttributes");

        manyToOneRelationAttribute.setColumns(Arrays.asList(oneToManyRelationColumn));
        manyToOneRelationAttribute.setForeignColumns(new ArrayList<>());

        attributeList.add(manyToOneRelationAttribute);

        entitiesList.add(new Entity("TestEntityWithManyToOneAttributes", "tableName", attributeList));

        return entitiesList;
    }

    public static List<Entity> createEntitiesWithManyToManyAttributes() {
        List<Entity> entitiesList = new ArrayList<>();

        List<Attribute> attributeListOwner = new ArrayList<>();
        List<Attribute> attributeList = new ArrayList<>();

        List<Column> columns = new ArrayList<>();
        Column column1 = new Column("column_1_id");
        columns.add(column1);

        List<Column> reverseColumns = new ArrayList<>();
        Column column2 = new Column("column_2_id");
        reverseColumns.add(column2);

        Attribute manyToManyOwnerRelationAttribute = new Attribute("manyToManyOwnerRelationAttribute", "Array");
        manyToManyOwnerRelationAttribute.setIsCollection(true);
        Relation relationOwner = new Relation("TestEntityWithManyToManyAttributes", columns, reverseColumns, "intermediateTableName", true);
        manyToManyOwnerRelationAttribute.setRelation(relationOwner);
        manyToManyOwnerRelationAttribute.setType("TestEntityWithManyToManyAttributes");
        Column manyToManyOwnerRelationColumn = new Column("manyToManyOwnerRelationColumn");
        manyToManyOwnerRelationAttribute.setColumns(Arrays.asList(manyToManyOwnerRelationColumn));
        attributeListOwner.add(manyToManyOwnerRelationAttribute);
        entitiesList.add(new Entity("TestEntityWithManyToManyOwnerAttributes", "tableName", attributeListOwner));

        Attribute manyToManyRelationAttribute = new Attribute("manyToManyRelationAttribute", "Array");
        manyToManyRelationAttribute.setIsCollection(true);
        Relation relation = new Relation("TestEntityWithManyToManyOwnerAttributes", columns, reverseColumns, "intermediateTableName");
        manyToManyRelationAttribute.setRelation(relation);
        manyToManyRelationAttribute.setType("TestEntityWithManyToManyOwnerAttributes");
        Column manyToManyRelationColumn = new Column("manyToManyRelationColumn");
        manyToManyRelationAttribute.setColumns(Arrays.asList(manyToManyRelationColumn));
        attributeList.add(manyToManyRelationAttribute);
        entitiesList.add(new Entity("TestEntityWithManyToManyAttributes", "tableName", attributeList));

        return entitiesList;
    }

    public static Entity createEntityWithAttributeValidations() {
        List<Attribute> attributeList = new ArrayList<>();

        Attribute validationsAttribute = new Attribute("validationsAttribute", "Long");
        validationsAttribute.setColumns(new ArrayList<>());
        validationsAttribute.setForeignColumns(new ArrayList<>());

        List<Validation> validationList = new ArrayList<>();

        Validation notNullValidation = new Validation(ValidationType.NOT_NULL);
        validationList.add(notNullValidation);

        Validation sizeValidation = new Validation(ValidationType.SIZE, 1, 2);
        validationList.add(sizeValidation);

        Validation minValidation = new Validation(ValidationType.MIN, 1L);
        validationList.add(minValidation);

        Validation maxValidation = new Validation(ValidationType.MAX, 2L);
        validationList.add(maxValidation);

        Validation emailValidation = new Validation(ValidationType.EMAIL);
        validationList.add(emailValidation);

        Validation notEmptyValidation = new Validation(ValidationType.NOT_EMPTY);
        validationList.add(notEmptyValidation);

        Validation notBlankValidation = new Validation(ValidationType.NOT_BLANK);
        validationList.add(notBlankValidation);

        Validation positiveValidation = new Validation(ValidationType.POSITIVE);
        validationList.add(positiveValidation);

        Validation positiveOrZeroValidation = new Validation(ValidationType.POSITIVE_OR_ZERO);
        validationList.add(positiveOrZeroValidation);

        Validation negativeValidation = new Validation(ValidationType.NEGATIVE);
        validationList.add(negativeValidation);

        Validation negativeOrZeroValidation = new Validation(ValidationType.NEGATIVE_OR_ZERO);
        validationList.add(negativeOrZeroValidation);

        Validation pastValidation = new Validation(ValidationType.PAST);
        validationList.add(pastValidation);

        Validation pastOrPresentValidation = new Validation(ValidationType.PAST_OR_PRESENT);
        validationList.add(pastOrPresentValidation);

        Validation futureValidation = new Validation(ValidationType.FUTURE);
        validationList.add(futureValidation);

        Validation futureOrPresentValidation = new Validation(ValidationType.FUTURE_OR_PRESENT);
        validationList.add(futureOrPresentValidation);

        Validation patternValidation = new Validation(ValidationType.PATTERN, "[^i*&2@]");
        validationList.add(patternValidation);

        Validation digitsValidation = new Validation(ValidationType.DIGITS, 2, 2);
        validationList.add(digitsValidation);

        Validation decimalMinValidation = new Validation(ValidationType.DECIMAL_MIN, BigDecimal.valueOf(0.1d), true);
        validationList.add(decimalMinValidation);

        Validation decimalMaxValidation = new Validation(ValidationType.DECIMAL_MAX, BigDecimal.valueOf(0.2d), true);
        validationList.add(decimalMaxValidation);

        validationsAttribute.setValidations(validationList);

        attributeList.add(validationsAttribute);

        return new Entity("TestEntityWithAttributeValidations", "tableName", attributeList);
    }

    public static Entity createEntitiesSequenceIdAttribute() {
        List<Attribute> attributeList = new ArrayList<>();

        Column column = new Column();
        column.setName("id_column");
        column.setSequence("SEQUENCE_NAME");
        column.setPrimaryKey(true);
        column.setAutogenerated(true);

        Attribute longAttribute = new Attribute("id", "Long");
        longAttribute.setColumns(Arrays.asList(column));

        attributeList.add(longAttribute);

        return new Entity("TestEntityWithSequenceIdAttribute", "tableName", attributeList);
    }

}
