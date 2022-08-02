package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.EndpointBaseResponseObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApigenEntityOutputResourceBuilderTest {

    @Test
    void givenAResponseWithAttribute_whenGenerateResource_thenFileStructureIsCorrect() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithSimpleAttribute("EntityName");
        Endpoint endpoint = new Endpoint();
        endpoint.setResponse(response);

        ApigenEntityOutputResourceBuilder<ApigenContext> builder = new ApigenEntityOutputResourceBuilder<>(endpoint, ApigenContextObjectMother.create(),
                ConfigurationObjectMother.create());
        TypeSpec spec = builder.build();
        assertEquals("EntityNameOutResource", spec.name, "The name is wrong");
        assertEquals("CLASS", spec.kind.name(), "The file is not a class");
        assertFalse(spec.annotations.isEmpty(), "The annotations are wrong");
        assertEquals("@lombok.Data", spec.annotations.get(0).toString(), "The @Data annotation is wrong");
        assertEquals("@org.apiaddicts.apitools.apigen.archetypecore.core.resource.ApigenEntityOutResource", spec.annotations.get(1).toString(), "The @ApigenEntityOutResource annotation is wrong");
        assertFalse(spec.fieldSpecs.isEmpty(), "The fields are wrong");
    }

    @Test
    void givenAResponseWithoutAttributes_whenGenerateResource_thenClassHasNoFields() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithoutAttributes("EntityName");
        Endpoint endpoint = new Endpoint();
        endpoint.setResponse(response);

        ApigenEntityOutputResourceBuilder<ApigenContext> builder = new ApigenEntityOutputResourceBuilder<>(endpoint, ApigenContextObjectMother.create(),
                ConfigurationObjectMother.create());
        TypeSpec spec = builder.build();
        assertTrue(spec.fieldSpecs.isEmpty(), "There should not be any attribute");
    }

    @Test
    void givenAResponseWithSimpleAttribute_whenGenerateResource_thenFieldIsCorrect() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithSimpleAttribute("EntityName");
        Endpoint endpoint = new Endpoint();
        endpoint.setResponse(response);

        ApigenEntityOutputResourceBuilder<ApigenContext> builder = new ApigenEntityOutputResourceBuilder<>(endpoint, ApigenContextObjectMother.create(),
                ConfigurationObjectMother.create());
        TypeSpec spec = builder.build();
        assertFalse(spec.fieldSpecs.isEmpty(), "The fields are wrong");
        assertEquals("entityField", spec.fieldSpecs.get(0).name, "The field name is wrong");
        assertEquals("java.lang.String", spec.fieldSpecs.get(0).type.toString(), "The field type is wrong");
        assertEquals("@com.fasterxml.jackson.annotation.JsonProperty(\"jsonField\")", spec.fieldSpecs.get(0).annotations.get(0).toString(), "The field annotation is wrong");
    }

    @Test
    void givenAResponseWithRelatedAttribute_whenGenerateResource_thenFieldIsCorrect() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithRelatedAttribute("EntityName", "OtherEntity");
        Endpoint endpoint = new Endpoint();
        endpoint.setResponse(response);

        ApigenEntityOutputResourceBuilder<ApigenContext> builder = new ApigenEntityOutputResourceBuilder<>(endpoint, ApigenContextObjectMother.create(),
                ConfigurationObjectMother.create());
        TypeSpec spec = builder.build();
        assertFalse(spec.fieldSpecs.isEmpty(), "The fields are wrong");
        assertEquals("relatedEntityField", spec.fieldSpecs.get(0).name, "The field name is wrong");
        assertEquals("the.group.artifact.otherentity.web.OtherEntityOutResource", spec.fieldSpecs.get(0).type.toString(), "The field type is wrong");
        assertEquals("@com.fasterxml.jackson.annotation.JsonProperty(\"relatedJsonField\")", spec.fieldSpecs.get(0).annotations.get(0).toString(), "The field annotation is wrong");
    }

    @Test
    void givenAResponseWithRelatedListAttribute_whenGenerateResource_thenFieldIsCorrect() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithRelatedListAttribute("EntityName", "OtherEntity");
        Endpoint endpoint = new Endpoint();
        endpoint.setResponse(response);

        ApigenEntityOutputResourceBuilder<ApigenContext> builder = new ApigenEntityOutputResourceBuilder<>(endpoint, ApigenContextObjectMother.create(),
                ConfigurationObjectMother.create());
        TypeSpec spec = builder.build();
        assertFalse(spec.fieldSpecs.isEmpty(), "The fields are wrong");
        assertEquals("relatedEntityListField", spec.fieldSpecs.get(0).name, "The field name is wrong");
        assertEquals("java.util.Set<the.group.artifact.otherentity.web.OtherEntityOutResource>", spec.fieldSpecs.get(0).type.toString(), "The field type is wrong");
        assertEquals("@com.fasterxml.jackson.annotation.JsonProperty(\"relatedArrayJsonField\")", spec.fieldSpecs.get(0).annotations.get(0).toString(), "The field annotation is wrong");
    }

    @Test
    void givenAValidResponse_whenGenerateResource_thenPackageIsCorrect() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithoutAttributes("EntityName");
        Endpoint endpoint = new Endpoint();
        endpoint.setResponse(response);

        ApigenEntityOutputResourceBuilder<ApigenContext> builder = new ApigenEntityOutputResourceBuilder<>(endpoint, ApigenContextObjectMother.create(),
                ConfigurationObjectMother.create());
        assertEquals("the.group.artifact.entityname.web", builder.getPackage(), "The package is wrong");
    }

    @Test
    void givenAValidResponse_whenGenerateResource_thenTypeNameIsCorrect() {
        TypeName typeName = ApigenEntityOutputResourceBuilder.getTypeName("EntityName", "the.group.artifact");
        assertEquals("the.group.artifact.entityname.web.EntityNameOutResource", typeName.toString(), "TypeName is wrong");
    }
}
