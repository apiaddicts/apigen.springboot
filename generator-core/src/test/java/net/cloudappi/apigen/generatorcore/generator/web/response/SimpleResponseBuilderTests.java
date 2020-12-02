package net.cloudappi.apigen.generatorcore.generator.web.response;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class SimpleResponseBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        SimpleResponseBuilder builder = new SimpleResponseBuilder("EntityName", "the.base.package");
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
        assertEquals("net.cloudappi.apigen.archetypecore.core.responses.ApiResponse<the.base.package.entityname.web.EntityNameOutResource>", typeSpec.superclass.toString());
    }

    @Test
    void givenSimpleResponseBuilder_whenBuild_thenHaveMethodSpecISConstructorAndIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertTrue(methodSpec.isConstructor());
        assertEquals("[the.base.package.entityname.web.EntityNameOutResource data]", methodSpec.parameters.toString());
        assertEquals("super(data);\n", methodSpec.code.toString());
    }


    @Test
    public void givenTypeNameOfSimpleResponseBuilder_whenGetBasePackageAndEntityName_thenClassTypeName() {
        TypeName typeName = SimpleResponseBuilder.getTypeName("EntityName", "the.base.package");
        assertNotNull(typeName);
        assertEquals("the.base.package.entityname.web.EntityNameResponse", typeName.toString());
    }
}
