package org.apiaddicts.apitools.apigen.generatorcore.generator.service;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData.AttributeData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RelationManagerBuilderTests {

    static TypeSpec spec;

    @BeforeAll
    static void init() {
        Map<String, AttributeData> attributes = new HashMap<>();
        attributes.put("simple", new AttributeData(false, "Simple", true, false, TypeName.get(String.class)));
        attributes.put("simpleTwo", new AttributeData(false, "Simple", true, false, TypeName.get(String.class)));
        attributes.put("list", new AttributeData(true, "ListItem", true, false, TypeName.get(String.class)));
        attributes.put("notOwned", new AttributeData(true, "Other", false, false, TypeName.get(String.class)));
        RelationManagerBuilder builder = new RelationManagerBuilder("EntityFirst", "the.pkg", attributes);

        spec = builder.build();
    }


    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenNameCorrect() {
        assertEquals("EntityFirstRelationManager", spec.name);
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenAnnotationsCorrect() {
        assertEquals("[@org.springframework.stereotype.Component]", spec.annotations.toString());
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenSuperclassCorrect() {
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.AbstractRelationsManager<the.pkg.entityfirst.EntityFirst>", spec.superclass.toString());
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenHasAllFields() {
        assertEquals(2, spec.fieldSpecs.size());

        FieldSpec fieldSpec;

        fieldSpec = spec.fieldSpecs.get(0);
        assertEquals("listItemService", fieldSpec.name);
        assertEquals("the.pkg.listitem.ListItemService", fieldSpec.type.toString());
        assertEquals("[@org.springframework.beans.factory.annotation.Autowired]", fieldSpec.annotations.toString());

        fieldSpec = spec.fieldSpecs.get(1);
        assertEquals("simpleService", fieldSpec.name);
        assertEquals("the.pkg.simple.SimpleService", fieldSpec.type.toString());
        assertEquals("[@org.springframework.beans.factory.annotation.Autowired]", fieldSpec.annotations.toString());
    }


    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenHasAllMethods() {
        assertEquals(8, spec.methodSpecs.size());
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenMainCreateMethodCorrect() {
        MethodSpec methodSpec = spec.methodSpecs.get(0);

        assertEquals("createOrRetrieveRelations", methodSpec.name);
        assertEquals("[@java.lang.Override, @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.MANDATORY)]",
                methodSpec.annotations.toString());
        assertEquals("[the.pkg.entityfirst.EntityFirst entityFirst]", methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
        assertEquals("" +
                        "org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrors errors = new org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrors();\n" +
                        "createOrRetrieveRelationsList(entityFirst, errors);\n" +
                        "createOrRetrieveRelationsSimple(entityFirst, errors);\n" +
                        "createOrRetrieveRelationsSimpleTwo(entityFirst, errors);\n" +
                        "if (!errors.isEmpty()) {\n" +
                        "  throw new org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrorsException(errors);\n" +
                        "}\n"
                , methodSpec.code.toString());
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenCreateMethodListCorrect() {
        MethodSpec methodSpec = spec.methodSpecs.get(1);

        assertEquals("createOrRetrieveRelationsList", methodSpec.name);
        assertEquals("[]", methodSpec.annotations.toString());
        assertEquals("[the.pkg.entityfirst.EntityFirst entityFirst, org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrors errors]", methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
        assertEquals("" +
                        "entityFirst.setList(createOrRetrieve(entityFirst.getList(), listItemService, errors));\n"
                , methodSpec.code.toString());
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenCreateMethodSingleCorrect() {
        MethodSpec methodSpec = spec.methodSpecs.get(3);

        assertEquals("createOrRetrieveRelationsSimpleTwo", methodSpec.name);
        assertEquals("[]", methodSpec.annotations.toString());
        assertEquals("[the.pkg.entityfirst.EntityFirst entityFirst, org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrors errors]", methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
        assertEquals("" +
                        "entityFirst.setSimpleTwo(createOrRetrieve(entityFirst.getSimpleTwo(), simpleService, errors));\n"
                , methodSpec.code.toString());
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenMainUpdateMethodCorrect() {
        MethodSpec methodSpec = spec.methodSpecs.get(4);

        assertEquals("updateRelations", methodSpec.name);
        assertEquals("[@java.lang.Override, @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.MANDATORY)]",
                methodSpec.annotations.toString());
        assertEquals("[the.pkg.entityfirst.EntityFirst persistedEntityFirst, the.pkg.entityfirst.EntityFirst entityFirst, java.util.Set<java.lang.String> fields]", methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
        assertEquals("" +
                        "org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrors errors = new org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrors();\n" +
                        "boolean updateAll = (fields == null);\n" +
                        "if (updateAll || fields.contains(\"list\")) {\n" +
                          "  updateRelationsList(persistedEntityFirst, entityFirst, fields, errors);\n" +
                        "}\n" +
                        "if (updateAll || fields.contains(\"simple\")) {\n" +
                        "  updateRelationsSimple(persistedEntityFirst, entityFirst, fields, errors);\n" +
                        "}\n" +
                        "if (updateAll || fields.contains(\"simpleTwo\")) {\n" +
                        "  updateRelationsSimpleTwo(persistedEntityFirst, entityFirst, fields, errors);\n" +
                        "}\n" +
                        "if (!errors.isEmpty()) {\n" +
                        "  throw new org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrorsException(errors);\n" +
                        "}\n"
                , methodSpec.code.toString());
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenUpdateMethodListCorrect() {
        MethodSpec methodSpec = spec.methodSpecs.get(5);

        assertEquals("updateRelationsList", methodSpec.name);
        assertEquals("[]", methodSpec.annotations.toString());
        assertEquals("[the.pkg.entityfirst.EntityFirst persistedEntityFirst, the.pkg.entityfirst.EntityFirst entityFirst, java.util.Set<java.lang.String> fields, org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrors errors]", methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
        assertEquals("" +
                        "persistedEntityFirst.getList().clear();\n" +
                        "persistedEntityFirst.getList().addAll(retrieve(entityFirst.getList(), listItemService, errors));\n"
                , methodSpec.code.toString());
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenUpdateMethodSingleCorrect() {
        MethodSpec methodSpec = spec.methodSpecs.get(7);

        assertEquals("updateRelationsSimpleTwo", methodSpec.name);
        assertEquals("[]", methodSpec.annotations.toString());
        assertEquals("[the.pkg.entityfirst.EntityFirst persistedEntityFirst, the.pkg.entityfirst.EntityFirst entityFirst, java.util.Set<java.lang.String> fields, org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrors errors]", methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
        assertEquals("" +
                        "persistedEntityFirst.setSimpleTwo(retrieve(entityFirst.getSimpleTwo(), simpleService, errors));\n"
                , methodSpec.code.toString());
    }

    @Test
    void givenEntityName_whenGetTypeName_thenCorrect() {
        TypeName typeName = RelationManagerBuilder.getTypeName("EntityName", "the.pkg");
        assertEquals("the.pkg.entityname.EntityNameRelationManager", typeName.toString());
    }
}
