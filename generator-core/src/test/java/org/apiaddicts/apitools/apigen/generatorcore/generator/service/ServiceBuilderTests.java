package org.apiaddicts.apitools.apigen.generatorcore.generator.service;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Attribute;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntityBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceBuilderTests {

    private static TypeSpec generatedService;

    @BeforeAll
    static void prepareTest() {
        Entity entity = EntityObjectMother.createSimpleEntityWithStringAsPrimaryKey();
        ServiceBuilder serviceBuilder = new ServiceBuilder(
                entity.getName(),
                "the.base.package",
                entity.getAttributes().stream().map(Attribute::getName).collect(Collectors.toSet()),
                EntityBuilder.getIDTypeName(entity, "the.base.package")
        );
        generatedService = serviceBuilder.build();
    }

    @Test
    void givenAnEntityWithStringAsPrimaryKey_whenGenerateService_thenFileStructureIsCorrect() {
        assertEquals(Modifier.PUBLIC, generatedService.modifiers.toArray()[0], "Public modifier is wrong");
        assertEquals(TypeSpec.Kind.CLASS, generatedService.kind, "Class declaration is wrong");
        assertEquals("SimpleTestEntityService", generatedService.name, "The name is wrong");
        assertEquals(2, generatedService.annotations.size(), "Number of annotations is wrong");
        assertEquals(2, generatedService.methodSpecs.size(), "Number of methods is wrong");
    }

    @Test
    void givenAnEntityWithStringAsPrimaryKey_whenGenerateService_thenAnnotationsAreCorrect() {
        assertEquals("@lombok.extern.slf4j.Slf4j",
                generatedService.annotations.get(0).toString(), "Annotation is wrong");
        assertEquals("@org.springframework.stereotype.Service",
                generatedService.annotations.get(1).toString(), "Annotation is wrong");
    }

    @Test
    void givenAnEntityWithStringAsPrimaryKey_whenGenerateService_thenConstructorMethodIsCorrect() {
        assertEquals("public Constructor(the.base.package.simpletestentity.SimpleTestEntityRepository repository,\n" +
                        "    @org.springframework.lang.Nullable org.apiaddicts.apitools.apigen.archetypecore.core.AbstractRelationsManager<the.base.package.simpletestentity.SimpleTestEntity> relationsManager,\n" +
                        "    @org.springframework.lang.Nullable org.apiaddicts.apitools.apigen.archetypecore.core.ApigenMapper<the.base.package.simpletestentity.SimpleTestEntity> mapper) {\n" +
                        "  super(repository, relationsManager, mapper);\n" +
                        "}\n",
                generatedService.methodSpecs.get(0).toString(), "Constructor Method is wrong");
    }

    @Test
    void givenAnEntity_whenGenerateService_thenUpdateBasicDataPartiallyMethodIsCorrect() {
        assertEquals("@java.lang.Override\n" +
                        "protected void updateBasicDataPartially(\n" +
                        "    the.base.package.simpletestentity.SimpleTestEntity persistedEntity,\n" +
                        "    the.base.package.simpletestentity.SimpleTestEntity entity,\n" +
                        "    java.util.Set<java.lang.String> fields) {\n" +
                        "  if (fields == null) {\n" +
                        "    mapper.updateBasicData(entity, persistedEntity);\n" +
                        "  } else {\n" +
                        "    if (fields.contains(\"id\")) persistedEntity.setId(entity.getId());\n" +
                        "  }\n" +
                        "}\n",
                generatedService.methodSpecs.get(1).toString());
    }

    @Test
    void givenValidAttributes_whenAskForPackage_thenPackageIsCorrect() {
        ServiceBuilder serviceBuilder = new ServiceBuilder("EntityName", "the.base.package", Collections.emptySet(), TypeName.get(String.class));
        assertEquals("the.base.package.entityname", serviceBuilder.getPackage(), "The package is wrong");
    }

    @Test
    void givenValidAttributes_whenAskForTypeName_thenTypeNameIsCorrect() {
        TypeName typeName = ServiceBuilder.getTypeName("EntityName", "the.base.package");
        assertEquals("the.base.package.entityname.EntityNameService", typeName.toString(), "TypeName is wrong");
    }

}
