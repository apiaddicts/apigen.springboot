package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.persistence.repository;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

public class ApigenRepositoryBuilderTests {

    private static TypeSpec generatedRepository;

    @BeforeAll
    static void prepareTest() {
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        Mockito.when(entitiesData.getIDType(anyString())).thenReturn(TypeName.get(String.class));
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        Entity entity = EntityObjectMother.createSimpleEntityWithStringAsPrimaryKey();
        ApigenRepositoryBuilder<ApigenContext> repositoryBuilder = new ApigenRepositoryBuilder<>(entity, ctx,
                ConfigurationObjectMother.create());
        generatedRepository = repositoryBuilder.build();
    }

    @Test
    void givenAnEntityWithStringAsPrimaryKey_whenGenerateService_thenFileStructureIsCorrect() {
        assertEquals(Modifier.PUBLIC, generatedRepository.modifiers.toArray()[0], "Public modifier is wrong");
        assertEquals(TypeSpec.Kind.INTERFACE, generatedRepository.kind, "Interface declaration is wrong");
        assertEquals("SimpleTestEntityRepository", generatedRepository.name, "The name is wrong");
        assertEquals(1, generatedRepository.annotations.size(), "Number of annotations is wrong");
        assertEquals(0, generatedRepository.methodSpecs.size(), "Number of methods is wrong");
    }

    @Test
    void givenAnEntityWithStringAsPrimaryKey_whenGenerateRepository_thenAnnotationsAreCorrect() {
        assertEquals("@org.springframework.stereotype.Repository",
                generatedRepository.annotations.get(0).toString(), "Annotation is wrong");
    }

    @Test
    void givenAnEntityWithStringAsPrimaryKey_whenGenerateRepository_thenSuperinterfaceIsCorrect() {
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenRepository<the.group.artifact.simpletestentity.SimpleTestEntity, java.lang.String>",
                generatedRepository.superinterfaces.get(0).toString(), "Superinterface is wrong");
    }

    @Test
    void givenValidAttributes_whenAskForPackage_thenPackageIsCorrect() {
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        Mockito.when(entitiesData.getIDType(anyString())).thenReturn(TypeName.get(String.class));
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        Entity entity = EntityObjectMother.createSimpleEntityWithName();

        ApigenRepositoryBuilder<ApigenContext> repositoryBuilder = new ApigenRepositoryBuilder<>(entity, ctx, ConfigurationObjectMother.create());
        assertEquals("the.group.artifact.simpletestentity", repositoryBuilder.getPackage(), "The package is wrong");
    }

    @Test
    void givenValidAttributes_whenAskForTypeName_thenTypeNameIsCorrect() {
        TypeName typeName = ApigenRepositoryBuilder.getTypeName("EntityName", "the.base.package");
        assertEquals("the.base.package.entityname.EntityNameRepository", typeName.toString(), "TypeName is wrong");
    }
}
