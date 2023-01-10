package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.EntityListResponseBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class EntityListResponseBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Response response = new Response();
        response.setRelatedEntity("EntityName");
        response.setCollectionName("list_name");
        EntityListResponseBuilder<ApigenContext> builder = new EntityListResponseBuilder<>(response, ApigenContextObjectMother.create(), ConfigurationObjectMother.create());
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
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse<org.apiaddicts.apitools.apigen.archetypecore.core.responses.content.ApiListResponseContent<the.group.artifact.entityname.web.EntityNameOutResource>>", typeSpec.superclass.toString());
    }

    @Test
    void givenSimpleResponseBuilder_whenBuild_thenHaveMethodSpecISConstructorAndIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[java.util.List<the.group.artifact.entityname.web.EntityNameOutResource> listName]", methodSpec.parameters.toString());
        assertEquals("super(new EntityNameListResponseContent(listName));\n", methodSpec.code.toString());
    }

    @Test
    void givenListResponseBuilder_whenBuild_thenHaveTypeSpec() {
        TypeSpec subTypeSpec = typeSpec.typeSpecs.get(0);
        assertEquals("[@lombok.Data]", subTypeSpec.annotations.toString());
        assertEquals(2, subTypeSpec.modifiers.size());
        assertEquals("[private, static]", subTypeSpec.modifiers.toString());
        assertEquals("EntityNameListResponseContent", subTypeSpec.name);
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.responses.content.ApiListResponseContent<the.group.artifact.entityname.web.EntityNameOutResource>", subTypeSpec.superclass.toString());
        assertEquals(2, subTypeSpec.methodSpecs.size());
        MethodSpec methodSpec;
        methodSpec = subTypeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[java.util.List<the.group.artifact.entityname.web.EntityNameOutResource> listName]", methodSpec.parameters.toString());
        assertEquals("super(listName);\n", methodSpec.code.toString());
        methodSpec = subTypeSpec.methodSpecs.get(1);
        assertFalse(methodSpec.isConstructor());
        assertEquals("getContent", methodSpec.name);
        assertEquals("[@java.lang.Override, @com.fasterxml.jackson.annotation.JsonProperty(\"list_name\")]", methodSpec.annotations.toString());
        assertEquals("return content;\n", methodSpec.code.toString());
    }

    @Test
    void givenTypeNameOfSimpleResponseBuilder_whenGetBasePackageAndEntityName_thenClassTypeName() {
        TypeName typeName = EntityListResponseBuilder.getTypeName("EntityName", "the.group.artifact");
        assertNotNull(typeName);
        assertEquals("the.group.artifact.entityname.web.EntityNameListResponse", typeName.toString());
    }
}
