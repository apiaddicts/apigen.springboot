package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.service;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Attribute;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.lang.model.element.Modifier;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

class ApigenServiceBuilderTests {

    private static TypeSpec generatedService;
    private static ApigenServiceBuilder<ApigenContext> serviceBuilder;

    @BeforeAll
    static void prepareTest() {
        Entity entity = EntityObjectMother.createSimpleEntityWithStringAsPrimaryKey();
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        Mockito.when(entitiesData.getBasicAttributes(anyString())).thenReturn(Collections.emptySet());
        Mockito.when(entitiesData.getIDType(anyString())).thenReturn(TypeName.get(String.class));
        Mockito.when(entitiesData.getBasicAttributes(anyString()))
                .thenReturn(entity.getAttributes().stream().map(Attribute::getName).collect(Collectors.toSet()));
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        Configuration cfg = ConfigurationObjectMother.create(Collections.singletonList(entity), null);
        serviceBuilder = new ApigenServiceBuilder<>(entity, ctx, cfg);
        generatedService = serviceBuilder.build();
    }

    @Test
    void givenAnEntityWithStringAsPrimaryKey_whenGenerateService_thenFileStructureIsCorrect() {
        assertEquals(Modifier.PUBLIC, generatedService.modifiers.toArray()[0], "Public modifier is wrong");
        assertEquals(TypeSpec.Kind.CLASS, generatedService.kind, "Class declaration is wrong");
        assertEquals("SimpleTestEntityService", generatedService.name, "The name is wrong");
        assertEquals(2, generatedService.annotations.size(), "Number of annotations is wrong");
        assertEquals(1, generatedService.methodSpecs.size(), "Number of methods is wrong");
    }

    @Test
    void givenAnEntityWithStringAsPrimaryKey_whenGenerateService_thenAnnotationsAreCorrect() {
        assertEquals("@lombok.extern.slf4j.Slf4j", generatedService.annotations.get(0).toString(),
                "Annotation is wrong");
        assertEquals("@org.springframework.stereotype.Service", generatedService.annotations.get(1).toString(),
                "Annotation is wrong");
    }

    @Test
    void givenAnEntityWithStringAsPrimaryKey_whenGenerateService_thenConstructorMethodIsCorrect() {
        assertEquals(
                "public Constructor(the.group.artifact.simpletestentity.SimpleTestEntityRepository repository,\n" + "  " +
                        "  @org.springframework.lang.Nullable org.apiaddicts.apitools.apigen.archetypecore.core" +
                        ".AbstractRelationsManager<the.group.artifact.simpletestentity.SimpleTestEntity> " +
                        "relationsManager,\n" + "    @org.springframework.lang.Nullable org.apiaddicts.apitools" +
                        ".apigen.archetypecore.core.ApigenMapper<the.group.artifact.simpletestentity.SimpleTestEntity> " +
                        "mapper) {\n" + "  super(repository, relationsManager, mapper);\n" + "}\n",
                generatedService.methodSpecs.get(0).toString(), "Constructor Method is wrong");
    }

    @Test
    void givenValidAttributes_whenAskForPackage_thenPackageIsCorrect() {
        assertEquals("the.group.artifact.simpletestentity", serviceBuilder.getPackage(), "The package is wrong");
    }

    @Test
    void givenValidAttributes_whenAskForTypeName_thenTypeNameIsCorrect() {
        TypeName typeName = ApigenServiceBuilder.getTypeName("EntityName", "the.group.artifact");
        assertEquals("the.group.artifact.entityname.EntityNameService", typeName.toString(), "TypeName is wrong");
    }

}
