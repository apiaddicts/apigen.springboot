package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractClassBuilder;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

// TODO #14908 refactor to avoid validate full generation
class EntitiesGeneratorTest {

    private static final String BASE_PACKAGE = "org.test";

    @Test
    void generateSingleEntityTest() {
        Entity simpleTestEntity = EntityObjectMother.createSimpleEntityWithName();
        List<Entity> simpleEntitiesList = new ArrayList<>();
        simpleEntitiesList.add(simpleTestEntity);

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(simpleEntitiesList, BASE_PACKAGE);

        assertEquals(1, getGeneratedEntities(entitiesGenerator).size());
    }

    @Test
    void checkGeneratedEntityTest() {
        Entity simpleTestEntity = EntityObjectMother.createSimpleEntityWithName();
        List<Entity> simpleEntitiesList = new ArrayList<>();
        simpleEntitiesList.add(simpleTestEntity);

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(simpleEntitiesList, BASE_PACKAGE);

        assertEquals(simpleTestEntity.getName(), getGeneratedEntities(entitiesGenerator).get(simpleTestEntity.getName()).name);
    }

    @Test
    void generateEntityWith_Simple_Attributes() {
        Entity testEntityWithSimpleAttributes = EntityObjectMother.createEntityWithSimpleAttributes();
        List<Entity> testEntitiesList = new ArrayList<>();
        testEntitiesList.add(testEntityWithSimpleAttributes);

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(testEntitiesList, BASE_PACKAGE);

        TypeSpec generatedEntity = getGeneratedEntities(entitiesGenerator).get(testEntityWithSimpleAttributes.getName());

        assertEquals(testEntityWithSimpleAttributes.getName(), generatedEntity.name, "Checking if Entity name matches given name:");

        assertEquals(2, generatedEntity.fieldSpecs.size(), "Checking if Entity contains the given number of fields:");

        assertTrue(generatedEntity.annotations.contains(AnnotationSpec.builder(Getter.class).build()), "Checking if Entity contains @Getter annotation:");
        assertTrue(generatedEntity.annotations.contains(AnnotationSpec.builder(Setter.class).build()), "Checking if Entity contains @Setter annotation:");

        assertTrue(generatedEntity.annotations.contains(AnnotationSpec.builder(javax.persistence.Entity.class).build()), "Checking if Entity contains @Entity annotation:");

        assertTrue(generatedEntity.annotations.contains(AnnotationSpec.builder(javax.persistence.Table.class).addMember("name", "$S", "tableName").build()), "Checking if Entity contains @Table annotation:");
        assertTrue(generatedEntity.annotations.contains(AnnotationSpec.builder(NoArgsConstructor.class).build()), "Checking if Entity contains @NoArgsConstructor annotation:");
    }

    @Test
    void generateEntityWith_OneToMany_Empty_MappedBy() {
        Entity testEntityWithComplexAttributes = EntityObjectMother.createEntityWithOneToManyAttributes("");
        List<Entity> testEntitiesList = new ArrayList<>();
        testEntitiesList.add(testEntityWithComplexAttributes);
        testEntitiesList.add(EntityObjectMother.createSimpleEntityWithName());

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(testEntitiesList, BASE_PACKAGE);

        TypeSpec generatedEntity = getGeneratedEntities(entitiesGenerator).get(testEntityWithComplexAttributes.getName());

        assertEquals(testEntityWithComplexAttributes.getName(), generatedEntity.name, "Checking if Entity name matches given name:");

        assertEquals(3, generatedEntity.fieldSpecs.size(), "Checking if Entity contains the given number of fields:");

        assertTrue(generatedEntity.annotations.contains(AnnotationSpec.builder(Getter.class).build()), "Checking if Entity contains @Getter annotation:");
        assertTrue(generatedEntity.annotations.contains(AnnotationSpec.builder(Setter.class).build()), "Checking if Entity contains @Setter annotation:");

        assertTrue(generatedEntity.annotations.contains(AnnotationSpec.builder(javax.persistence.Entity.class).build()), "Checking if Entity contains @Entity annotation:");

        assertTrue(generatedEntity.annotations.contains(AnnotationSpec.builder(javax.persistence.Table.class).addMember("name", "$S", "tableName").build()), "Checking if Entity contains @Table annotation:");

        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(AnnotationSpec.builder(Id.class).build()), "Checking if Entity contains @Id attribute:");

        assertTrue(generatedEntity.fieldSpecs.get(2).annotations.contains(AnnotationSpec.builder(OneToMany.class).addMember("mappedBy", "$S", "").build()), "Checking if Entity contains @OneToMany attribute:");
    }

    @Test
    void generateEntityWith_OneToMany_ManyToOne_Relation() {
        List<Entity> testEntitiesList = EntityObjectMother.createEntitiesWithOneToManyAndManyToOneAttributes();

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(testEntitiesList, BASE_PACKAGE);

        TypeSpec generatedEntityOneToMany = getGeneratedEntities(entitiesGenerator).get(testEntitiesList.get(0).getName());
        TypeSpec generatedEntityManyToOne = getGeneratedEntities(entitiesGenerator).get(testEntitiesList.get(1).getName());


        assertEquals(testEntitiesList.get(0).getName(), generatedEntityOneToMany.name, "Checking if Entity name matches given name:");
        assertEquals(testEntitiesList.get(1).getName(), generatedEntityManyToOne.name, "Checking if Entity name matches given name:");

        assertTrue(generatedEntityOneToMany.fieldSpecs.get(2).annotations.contains(AnnotationSpec.builder(OneToMany.class).addMember("mappedBy", "$S", "manyToOneRelationAttribute").build()), "Checking if Entity1 contains @OneToMany annotation:");

        assertTrue(generatedEntityManyToOne.fieldSpecs.get(0).annotations.contains(AnnotationSpec.builder(ManyToOne.class).build()), "Checking if Entity2 contains @ManyToOne annotation:");
        assertTrue(generatedEntityManyToOne.fieldSpecs.get(0).annotations.contains(AnnotationSpec.builder(JoinColumn.class).addMember("name", "$S", "manyToOneRelationColumn").build()), "Checking if Entity2 contains @JoinColumn annotation:");
    }

    @Test
    void generateEntityWith_ManyToMany_Relation() {
        List<Entity> testEntitiesList = EntityObjectMother.createEntitiesWithManyToManyAttributes();

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(testEntitiesList, BASE_PACKAGE);

        TypeSpec generatedEntityManyToManyOwner = getGeneratedEntities(entitiesGenerator).get(testEntitiesList.get(0).getName());
        TypeSpec generatedEntityManyToMany = getGeneratedEntities(entitiesGenerator).get(testEntitiesList.get(1).getName());


        assertEquals(testEntitiesList.get(0).getName(), generatedEntityManyToManyOwner.name, "Checking if Entity name matches given name:");
        assertEquals(testEntitiesList.get(1).getName(), generatedEntityManyToMany.name, "Checking if Entity name matches given name:");

        assertEquals("@javax.persistence.ManyToMany", generatedEntityManyToManyOwner.fieldSpecs.get(0).annotations.get(0).toString());
        assertEquals("" +
                "@javax.persistence.JoinTable(" +
                "name = \"intermediateTableName\", " +
                "joinColumns = @javax.persistence.JoinColumn(name = \"column_1_id\"), " +
                "inverseJoinColumns = @javax.persistence.JoinColumn(name = \"column_2_id\"), " +
                "uniqueConstraints = @javax.persistence.UniqueConstraint(columnNames = {\"column_1_id\", \"column_2_id\"})" +
                ")", generatedEntityManyToManyOwner.fieldSpecs.get(0).annotations.get(1).toString());
        assertTrue(generatedEntityManyToMany.fieldSpecs.get(0).annotations.contains(AnnotationSpec.builder(ManyToMany.class).addMember("mappedBy", "$S", "manyToManyOwnerRelationAttribute").build()), "Checking if Entity2 contains @ManyToMany annotation:");
    }

    @Test
    void generateEntityWith_OneToOne_Relation() {
        List<Entity> testEntitiesList = EntityObjectMother.createEntitiesWithOneToOneAttributes();

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(testEntitiesList, BASE_PACKAGE);

        TypeSpec generatedEntityOneToOneOwner = getGeneratedEntities(entitiesGenerator).get(testEntitiesList.get(0).getName());
        TypeSpec generatedEntityOneToOne = getGeneratedEntities(entitiesGenerator).get(testEntitiesList.get(1).getName());


        assertEquals(testEntitiesList.get(0).getName(), generatedEntityOneToOneOwner.name, "Checking if Entity name matches given name:");
        assertEquals(testEntitiesList.get(1).getName(), generatedEntityOneToOne.name, "Checking if Entity name matches given name:");

        assertTrue(generatedEntityOneToOneOwner.fieldSpecs.get(0).annotations.contains(AnnotationSpec.builder(OneToOne.class).build()), "Checking if Entity1 contains @OneToOne annotation:");
        assertTrue(generatedEntityOneToOneOwner.fieldSpecs.get(0).annotations.contains(AnnotationSpec.builder(JoinColumn.class).addMember("name", "$S", "oneToOneOwnerRelationColumn").build()), "Checking if Entity1 contains @JoinColumn annotation:");

        assertTrue(generatedEntityOneToOne.fieldSpecs.get(0).annotations.contains(AnnotationSpec.builder(OneToOne.class).addMember("mappedBy", "$S", "oneToOneOwnerRelationAttribute").build()), "Checking if Entity2 contains @OneToOne annotation:");
    }

    private TypeSpec setValidationTest() {
        Entity testEntityWithAttributeValidations = EntityObjectMother.createEntityWithAttributeValidations();
        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(Arrays.asList(testEntityWithAttributeValidations), BASE_PACKAGE);
        return getGeneratedEntities(entitiesGenerator).get(testEntityWithAttributeValidations.getName());
    }

    @Test
    void generateAttribute_NotNull_Validation() {
        TypeSpec generatedEntity = setValidationTest();

        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(NotNull.class)
                        .build()), "Checking if Attribute has @NotNull Annotation:"
        );
    }

    @Test
    void generateAttribute_Size_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(Size.class)
                        .addMember("min", "1")
                        .addMember("max", "2")
                        .build()),
                "Checking if Attribute has @Size Annotation:"
        );
    }

    @Test
    void generateAttribute_Min_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(Min.class)
                        .addMember("value", "1")
                        .build()), "Checking if Attribute has @Min Annotation:"
        );
    }

    @Test
    void generateAttribute_Max_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(Max.class)
                        .addMember("value", "2")
                        .build()), "Checking if Attribute has @Max Annotation:"
        );
    }

    @Test
    void generateAttribute_Email_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(Email.class)
                        .build()), "Checking if Attribute has @Email Annotation:"
        );
    }

    @Test
    void generateAttribute_NotEmtpy_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(NotEmpty.class)
                        .build()), "Checking if Attribute has @NotEmtpy Annotation:"
        );
    }

    @Test
    void generateAttribute_NotBlank_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(NotBlank.class)
                        .build()), "Checking if Attribute has @NotBlank Annotation:"
        );
    }

    @Test
    void generateAttribute_Positive_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(Positive.class)
                        .build()), "Checking if Attribute has @Positive Annotation:"
        );
    }

    @Test
    void generateAttribute_PositiveOrZero_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(PositiveOrZero.class)
                        .build()), "Checking if Attribute has @PositiveOrZero Annotation:"
        );
    }

    @Test
    void generateAttribute_Negative_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(Negative.class)
                        .build()), "Checking if Attribute has @Negative Annotation:"
        );
    }

    @Test
    void generateAttribute_NegativeOrZero_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(NegativeOrZero.class)
                        .build()), "Checking if Attribute has @NegativeOrZero Annotation:"
        );
    }

    @Test
    void generateAttribute_Past_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(Past.class)
                        .build()), "Checking if Attribute has @Past Annotation:"
        );
    }

    @Test
    void generateAttribute_PastOrPresent_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(PastOrPresent.class)
                        .build()), "Checking if Attribute has @PastOrPresent Annotation:"
        );
    }

    @Test
    void generateAttribute_Future_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(Future.class)
                        .build()), "Checking if Attribute has @Future Annotation:"
        );
    }

    @Test
    void generateAttribute_FutureOrPresent_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(FutureOrPresent.class)
                        .build()), "Checking if Attribute has @FutureOrPresent Annotation:"
        );
    }

    @Test
    void generateAttribute_Pattern_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(Pattern.class)
                        .addMember("regexp", "$S", "[^i*&2@]")
                        .build()), "Checking if Attribute has @Pattern Annotation:"
        );
    }

    @Test
    void generateAttribute_Digits_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(Digits.class)
                        .addMember("integer", "2")
                        .addMember("fraction", "2")
                        .build()), "Checking if Attribute has @Digits Annotation:"
        );
    }

    @Test
    void generateAttribute_DecimalMin_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(DecimalMin.class)
                        .addMember("value", "$S", "0.1")
                        .addMember("inclusive", "$L", true)
                        .build()), "Checking if Attribute has @DecimalMin Annotation:"
        );
    }

    @Test
    void generateAttribute_DecimalMax_Validation() {
        TypeSpec generatedEntity = setValidationTest();
        assertTrue(generatedEntity.fieldSpecs.get(0).annotations.contains(
                AnnotationSpec.builder(DecimalMax.class)
                        .addMember("value", "$S", "0.2")
                        .addMember("inclusive", "$L", true)
                        .build()), "Checking if Attribute has @DecimalMax Annotation:"
        );
    }

    @Test
    void generateEntityWith_Array_ManyToMany_Attribute() {
        List<Entity> testEntitiesList = EntityObjectMother.createEntitiesWithManyToManyAttributes();

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(testEntitiesList, BASE_PACKAGE);

        TypeSpec generatedEntityManyToManyOwner = getGeneratedEntities(entitiesGenerator).get(testEntitiesList.get(0).getName());

        String type = testEntitiesList.get(0).getAttributes().get(0).getType();

        assertEquals(ParameterizedTypeName.get(ClassName.get(Set.class), ClassName.get(concatPackage(BASE_PACKAGE, type), type)), generatedEntityManyToManyOwner.fieldSpecs.get(0).type, "Checking if attribute has Set field");
    }

    @Test
    void generateEntityWith_Array_OneToMany_Attribute() {
        List<Entity> testEntitiesList = EntityObjectMother.createEntitiesWithOneToManyAndManyToOneAttributes();

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(testEntitiesList, BASE_PACKAGE);

        TypeSpec generatedEntityOneToMany = getGeneratedEntities(entitiesGenerator).get(testEntitiesList.get(0).getName());

        String type = testEntitiesList.get(0).getAttributes().get(2).getType();

        assertEquals(ParameterizedTypeName.get(ClassName.get(Set.class), ClassName.get(concatPackage(BASE_PACKAGE, type), type)), generatedEntityOneToMany.fieldSpecs.get(2).type, "Checking if attribute has Set field");
    }

    @Test
    void generateEntityWith_GetAndSet_Id_Method() {
        Entity testEntity = EntityObjectMother.createEntityWithSequenceIdAttribute();

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(Arrays.asList(testEntity), BASE_PACKAGE);

        TypeSpec generatedEntity = getGeneratedEntities(entitiesGenerator).get(testEntity.getName());

        assertEquals("getId", generatedEntity.methodSpecs.get(1).name, "Checking if getId method exists");
        assertEquals("java.lang.Long", generatedEntity.methodSpecs.get(1).returnType.toString(), "Checking getId method return type");

        assertEquals("setId", generatedEntity.methodSpecs.get(2).name, "Checking if setId method exists");
        assertEquals("void", generatedEntity.methodSpecs.get(2).returnType.toString(), "Checking setId method return type");
    }

    @Test
    void generateEntityWith_ConstructorById() {
        Entity testEntity = EntityObjectMother.createEntityWithSequenceIdAttribute();

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(Arrays.asList(testEntity), BASE_PACKAGE);

        TypeSpec generatedEntity = getGeneratedEntities(entitiesGenerator).get(testEntity.getName());

        MethodSpec methodSpec = generatedEntity.methodSpecs.get(0);

        assertTrue(methodSpec.isConstructor(), "Method is not a constructor");
        assertEquals("<init>", methodSpec.name);
        assertTrue(methodSpec.annotations.isEmpty(), "Method has annotations");
        assertEquals("[java.lang.Long id]", methodSpec.parameters.toString());
        assertNull(methodSpec.returnType, "Method has return types");
        assertEquals("this.setId(id);\n", methodSpec.code.toString());
    }

    private String concatPackage(String basePackage, String newPackage) {
        return String.format("%s.%s", basePackage, newPackage.toLowerCase());
    }

    private Map<String, TypeSpec> getGeneratedEntities(EntitiesGenerator eg) {
        return eg.getBuilders().stream().map(AbstractClassBuilder::build).collect(Collectors.toMap(b -> b.name, b -> b));
    }

    @Test
    void givenAutogeneratedSequenceId_whenGenerated_thenSuccess() {
        Entity testEntity = EntityObjectMother.createEntityWithSequenceIdAttribute();

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(Arrays.asList(testEntity), BASE_PACKAGE);

        TypeSpec generatedEntity = getGeneratedEntities(entitiesGenerator).get(testEntity.getName());

        String actual = generatedEntity.fieldSpecs.get(0).annotations.toString();

        assertEquals("[" +
                "@javax.persistence.Id, " +
                "@javax.persistence.GeneratedValue(generator = \"SEQUENCE_NAME_name\"), " +
                "@javax.persistence.SequenceGenerator(name = \"SEQUENCE_NAME_name\", sequenceName = \"SEQUENCE_NAME\"), " +
                "@javax.persistence.Column(name = \"id_column\")" +
                "]", actual);
    }

    @Test
    void givenAutogeneratedNumericId_whenGenerated_thenSuccess() {
        Entity testEntity = EntityObjectMother.createEntityWithAutogeneratedLongId();

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(Arrays.asList(testEntity), BASE_PACKAGE);

        TypeSpec generatedEntity = getGeneratedEntities(entitiesGenerator).get(testEntity.getName());

        String actual = generatedEntity.fieldSpecs.get(0).annotations.toString();

        assertEquals("[" +
                "@javax.persistence.Id, " +
                "@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY), " +
                "@javax.persistence.Column(name = \"id\")" +
                "]", actual);
    }

    @Test
    void givenAutogeneratedStringId_whenGenerated_thenSuccess() {
        Entity testEntity = EntityObjectMother.createEntityWithAutogeneratedStringId();

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(Arrays.asList(testEntity), BASE_PACKAGE);

        TypeSpec generatedEntity = getGeneratedEntities(entitiesGenerator).get(testEntity.getName());

        String actual = generatedEntity.fieldSpecs.get(0).annotations.toString();

        assertEquals("[" +
                "@javax.persistence.Id, " +
                "@javax.persistence.GeneratedValue(generator = \"uuid\"), " +
                "@org.hibernate.annotations.GenericGenerator(name = \"uuid\", strategy = \"uuid2\"), " +
                "@javax.persistence.Column(name = \"id\")" +
                "]", actual);
    }

    @Test
    void givenNotAutogeneratedId_whenGenerated_thenSuccess() {
        Entity testEntity = EntityObjectMother.createEntityWithoutAutogeneratedId();

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(Arrays.asList(testEntity), BASE_PACKAGE);

        TypeSpec generatedEntity = getGeneratedEntities(entitiesGenerator).get(testEntity.getName());

        String actual = generatedEntity.fieldSpecs.get(0).annotations.toString();

        assertEquals("[@javax.persistence.Id, @javax.persistence.Column(name = \"id\")]", actual);
    }
}