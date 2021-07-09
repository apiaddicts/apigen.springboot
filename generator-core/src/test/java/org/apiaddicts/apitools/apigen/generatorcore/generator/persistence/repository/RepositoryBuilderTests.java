package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.repository;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntityBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepositoryBuilderTests {

    private static TypeSpec generatedRepository;

    @BeforeAll
    static void prepareTest() {
        Entity entity = EntityObjectMother.createSimpleEntityWithStringAsPrimaryKey();
        RepositoryBuilder repositoryBuilder = new RepositoryBuilder(entity.getName(), "the.base.package", EntityBuilder.getIDTypeName(entity, "the.base.package"));
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
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenRepository<the.base.package.simpletestentity.SimpleTestEntity, java.lang.String>",
                generatedRepository.superinterfaces.get(0).toString(), "Superinterface is wrong");
    }

    @Test
    void givenValidAttributes_whenAskForPackage_thenPackageIsCorrect() {
        RepositoryBuilder repositoryBuilder = new RepositoryBuilder("EntityName", "the.base.package", TypeName.get(String.class));
        assertEquals("the.base.package.entityname", repositoryBuilder.getPackage(), "The package is wrong");
    }

    @Test
    void givenValidAttributes_whenAskForTypeName_thenTypeNameIsCorrect() {
        TypeName typeName = RepositoryBuilder.getTypeName("EntityName", "the.base.package");
        assertEquals("the.base.package.entityname.EntityNameRepository", typeName.toString(), "TypeName is wrong");
    }
}
