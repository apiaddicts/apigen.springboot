package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.ResourceListResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class ResourceListResponseBuilderTests {

    private static TypeSpec typeSpec;
    private static Mapping rootMapping;
    private static Endpoint endpoint;


    @BeforeAll
    static void init() {
        endpoint = EndpointObjectMother.customEndpoint();
        endpoint.getResponse().setIsCollection(true);
        endpoint.getResponse().setIsStandard(true);
        endpoint.getResponse().setCollectionName("collection_name");
        rootMapping = new Mapping("/resource");
        ResourceListResponseBuilder<ApigenContext> builder = new ResourceListResponseBuilder<>(rootMapping, endpoint, ApigenContextObjectMother.create(),
                ConfigurationObjectMother.create());
        typeSpec = builder.build();
    }

    @Test
    void givenResourceListResponseBuilder_whenBuild_thenNameCorrect() {
        assertEquals("CreateResourceEndpointListResponse", typeSpec.name);
    }

    @Test
    void givenResourceListResponseBuilder_whenBuild_thenModifierIsPublic() {
        assertTrue(typeSpec.hasModifier(Modifier.PUBLIC));
    }

    @Test
    void givenResourceListResponseBuilder_whenBuild_thenKindIsClass() {
        assertEquals("CLASS", typeSpec.kind.toString());
    }

    @Test
    void givenResourceListResponseBuilder_whenBuild_thenHaveOneAnnotationAndValueIsCorrect() {
        assertEquals("[@lombok.Data]", typeSpec.annotations.toString());
    }

    @Test
    void givenResourceListResponseBuilder_whenBuild_thenHaveSuperClassAndIsCorrect() {
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse<org.apiaddicts.apitools.apigen.archetypecore.core.responses.content.ApiListResponseContent<the.group.artifact.resource.web.CreateResourceEndpointOutResource>>", typeSpec.superclass.toString());
    }

    @Test
    void givenResourceListResponseBuilder_whenBuild_thenHaveMethodSpecISConstructorAndIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[java.util.List<the.group.artifact.resource.web.CreateResourceEndpointOutResource> collectionName]", methodSpec.parameters.toString());
        assertEquals("super(new ResourceEndpointListResponseContent(collectionName));\n", methodSpec.code.toString());
    }

    @Test
    void givenResourceListResponseBuilder_whenBuild_thenHaveTypeSpec() {
        TypeSpec subTypeSpec = typeSpec.typeSpecs.get(0);
        assertEquals("[@lombok.Data]", subTypeSpec.annotations.toString());
        assertEquals(2, subTypeSpec.modifiers.size());
        assertEquals("[private, static]", subTypeSpec.modifiers.toString());
        assertEquals("ResourceEndpointListResponseContent", subTypeSpec.name);
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.responses.content.ApiListResponseContent<the.group.artifact.resource.web.CreateResourceEndpointOutResource>", subTypeSpec.superclass.toString());
        assertEquals(2, subTypeSpec.methodSpecs.size());
        MethodSpec methodSpec;
        methodSpec = subTypeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[java.util.List<the.group.artifact.resource.web.CreateResourceEndpointOutResource> collectionName]", methodSpec.parameters.toString());
        assertEquals("super(collectionName);\n", methodSpec.code.toString());
        methodSpec = subTypeSpec.methodSpecs.get(1);
        assertFalse(methodSpec.isConstructor());
        assertEquals("getContent", methodSpec.name);
        assertEquals("[@java.lang.Override, @com.fasterxml.jackson.annotation.JsonProperty(\"collection_name\")]", methodSpec.annotations.toString());
        assertEquals("return content;\n", methodSpec.code.toString());
    }

    @Test
    void givenValidParameters_whenGetTypeName_thenClassTypeName() {
        TypeName typeName = ResourceListResponseBuilder.getTypeName(rootMapping, endpoint, "the.group.artifact");
        assertNotNull(typeName);
        assertEquals("the.group.artifact.resource.web.CreateResourceEndpointListResponse", typeName.toString());
    }
}
