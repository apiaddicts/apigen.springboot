package org.apiaddicts.apitools.apigen.generatorcore.generator.web.response;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.*;

@Deprecated
class EntityListResponseBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        EntityListResponseBuilder builder = new EntityListResponseBuilder("EntityName", "list_name", "the.base.package");
        typeSpec = builder.build();
    }

    @Test
    void givenListResponseBuilder_whenBuild_thenNameCorrect() {
        assertEquals("EntityNameListResponse", typeSpec.name);
    }

    @Test
    void givenListResponseBuilder_whenBuild_thenModifierIsPublic() {
        assertTrue(typeSpec.hasModifier(Modifier.PUBLIC));
    }

    @Test
    void givenListResponseBuilder_whenBuild_thenKindIsClass() {
        assertEquals("CLASS", typeSpec.kind.toString());
    }

    @Test
    void givenListResponseBuilder_whenBuild_thenHaveOneAnnotationAndValueIsCorrect() {
        assertEquals("[@lombok.Data]", typeSpec.annotations.toString());
    }

    @Test
    void givenListResponseBuilder_whenBuild_thenHaveSuperClassAndIsCorrect() {
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse<org.apiaddicts.apitools.apigen.archetypecore.core.responses.content.ApiListResponseContent<the.base.package.entityname.web.EntityNameOutResource>>", typeSpec.superclass.toString());
    }

    @Test
    void givenSimpleResponseBuilder_whenBuild_thenHaveMethodSpecISConstructorAndIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[java.util.List<the.base.package.entityname.web.EntityNameOutResource> listName]", methodSpec.parameters.toString());
        assertEquals("super(new EntityNameListResponseContent(listName));\n", methodSpec.code.toString());
    }

    @Test
    void givenListResponseBuilder_whenBuild_thenHaveTypeSpec() {
        TypeSpec subTypeSpec = typeSpec.typeSpecs.get(0);
        assertEquals("[@lombok.Data]", subTypeSpec.annotations.toString());
        assertEquals(2, subTypeSpec.modifiers.size());
        assertEquals("[private, static]", subTypeSpec.modifiers.toString());
        assertEquals("EntityNameListResponseContent", subTypeSpec.name);
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.responses.content.ApiListResponseContent<the.base.package.entityname.web.EntityNameOutResource>", subTypeSpec.superclass.toString());
        assertEquals(2, subTypeSpec.methodSpecs.size());
        MethodSpec methodSpec;
        methodSpec = subTypeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[java.util.List<the.base.package.entityname.web.EntityNameOutResource> listName]", methodSpec.parameters.toString());
        assertEquals("super(listName);\n", methodSpec.code.toString());
        methodSpec = subTypeSpec.methodSpecs.get(1);
        assertFalse(methodSpec.isConstructor());
        assertEquals("getContent", methodSpec.name);
        assertEquals("[@java.lang.Override, @com.fasterxml.jackson.annotation.JsonProperty(\"list_name\")]", methodSpec.annotations.toString());
        assertEquals("return content;\n", methodSpec.code.toString());
    }

    @Test
    public void givenTypeNameOfSimpleResponseBuilder_whenGetBasePackageAndEntityName_thenClassTypeName() {
        TypeName typeName = EntityListResponseBuilder.getTypeName("EntityName", "the.base.package");
        assertNotNull(typeName);
        assertEquals("the.base.package.entityname.web.EntityNameListResponse", typeName.toString());
    }
}
