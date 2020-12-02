package net.cloudappi.apigen.generatorcore.generator.web.resource.output;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import net.cloudappi.apigen.generatorcore.config.controller.*;
import net.cloudappi.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import net.cloudappi.apigen.generatorcore.generator.web.controller.endpoints.AttributeObjectMother;
import net.cloudappi.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceOutputResourceBuilderTest {

    private static String basePackage = "the.base.package";
    private static Mapping rootMapping;
    private static Endpoint endpoint;
    private static Response response;

    @BeforeEach
    void init() {
        endpoint = EndpointObjectMother.customEndpoint();
        response = endpoint.getResponse();
        response.setIsCollection(false);
        response.setIsStandard(false);
        rootMapping = new Mapping("/resource");
    }

    @Test
    void givenAResponseWithAttribute_whenGenerateResource_thenFileStructureIsCorrect() {
        ResourceOutputResourceBuilder builder = new ResourceOutputResourceBuilder(rootMapping, endpoint, basePackage);
        TypeSpec spec = builder.build();
        assertEquals("CreateResourceEndpointOutResource", spec.name);
        assertEquals("CLASS", spec.kind.name());
        assertFalse(spec.annotations.isEmpty());
        assertEquals("@lombok.Data", spec.annotations.get(0).toString());
    }

    @Test
    void givenAResponseWithoutAttributes_whenGenerateResource_thenClassHasNoFields() {
        ResourceOutputResourceBuilder builder = new ResourceOutputResourceBuilder(rootMapping, endpoint, basePackage);
        TypeSpec spec = builder.build();
        assertTrue(spec.fieldSpecs.isEmpty());
    }

    @Test
    void givenAResponseWithSimpleAttribute_whenGenerateResource_thenFieldIsCorrect() {
        response.getAttributes().add(AttributeObjectMother.createSimpleStringAttribute("test"));
        ResourceOutputResourceBuilder builder = new ResourceOutputResourceBuilder(rootMapping, endpoint, basePackage);
        TypeSpec spec = builder.build();
        assertFalse(spec.fieldSpecs.isEmpty());
        assertEquals("test", spec.fieldSpecs.get(0).name);
        assertEquals("java.lang.String", spec.fieldSpecs.get(0).type.toString());
        assertEquals("@com.fasterxml.jackson.annotation.JsonProperty(\"test\")", spec.fieldSpecs.get(0).annotations.get(0).toString());
    }

    @Test
    void givenAResponseWithNestedAttribute_whenGenerateResource_thenFieldIsCorrect() {
        Attribute nested = AttributeObjectMother.createSimpleStringAttribute("name");
        Attribute object = AttributeObjectMother.createSimpleAttribute("object", "jsonObj", "obj");
        object.getAttributes().add(nested);
        response.getAttributes().add(object);

        ResourceOutputResourceBuilder builder = new ResourceOutputResourceBuilder(rootMapping, endpoint, basePackage);
        TypeSpec spec = builder.build();
        assertFalse(spec.fieldSpecs.isEmpty());
        assertEquals("obj", spec.fieldSpecs.get(0).name);
        assertEquals("the.base.package.resource.web.CreateResourceEndpointOutResource.Obj", spec.fieldSpecs.get(0).type.toString());
        assertEquals("@com.fasterxml.jackson.annotation.JsonProperty(\"jsonObj\")", spec.fieldSpecs.get(0).annotations.get(0).toString());
    }

    @Test
    void givenAResponseWithNestedListAttribute_whenGenerateResource_thenFieldIsCorrect() {
        Attribute nested = AttributeObjectMother.createSimpleStringAttribute("name");
        Attribute object = AttributeObjectMother.createSimpleAttribute("object", "jsonObj", "obj");
        object.getAttributes().add(nested);
        object.setCollection(true);
        response.getAttributes().add(object);

        ResourceOutputResourceBuilder builder = new ResourceOutputResourceBuilder(rootMapping, endpoint, basePackage);
        TypeSpec spec = builder.build();
        assertFalse(spec.fieldSpecs.isEmpty());
        assertEquals("obj", spec.fieldSpecs.get(0).name);
        assertEquals("java.util.Set<the.base.package.resource.web.CreateResourceEndpointOutResource.Obj>", spec.fieldSpecs.get(0).type.toString());
        assertEquals("@com.fasterxml.jackson.annotation.JsonProperty(\"jsonObj\")", spec.fieldSpecs.get(0).annotations.get(0).toString());
    }

    @Test
    void givenAValidResponse_whenGenerateResource_thenPackageIsCorrect() {
        ResourceOutputResourceBuilder builder = new ResourceOutputResourceBuilder(rootMapping, endpoint, basePackage);
        assertEquals("the.base.package.resource.web", builder.getPackage());
    }

    @Test
    void givenAValidResponse_whenGenerateResource_thenTypeNameIsCorrect() {
        TypeName typeName = ResourceOutputResourceBuilder.getTypeName(rootMapping, endpoint, basePackage);
        assertEquals("the.base.package.resource.web.CreateResourceEndpointOutResource", typeName.toString());
    }
}
