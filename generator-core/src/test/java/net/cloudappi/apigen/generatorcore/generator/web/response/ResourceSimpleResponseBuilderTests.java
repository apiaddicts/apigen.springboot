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
        ResourceSimpleResponseBuilder builder = new ResourceSimpleResponseBuilder(rootMapping, endpoint, "the.base.package");
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
        assertEquals("net.cloudappi.apigen.archetypecore.core.responses.ApiResponse<the.base.package.resource.web.CreateResourceEndpointOutResource>", typeSpec.superclass.toString());
    }

    @Test
    void givenResourceSimpleResponseBuilder_whenBuild_thenHaveMethodSpecISConstructorAndIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[the.base.package.resource.web.CreateResourceEndpointOutResource data]", methodSpec.parameters.toString());
        assertEquals("super(data);\n", methodSpec.code.toString());
    }

    @Test
    public void givenTypeNameOfResourceSimpleResponseBuilder_whenGetBasePackageAndEntityName_thenClassTypeName() {
        TypeName typeName = ResourceSimpleResponseBuilder.getTypeName(rootMapping, endpoint, "the.base.package");
        assertNotNull(typeName);
        assertEquals("the.base.package.resource.web.CreateResourceEndpointResponse", typeName.toString());
    }
}
