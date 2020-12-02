package net.cloudappi.apigen.generatorcore.generator.web.resource.input;

import com.squareup.javapoet.TypeSpec;
import net.cloudappi.apigen.generatorcore.config.controller.Attribute;
import net.cloudappi.apigen.generatorcore.config.controller.Endpoint;
import net.cloudappi.apigen.generatorcore.utils.Mapping;
import net.cloudappi.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import net.cloudappi.apigen.generatorcore.generator.web.controller.endpoints.AttributeObjectMother;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AllInputResourceBuilderTests {

    private static String basePackage = "the.base.pkge";

    private static TypeSpec entityTypeSpec;
    private static TypeSpec entityNestedTypeSpec;
    private static TypeSpec resourceTypeSpec;


    @BeforeAll
    static void init() {
        initEntityTypeSpec();
        initEntityNestedIdTypeSpec();
        initResourceTypeSpec();
    }

    private static void initEntityTypeSpec() {
        Attribute attribute = AttributeObjectMother.createSimpleAttribute("string", "json", "java");

        Endpoint endpoint = EndpointObjectMother.customEndpoint();
        endpoint.setRelatedEntity("Entity");
        endpoint.getRequest().setAttributes(Collections.singletonList(attribute));

        AllInputResourceBuilder builder = new AllInputResourceBuilder(new Mapping("/entity"), endpoint, basePackage);
        entityTypeSpec = builder.build();
    }

    private static void initEntityNestedIdTypeSpec() {
        Attribute attribute = AttributeObjectMother.createSimpleAttribute("integer", "other", "other.id");
        attribute.setRelatedEntity("Other");

        Endpoint endpoint = EndpointObjectMother.customEndpoint();
        endpoint.setRelatedEntity("Entity");
        endpoint.getRequest().setAttributes(Collections.singletonList(attribute));

        AllInputResourceBuilder builder = new AllInputResourceBuilder(new Mapping("/entity"), endpoint, basePackage);
        entityNestedTypeSpec = builder.build();
    }

    private static void initResourceTypeSpec() {
        Attribute nested = AttributeObjectMother.createSimpleAttribute("string", "nested");
        Attribute attribute = AttributeObjectMother.createSimpleAttribute("object", "parent");
        attribute.getAttributes().add(nested);

        Endpoint endpoint = EndpointObjectMother.customEndpoint();
        endpoint.getRequest().setAttributes(Collections.singletonList(attribute));

        AllInputResourceBuilder builder = new AllInputResourceBuilder(new Mapping("/resource"), endpoint, basePackage);
        resourceTypeSpec = builder.build();
    }

    @Test
    void givenEntityEndpoint_whenGenerated_thenAnnotationsAreCorrect() {
        assertEquals("[@lombok.Data]", entityTypeSpec.annotations.toString());
    }

    @Test
    void givenEntityEndpoint_whenGenerated_thenNameIsCorrect() {
        assertEquals("CreateEntityEndpointResource", entityTypeSpec.name);
    }

    @Test
    void givenEntityEndpoint_whenGenerated_thenFieldsAreCorrect() {
        assertEquals(1, entityTypeSpec.fieldSpecs.size());
        assertEquals("" +
                        "@com.fasterxml.jackson.annotation.JsonProperty(\"json\")\n" +
                        "private java.lang.String java;\n",
                entityTypeSpec.fieldSpecs.get(0).toString());
    }

    @Test
    void givenEntityNestedIdEndpoint_whenGenerated_thenAnnotationsAreCorrect() {
        assertEquals("[@lombok.Data]", entityNestedTypeSpec.annotations.toString());
    }

    @Test
    void givenEntityNestedIdEndpoint_whenGenerated_thenNameIsCorrect() {
        assertEquals("CreateEntityEndpointResource", entityNestedTypeSpec.name);
    }

    @Test
    void givenEntityNestedIdEndpoint_whenGenerated_thenFieldsAreCorrect() {
        assertEquals(1, entityNestedTypeSpec.fieldSpecs.size());
        assertEquals("" +
                        "@com.fasterxml.jackson.annotation.JsonProperty(\"other\")\n" +
                        "@javax.validation.Valid\n" +
                        "private the.base.pkge.entity.web.CreateEntityEndpointResource.Other other;\n",
                entityNestedTypeSpec.fieldSpecs.get(0).toString());
    }

    @Test
    void givenEntityNestedIdEndpoint_whenGenerated_thenNestedClassIsCorrect() {
        assertEquals(1, entityNestedTypeSpec.typeSpecs.size());
        assertEquals("" +
                        "@lombok.Data\n" +
                        "@lombok.NoArgsConstructor\n" +
                        "public static class Other {\n" +
                        "  @com.fasterxml.jackson.annotation.JsonProperty(\"id\")\n" +
                        "  private java.lang.Long id;\n" +
                        "\n" +
                        "  @com.fasterxml.jackson.annotation.JsonCreator(\n" +
                        "      mode = JsonCreator.Mode.DELEGATING\n" +
                        "  )\n" +
                        "  Other(java.lang.Long id) {\n" +
                        "    this.id = id;\n" +
                        "  }\n" +
                        "}\n",
                entityNestedTypeSpec.typeSpecs.get(0).toString());
    }

    @Test
    void givenResourceEndpoint_whenGenerated_thenAnnotationsAreCorrect() {
        assertEquals("[@lombok.Data]", resourceTypeSpec.annotations.toString());
    }

    @Test
    void givenResourceEndpoint_whenGenerated_thenNameIsCorrect() {
        assertEquals("CreateResourceEndpointResource", resourceTypeSpec.name);
    }

    @Test
    void givenResourceEndpoint_whenGenerated_thenFieldsAreCorrect() {
        assertEquals(1, resourceTypeSpec.fieldSpecs.size());
        assertEquals("" +
                        "@com.fasterxml.jackson.annotation.JsonProperty(\"parent\")\n" +
                        "@javax.validation.Valid\n" +
                        "private the.base.pkge.resource.web.CreateResourceEndpointResource.Parent parent;\n",
                resourceTypeSpec.fieldSpecs.get(0).toString());
    }

    @Test
    void givenResourceEndpoint_whenGenerated_thenNestedClassIsCorrect() {
        assertEquals(1, resourceTypeSpec.typeSpecs.size());
        assertEquals("" +
                        "@lombok.Data\n" +
                        "public static class Parent {\n" +
                        "  @com.fasterxml.jackson.annotation.JsonProperty(\"nested\")\n" +
                        "  private java.lang.String nested;\n" +
                        "}\n",
                resourceTypeSpec.typeSpecs.get(0).toString());
    }
}