package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.response.builders.EntitySimpleResponseBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class EntitySimpleResponseBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Response response = new Response();
        response.setRelatedEntity("EntityName");
        EntitySimpleResponseBuilder<ApigenContext> builder = new EntitySimpleResponseBuilder<>(response, ApigenContextObjectMother.create(), ConfigurationObjectMother.create());
        typeSpec = builder.build();
    }

    @Test
    void givenSimpleResponseBuilder_whenBuild_thenNameCorrect() {
        assertEquals("EntityNameResponse", typeSpec.name);
    }

    @Test
    void givenSimpleResponseBuilder_whenBuild_thenModifierIsPublic() {
        assertTrue(typeSpec.hasModifier(Modifier.PUBLIC));
    }

    @Test
    void givenSimpleResponseBuilder_whenBuild_thenKindIsClass() {
        assertEquals("CLASS", typeSpec.kind.toString());
    }

    @Test
    void givenSimpleResponseBuilder_whenBuild_thenHaveOneAnnotationAndValueIsCorrect() {
        assertEquals("[@lombok.Data]", typeSpec.annotations.toString());
    }

    @Test
    void givenSimpleResponseBuilder_whenBuild_thenNoHaveFieldsSpecAndIsCorrect() {
        assertEquals("[]", typeSpec.fieldSpecs.toString());
    }

    @Test
    void givenSimpleResponseBuilder_whenBuild_thenHaveSuperClassAndIsCorrect() {
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse<the.group.artifact.entityname.web.EntityNameOutResource>", typeSpec.superclass.toString());
    }

    @Test
    void givenSimpleResponseBuilder_whenBuild_thenHaveMethodSpecISConstructorAndIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[the.group.artifact.entityname.web.EntityNameOutResource data]", methodSpec.parameters.toString());
        assertEquals("super(data);\n", methodSpec.code.toString());
    }

    @Test
    void givenTypeNameOfSimpleResponseBuilder_whenGetBasePackageAndEntityName_thenClassTypeName() {
        TypeName typeName = EntitySimpleResponseBuilder.getTypeName("EntityName", "the.group.artifact");
        assertNotNull(typeName);
        assertEquals("the.group.artifact.entityname.web.EntityNameResponse", typeName.toString());
    }
}
