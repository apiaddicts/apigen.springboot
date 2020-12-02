package net.cloudappi.apigen.generatorcore.generator.web.response;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import net.cloudappi.apigen.generatorcore.config.controller.Endpoint;
import net.cloudappi.apigen.generatorcore.utils.Mapping;
import net.cloudappi.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

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
        endpoint.getResponse().setCollectionName("collectionName");
        rootMapping = new Mapping("/resource");
        ResourceListResponseBuilder builder = new ResourceListResponseBuilder(rootMapping, endpoint, "the.base.package");
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
        assertEquals("net.cloudappi.apigen.archetypecore.core.responses.ApiResponse<net.cloudappi.apigen.archetypecore.core.responses.content.ApiListResponseContent<the.base.package.resource.web.CreateResourceEndpointOutResource>>", typeSpec.superclass.toString());
    }

    @Test
    void givenResourceListResponseBuilder_whenBuild_thenHaveMethodSpecISConstructorAndIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[java.util.List<the.base.package.resource.web.CreateResourceEndpointOutResource> collectionName]", methodSpec.parameters.toString());
        assertEquals("super(new ResourceEndpointListResponseContent(collectionName));\n", methodSpec.code.toString());
    }

    @Test
    void givenResourceListResponseBuilder_whenBuild_thenHaveTypeSpec() {
        TypeSpec subTypeSpec = typeSpec.typeSpecs.get(0);
        assertEquals("[@lombok.Data]", subTypeSpec.annotations.toString());
        assertEquals(2, subTypeSpec.modifiers.size());
        assertEquals("[private, static]", subTypeSpec.modifiers.toString());
        assertEquals("ResourceEndpointListResponseContent", subTypeSpec.name);
        assertEquals("net.cloudappi.apigen.archetypecore.core.responses.content.ApiListResponseContent<the.base.package.resource.web.CreateResourceEndpointOutResource>", subTypeSpec.superclass.toString());
        assertEquals(2, subTypeSpec.methodSpecs.size());
        MethodSpec methodSpec;
        methodSpec = subTypeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[java.util.List<the.base.package.resource.web.CreateResourceEndpointOutResource> collectionName]", methodSpec.parameters.toString());
        assertEquals("super(collectionName);\n", methodSpec.code.toString());
        methodSpec = subTypeSpec.methodSpecs.get(1);
        assertFalse(methodSpec.isConstructor());
        assertEquals("getCollectionName", methodSpec.name);
        assertEquals("[@com.fasterxml.jackson.annotation.JsonProperty(\"collectionName\")]", methodSpec.annotations.toString());
        assertEquals("return content;\n", methodSpec.code.toString());
    }

    @Test
    public void givenValidParameters_whenGetTypeName_thenClassTypeName() {
        TypeName typeName = ResourceListResponseBuilder.getTypeName(rootMapping, endpoint, "the.base.package");
        assertNotNull(typeName);
        assertEquals("the.base.package.resource.web.CreateResourceEndpointListResponse", typeName.toString());
    }
}
