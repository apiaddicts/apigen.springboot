package net.cloudappi.apigen.generatorcore.generator.web.resource.output;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import net.cloudappi.apigen.generatorcore.config.controller.EndpointBaseResponseObjectMother;
import net.cloudappi.apigen.generatorcore.config.controller.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityOutputResourceBuilderTest {

    @Test
    void givenAResponseWithAttribute_whenGenerateResource_thenFileStructureIsCorrect() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithSimpleAttribute("EntityName");
        EntityOutputResourceBuilder builder = new EntityOutputResourceBuilder(response, "the.base.package");
        TypeSpec spec = builder.build();
        assertEquals("EntityNameOutResource", spec.name, "The name is wrong");
        assertEquals("CLASS", spec.kind.name(), "The file is not a class");
        assertFalse(spec.annotations.isEmpty(), "The annotations are wrong");
        assertEquals("@lombok.Data", spec.annotations.get(0).toString(), "The @Data annotation is wrong");
        assertEquals("@net.cloudappi.apigen.archetypecore.core.resource.ApigenEntityOutResource", spec.annotations.get(1).toString(), "The @ApigenEntityOutResource annotation is wrong");
        assertFalse(spec.fieldSpecs.isEmpty(), "The fields are wrong");
    }

    @Test
    void givenAResponseWithoutAttributes_whenGenerateResource_thenClassHasNoFields() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithoutAttributes("EntityName");
        EntityOutputResourceBuilder builder = new EntityOutputResourceBuilder(response, "the.base.package");
        TypeSpec spec = builder.build();
        assertTrue(spec.fieldSpecs.isEmpty(), "There should not be any attribute");
    }

    @Test
    void givenAResponseWithSimpleAttribute_whenGenerateResource_thenFieldIsCorrect() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithSimpleAttribute("EntityName");
        EntityOutputResourceBuilder builder = new EntityOutputResourceBuilder(response, "the.base.package");
        TypeSpec spec = builder.build();
        assertFalse(spec.fieldSpecs.isEmpty(), "The fields are wrong");
        assertEquals("entityField", spec.fieldSpecs.get(0).name, "The field name is wrong");
        assertEquals("java.lang.String", spec.fieldSpecs.get(0).type.toString(), "The field type is wrong");
        assertEquals("@com.fasterxml.jackson.annotation.JsonProperty(\"jsonField\")", spec.fieldSpecs.get(0).annotations.get(0).toString(), "The field annotation is wrong");
    }

    @Test
    void givenAResponseWithRelatedAttribute_whenGenerateResource_thenFieldIsCorrect() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithRelatedAttribute("EntityName", "OtherEntity");
        EntityOutputResourceBuilder builder = new EntityOutputResourceBuilder(response, "the.base.package");
        TypeSpec spec = builder.build();
        assertFalse(spec.fieldSpecs.isEmpty(), "The fields are wrong");
        assertEquals("relatedEntityField", spec.fieldSpecs.get(0).name, "The field name is wrong");
        assertEquals("the.base.package.otherentity.web.OtherEntityOutResource", spec.fieldSpecs.get(0).type.toString(), "The field type is wrong");
        assertEquals("@com.fasterxml.jackson.annotation.JsonProperty(\"relatedJsonField\")", spec.fieldSpecs.get(0).annotations.get(0).toString(), "The field annotation is wrong");
    }

    @Test
    void givenAResponseWithRelatedListAttribute_whenGenerateResource_thenFieldIsCorrect() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithRelatedListAttribute("EntityName", "OtherEntity");
        EntityOutputResourceBuilder builder = new EntityOutputResourceBuilder(response, "the.base.package");
        TypeSpec spec = builder.build();
        assertFalse(spec.fieldSpecs.isEmpty(), "The fields are wrong");
        assertEquals("relatedEntityListField", spec.fieldSpecs.get(0).name, "The field name is wrong");
        assertEquals("java.util.Set<the.base.package.otherentity.web.OtherEntityOutResource>", spec.fieldSpecs.get(0).type.toString(), "The field type is wrong");
        assertEquals("@com.fasterxml.jackson.annotation.JsonProperty(\"relatedArrayJsonField\")", spec.fieldSpecs.get(0).annotations.get(0).toString(), "The field annotation is wrong");
    }

    @Test
    void givenAValidResponse_whenGenerateResource_thenPackageIsCorrect() {
        Response response = EndpointBaseResponseObjectMother.simpleResponseWithoutAttributes("EntityName");
        EntityOutputResourceBuilder builder = new EntityOutputResourceBuilder(response, "the.base.package");
        assertEquals("the.base.package.entityname.web", builder.getPackage(), "The package is wrong");
    }

    @Test
    void givenAValidResponse_whenGenerateResource_thenTypeNameIsCorrect() {
        TypeName typeName = EntityOutputResourceBuilder.getTypeName("EntityName", "the.base.package");
        assertEquals("the.base.package.entityname.web.EntityNameOutResource", typeName.toString(), "TypeName is wrong");
    }
}
