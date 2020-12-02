package net.cloudappi.apigen.generatorcore.generator.web.controller;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import net.cloudappi.apigen.generatorcore.config.controller.Controller;
import net.cloudappi.apigen.generatorcore.config.controller.ControllerObjectMother;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntitiesData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityControllerBuilderTests {

    private static TypeSpec generatedController;

    @BeforeAll
    static void prepareTest() {
        Controller controller = ControllerObjectMother.createControllerWithStandardEndpoints("EntityName", "/mapping");
        EntitiesData entitiesData = Mockito.mock(EntitiesData.class);
        EntityControllerBuilder controllerBuilder = new EntityControllerBuilder(controller, entitiesData,"the.base.package");
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
        assertEquals("@io.swagger.annotations.Api(tags = \"EntityName\")",
                generatedController.annotations.get(3).toString(),
                "@Api annotation is wrong");
        assertEquals("@org.springframework.validation.annotation.Validated",
                generatedController.annotations.get(4).toString(),
                "@Validated annotation is wrong");
    }

    @Test
    void givenValidController_whenBuildController_thenServiceFieldIsCorrect() {
        FieldSpec serviceFieldSpec = generatedController.fieldSpecs.get(0);
        assertEquals("service", serviceFieldSpec.name, "The service field name is wrong");
        assertEquals("the.base.package.entityname.EntityNameService", serviceFieldSpec.type.toString(), "The service field type is wrong");
        assertEquals("@org.springframework.beans.factory.annotation.Autowired", serviceFieldSpec.annotations.get(0).toString(), "The service field annotation is wrong");
    }

    @Test
    void givenValidController_whenBuildController_thenMapperFieldIsCorrect() {
        FieldSpec mapperFieldSpec = generatedController.fieldSpecs.get(1);
        assertEquals("mapper", mapperFieldSpec.name, "The mapper field name is wrong");
        assertEquals("the.base.package.entityname.EntityNameMapper", mapperFieldSpec.type.toString(), "The mapper field type is wrong");
        assertEquals("@org.springframework.beans.factory.annotation.Autowired", mapperFieldSpec.annotations.get(0).toString(), "The mapper field annotation is wrong");
    }

    @Test
    void givenValidController_whenBuildController_thenResourceNamingTranslatorFieldIsCorrect() {
        FieldSpec mapperFieldSpec = generatedController.fieldSpecs.get(2);
        assertEquals("namingTranslator", mapperFieldSpec.name, "The mapper field name is wrong");
        assertEquals("net.cloudappi.apigen.archetypecore.core.resource.ResourceNamingTranslator", mapperFieldSpec.type.toString(), "The mapper field type is wrong");
        assertEquals("@org.springframework.beans.factory.annotation.Autowired", mapperFieldSpec.annotations.get(0).toString(), "The mapper field annotation is wrong");
    }

    @Test
    void givenValidControllerWithAllEndpoints_whenBuildController_thenAllEndpointsAreGenerated() {
        assertEquals(6, generatedController.methodSpecs.size(), "Not all endpoints have been generated");
    }

    @Test
    void givenValidController_whenBuildController_thenPackageIsCorrect() {
        Controller controller = ControllerObjectMother.createControllerWithStandardEndpoints("EntityName", "/mapping");
        EntitiesData entitiesData = Mockito.mock(EntitiesData.class);
        EntityControllerBuilder builder = new EntityControllerBuilder(controller, entitiesData, "the.base.package");
        assertEquals("the.base.package.entityname.web", builder.getPackage(), "The package is wrong");
    }
}
