package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.ResourceSimpleResponseBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class ResourceSimpleResponseBuilderTests {

    private static TypeSpec typeSpec;
    private static Mapping rootMapping;
    private static Endpoint endpoint;

    @BeforeAll
    static void init() {
        endpoint = EndpointObjectMother.customEndpoint();
        endpoint.getResponse().setIsCollection(false);
        endpoint.getResponse().setIsStandard(true);
        rootMapping = new Mapping("/resource");
        ResourceSimpleResponseBuilder<ApigenContext>
                builder = new ResourceSimpleResponseBuilder<>(rootMapping, endpoint, ApigenContextObjectMother.create(),
                ConfigurationObjectMother.create());
        typeSpec = builder.build();
    }

    @Test
    void givenResourceSimpleResponseBuilder_whenBuild_thenNameCorrect() {
        assertEquals("CreateResourceEndpointResponse", typeSpec.name);
    }

    @Test
    void givenResourceSimpleResponseBuilder_whenBuild_thenModifierIsPublic() {
        assertTrue(typeSpec.hasModifier(Modifier.PUBLIC));
    }

    @Test
    void givenResourceSimpleResponseBuilder_whenBuild_thenKindIsClass() {
        assertEquals("CLASS", typeSpec.kind.toString());
    }

    @Test
    void givenResourceSimpleResponseBuilder_whenBuild_thenHaveOneAnnotationAndValueIsCorrect() {
        assertEquals("[@lombok.Data]", typeSpec.annotations.toString());
    }

    @Test
    void givenResourceSimpleResponseBuilder_whenBuild_thenNoHaveFieldsSpecAndIsCorrect() {
        assertEquals("[]", typeSpec.fieldSpecs.toString());
    }

    @Test
    void givenSimpleResponseBuilder_whenBuild_thenHaveSuperClassAndIsCorrect() {
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse<the.group.artifact.resource.web.CreateResourceEndpointOutResource>", typeSpec.superclass.toString());
    }

    @Test
    void givenResourceSimpleResponseBuilder_whenBuild_thenHaveMethodSpecISConstructorAndIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[the.group.artifact.resource.web.CreateResourceEndpointOutResource data]", methodSpec.parameters.toString());
        assertEquals("super(data);\n", methodSpec.code.toString());
    }

    @Test
    void givenTypeNameOfResourceSimpleResponseBuilder_whenGetBasePackageAndEntityName_thenClassTypeName() {
        TypeName typeName = ResourceSimpleResponseBuilder.getTypeName(rootMapping, endpoint, "the.group.artifact");
        assertNotNull(typeName);
        assertEquals("the.group.artifact.resource.web.CreateResourceEndpointResponse", typeName.toString());
    }
}
