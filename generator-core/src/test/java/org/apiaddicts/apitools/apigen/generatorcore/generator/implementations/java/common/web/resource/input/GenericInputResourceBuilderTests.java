package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input;

import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Attribute;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.AttributeObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericInputResourceBuilderTests {

    private static TypeSpec entityTypeSpec;
    private static TypeSpec entityNestedTypeSpec;
    private static TypeSpec resourceTypeSpec;
    private static TypeSpec classNestedSubclassSpec;


    @BeforeAll
    static void init() {
        initEntityTypeSpec();
        initEntityNestedIdTypeSpec();
        initResourceTypeSpec();
        initClassNestedSubclassSpec();
    }

	private static void initEntityTypeSpec() {
        Attribute attribute = AttributeObjectMother.createSimpleAttribute("string", "json", "java");

        Endpoint endpoint = EndpointObjectMother.customEndpoint();
        endpoint.setRelatedEntity("Entity");
        endpoint.getRequest().setAttributes(Collections.singletonList(attribute));
        Mapping mapping = new Mapping("/entity");

        GenericInputResourceBuilder<JavaContext> builder = new GenericInputResourceBuilder<>(mapping, endpoint,
                JavaContextObjectMother.create(),
                ConfigurationObjectMother.create());
        entityTypeSpec = builder.build();
    }

    private static void initEntityNestedIdTypeSpec() {
        Attribute attribute = AttributeObjectMother.createSimpleAttribute("integer", "other", "other.id");
        attribute.setRelatedEntity("Other");

        Endpoint endpoint = EndpointObjectMother.customEndpoint();
        endpoint.setRelatedEntity("Entity");
        endpoint.getRequest().setAttributes(Collections.singletonList(attribute));
        Mapping mapping = new Mapping("/entity");

        GenericInputResourceBuilder<JavaContext> builder = new GenericInputResourceBuilder<>(mapping, endpoint, JavaContextObjectMother.create(),
                ConfigurationObjectMother.create());
        entityNestedTypeSpec = builder.build();
    }

    private static void initResourceTypeSpec() {
        Attribute nested = AttributeObjectMother.createSimpleAttribute("string", "nested");
        Attribute attribute = AttributeObjectMother.createSimpleAttribute("object", "parent");
        attribute.getAttributes().add(nested);

        Endpoint endpoint = EndpointObjectMother.customEndpoint();
        endpoint.getRequest().setAttributes(Collections.singletonList(attribute));
        Mapping mapping = new Mapping("/resource");

        GenericInputResourceBuilder<JavaContext> builder = new GenericInputResourceBuilder<>(mapping, endpoint, JavaContextObjectMother.create(),
                ConfigurationObjectMother.create());
        resourceTypeSpec = builder.build();
    }

    private static void initClassNestedSubclassSpec() {
    	 Attribute attribute = AttributeObjectMother.createSimpleAttribute("object", "classOne", "fieldOne");
         attribute.setRelatedEntity("classTwo");
         
         Attribute nestedClassAttribute = AttributeObjectMother.createSimpleAttribute("object", "classTwo", "fieldTwo");
         attribute.setAttributes(new ArrayList<Attribute>() {{ add(nestedClassAttribute); }});
         
         Endpoint endpoint = EndpointObjectMother.customEndpoint();
         endpoint.setRelatedEntity("Entity");
         endpoint.getRequest().setAttributes(Collections.singletonList(attribute));
        Mapping mapping = new Mapping("/entity");

        GenericInputResourceBuilder<JavaContext> builder = new GenericInputResourceBuilder<>(mapping, endpoint, JavaContextObjectMother.create(),
                ConfigurationObjectMother.create());
         classNestedSubclassSpec = builder.build();
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
                        "private java.lang.Long other;\n",
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
                        "private the.group.artifact.resource.web.CreateResourceEndpointResource.Parent parent;\n",
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
    
    @Test
    void givenEntityNestedIdEndpoint_whenGenerated_thenClassNestedSubclassIsCorrect() {
        assertEquals(1, classNestedSubclassSpec.typeSpecs.size());
        assertEquals("@lombok.Data\n"
        		+ "public class CreateEntityEndpointResource {\n"
        		+ "  @com.fasterxml.jackson.annotation.JsonProperty(\"classOne\")\n"
        		+ "  @javax.validation.Valid\n"
        		+ "  private the.group.artifact.entity.web.CreateEntityEndpointResource.FieldOne fieldOne;\n"
        		+ "\n"
        		+ "  @lombok.Data\n"
        		+ "  public static class FieldOne {\n"
        		+ "    @com.fasterxml.jackson.annotation.JsonProperty(\"classTwo\")\n"
        		+ "    @javax.validation.Valid\n"
        		+ "    private the.group.artifact.entity.web.CreateEntityEndpointResource.FieldOne.FieldTwo fieldTwo;\n"
        		+ "\n"
        		+ "    @lombok.Data\n"
        		+ "    public static class FieldTwo {\n"
        		+ "    }\n"
        		+ "  }\n"
        		+ "}\n"
        		+ "",
                classNestedSubclassSpec.toString());
    }
}
