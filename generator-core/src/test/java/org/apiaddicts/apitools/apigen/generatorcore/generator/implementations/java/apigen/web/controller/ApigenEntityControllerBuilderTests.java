package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.ControllerObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApigenEntityControllerBuilderTests {

    private static TypeSpec generatedController;

    @BeforeAll
    static void prepareTest() {
        Controller controller = ControllerObjectMother.createControllerWithStandardEndpoints("EntityName", "/mapping");
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        Configuration cfg = ConfigurationObjectMother.create();
        ApigenEntityControllerBuilder<ApigenContext>
                controllerBuilder = new ApigenEntityControllerBuilder<>(controller, ctx, cfg);
        generatedController = controllerBuilder.build();
    }

    @Test
    void givenValidController_whenBuildController_thenFileStructureIsCorrect() {
        assertEquals(Modifier.PUBLIC.toString(), generatedController.modifiers.toArray()[0].toString(), "Public modifier is wrong");
        assertEquals("CLASS", generatedController.kind.name(), "Class declaration is wrong");
        assertEquals("EntityNameController", generatedController.name, "The name is wrong");
        assertEquals(5, generatedController.annotations.size(), "Number of annotations is wrong");
    }

    @Test
    void givenValidController_whenBuildController_thenAnnotationsAreCorrect() {
        assertEquals("@lombok.extern.slf4j.Slf4j",
                generatedController.annotations.get(0).toString(),
                "@Slf4j annotation is wrong");
        assertEquals("@org.springframework.web.bind.annotation.RestController",
                generatedController.annotations.get(1).toString(),
                "@RestController annotation is wrong");
        assertEquals("@org.springframework.web.bind.annotation.RequestMapping(\"/mapping\")",
                generatedController.annotations.get(2).toString(),
                "@RequestMapping annotation is wrong");
        assertEquals("@io.swagger.v3.oas.annotations.tags.Tag(name = \"EntityName\")",
                generatedController.annotations.get(3).toString(),
                "@Tag annotation is wrong");
        assertEquals("@org.springframework.validation.annotation.Validated",
                generatedController.annotations.get(4).toString(),
                "@Validated annotation is wrong");
    }

    @Test
    void givenValidController_whenBuildController_thenServiceFieldIsCorrect() {
        FieldSpec serviceFieldSpec = generatedController.fieldSpecs.get(0);
        assertEquals("service", serviceFieldSpec.name, "The service field name is wrong");
        assertEquals("the.group.artifact.entityname.EntityNameService", serviceFieldSpec.type.toString(), "The service field type is wrong");
        assertEquals("@org.springframework.beans.factory.annotation.Autowired", serviceFieldSpec.annotations.get(0).toString(), "The service field annotation is wrong");
    }

    @Test
    void givenValidController_whenBuildController_thenMapperFieldIsCorrect() {
        FieldSpec mapperFieldSpec = generatedController.fieldSpecs.get(1);
        assertEquals("mapper", mapperFieldSpec.name, "The mapper field name is wrong");
        assertEquals("the.group.artifact.entityname.EntityNameMapper", mapperFieldSpec.type.toString(), "The mapper field type is wrong");
        assertEquals("@org.springframework.beans.factory.annotation.Autowired", mapperFieldSpec.annotations.get(0).toString(), "The mapper field annotation is wrong");
    }

    @Test
    void givenValidController_whenBuildController_thenResourceNamingTranslatorFieldIsCorrect() {
        FieldSpec mapperFieldSpec = generatedController.fieldSpecs.get(2);
        assertEquals("namingTranslator", mapperFieldSpec.name, "The mapper field name is wrong");
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.resource.ResourceNamingTranslator", mapperFieldSpec.type.toString(), "The mapper field type is wrong");
        assertEquals("@org.springframework.beans.factory.annotation.Autowired", mapperFieldSpec.annotations.get(0).toString(), "The mapper field annotation is wrong");
    }

    @Test
    void givenValidControllerWithAllEndpoints_whenBuildController_thenAllEndpointsAreGenerated() {
        assertEquals(6, generatedController.methodSpecs.size(), "Not all endpoints have been generated");
    }

    @Test
    void givenValidController_whenBuildController_thenPackageIsCorrect() {
        Controller controller = ControllerObjectMother.createControllerWithStandardEndpoints("EntityName", "/mapping");
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        Configuration cfg = ConfigurationObjectMother.create();
        ApigenEntityControllerBuilder<ApigenContext>
                builder = new ApigenEntityControllerBuilder<>(controller, ctx, cfg);
        assertEquals("the.group.artifact.entityname.web", builder.getPackage(), "The package is wrong");
    }
}
