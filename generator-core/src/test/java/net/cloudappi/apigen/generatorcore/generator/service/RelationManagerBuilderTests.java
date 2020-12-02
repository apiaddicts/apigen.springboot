package net.cloudappi.apigen.generatorcore.generator.service;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntitiesData.AttributeData;
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
        assertEquals("net.cloudappi.apigen.archetypecore.core.AbstractRelationsManager<the.pkg.entityfirst.EntityFirst>", spec.superclass.toString());
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
        assertEquals(6, spec.methodSpecs.size());
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
                        "net.cloudappi.apigen.archetypecore.exceptions.RelationalErrors errors = new net.cloudappi.apigen.archetypecore.exceptions.RelationalErrors();\n" +
                        "if (entityFirst.getList() != null) {\n" +
                        "  entityFirst.setList(entityFirst.getList().stream().map(e -> createOrRetrieve(e, errors)).collect(java.util.stream.Collectors.toSet()));\n" +
                        "}\n" +
                        "if (entityFirst.getSimple() != null) {\n" +
                        "  entityFirst.setSimple(createOrRetrieve(entityFirst.getSimple(), errors));\n" +
                        "}\n" +
                        "if (entityFirst.getSimpleTwo() != null) {\n" +
                        "  entityFirst.setSimpleTwo(createOrRetrieve(entityFirst.getSimpleTwo(), errors));\n" +
                        "}\n" +
                        "if (!errors.isEmpty()) {\n" +
                        "  throw new net.cloudappi.apigen.archetypecore.exceptions.RelationalErrorsException(errors);\n" +
                        "}\n"
                , methodSpec.code.toString());
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenMainUpdateMethodCorrect() {
        MethodSpec methodSpec = spec.methodSpecs.get(1);

        assertEquals("updateRelations", methodSpec.name);
        assertEquals("[@java.lang.Override, @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.MANDATORY)]",
                methodSpec.annotations.toString());
        assertEquals("[the.pkg.entityfirst.EntityFirst persistedEntityFirst, the.pkg.entityfirst.EntityFirst entityFirst, java.util.Set<java.lang.String> fields]", methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
        assertEquals("" +
                        "net.cloudappi.apigen.archetypecore.exceptions.RelationalErrors errors = new net.cloudappi.apigen.archetypecore.exceptions.RelationalErrors();\n" +
                        "boolean updateAll = (fields == null);\n" +
                        "if (updateAll || fields.contains(\"list\")) {\n" +
                        "  if (entityFirst.getList() != null) {\n" +
                        "    persistedEntityFirst.getList().clear();\n" +
                        "    persistedEntityFirst.getList().addAll(entityFirst.getList().stream().map(e -> retrieve(e, errors)).collect(java.util.stream.Collectors.toSet()));\n" +
                        "  } else {\n" +
                        "    persistedEntityFirst.setList(null);\n" +
                        "  }\n" +
                        "}\n" +
                        "if (updateAll || fields.contains(\"simple\")) {\n" +
                        "  if (entityFirst.getSimple() != null) {\n" +
                        "    persistedEntityFirst.setSimple(retrieve(entityFirst.getSimple(), errors));\n" +
                        "  } else {\n" +
                        "    persistedEntityFirst.setSimple(null);\n" +
                        "  }\n" +
                        "}\n" +
                        "if (updateAll || fields.contains(\"simpleTwo\")) {\n" +
                        "  if (entityFirst.getSimpleTwo() != null) {\n" +
                        "    persistedEntityFirst.setSimpleTwo(retrieve(entityFirst.getSimpleTwo(), errors));\n" +
                        "  } else {\n" +
                        "    persistedEntityFirst.setSimpleTwo(null);\n" +
                        "  }\n" +
                        "}\n" +
                        "if (!errors.isEmpty()) {\n" +
                        "  throw new net.cloudappi.apigen.archetypecore.exceptions.RelationalErrorsException(errors);\n" +
                        "}\n"
                , methodSpec.code.toString());
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenCreateSubMethodCorrect() {
        MethodSpec methodSpec = spec.methodSpecs.get(2);
        assertEquals("createOrRetrieve", methodSpec.name);
        assertEquals("[the.pkg.listitem.ListItem listItem, net.cloudappi.apigen.archetypecore.exceptions.RelationalErrors errors]", methodSpec.parameters.toString());
        assertEquals("the.pkg.listitem.ListItem", methodSpec.returnType.toString());
        assertEquals("" +
                        "if (listItem.isReference()) {\n" +
                        "  return retrieve(listItem, errors);\n" +
                        "} else {\n" +
                        "  try {\n" +
                        "    return listItemService.create(listItem);\n" +
                        "  } catch (net.cloudappi.apigen.archetypecore.exceptions.RelationalErrorsException e) {\n" +
                        "    errors.merge(e.getRelationalErrors());\n" +
                        "    return null;\n" +
                        "  }\n" +
                        "}\n"
                , methodSpec.code.toString());
    }

    @Test
    void givenValidRelationManagerBuilder_whenBuild_thenUpdateSubMethodCorrect() {
        MethodSpec methodSpec = spec.methodSpecs.get(4);
        assertEquals("retrieve", methodSpec.name);
        assertEquals("[the.pkg.listitem.ListItem listItem, net.cloudappi.apigen.archetypecore.exceptions.RelationalErrors errors]", methodSpec.parameters.toString());
        assertEquals("the.pkg.listitem.ListItem", methodSpec.returnType.toString());
        assertEquals("" +
                        "the.pkg.listitem.ListItem retrieved = (listItem.getId() == null) ? null : listItemService.getOne(listItem.getId()).orElse(null);\n" +
                        "if (retrieved == null) errors.register(the.pkg.listitem.ListItem.class, listItem.getId());\n" +
                        "return retrieved;\n"
                , methodSpec.code.toString());
    }

    @Test
    void givenEntityName_whenGetTypeName_thenCorrect() {
        TypeName typeName = RelationManagerBuilder.getTypeName("EntityName", "the.pkg");
        assertEquals("the.pkg.entityname.EntityNameRelationManager", typeName.toString());
    }
}
