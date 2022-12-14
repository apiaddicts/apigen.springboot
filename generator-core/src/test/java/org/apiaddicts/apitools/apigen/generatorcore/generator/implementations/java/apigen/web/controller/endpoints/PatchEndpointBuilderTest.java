package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PatchEndpointBuilderTest {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Endpoint endPoint = EndpointObjectMother.standardPatch("patch", "EntityName");
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        PatchEndpointBuilder<ApigenContext>
                builderEndpoint = new PatchEndpointBuilder<>(new Mapping("/entities"), endPoint, ctx,
                ConfigurationObjectMother.create());
        TypeSpec.Builder builder = TypeSpec.classBuilder("PatchEndpoint");
        builderEndpoint.apply(builder);
        typeSpec = builder.build();
    }

    @Test
    void givenPatchEndpointBuilder_whenBuild_thenNameCorrect() {
        assertEquals("PatchEndpoint", typeSpec.name);
    }

    @Test
    void givenPatchEndpointBuilder_whenBuild_thenModifierIsPublic() {
        assertEquals(0, typeSpec.modifiers.size());
    }

    @Test
    void givenPatchEndpointBuilder_whenBuild_thenKindIsClass() {
        assertEquals("CLASS", typeSpec.kind.toString());
    }

    @Test
    void givenPatchEndpointBuilder_whenBuild_thenNoHaveAnnotation() {
        assertEquals(0, typeSpec.annotations.size());
    }

    @Test
    void givenPatchEndpointBuilder_whenBuild_thenNoHaveModifier() {
        assertEquals(0, typeSpec.modifiers.size());
    }

    @Test
    void givenPatchEndpointBuilder_whenBuild_thenNoHaveFieldsSpecs() {
        assertEquals(0, typeSpec.fieldSpecs.size());
    }

    @Test
    void givenPatchEndpointBuilder_whenBuild_thenHaveSuperClassAndIsCorrect() {
        assertEquals("java.lang.Object", typeSpec.superclass.toString());
    }

    @Test
    void givenPatchEndpointBuilder_whenBuild_thenHaveMethodSpecIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertFalse(methodSpec.isConstructor());
        assertEquals("patch", methodSpec.name);

        assertEquals(2, methodSpec.annotations.size());
        AnnotationSpec annotationSpec = methodSpec.annotations.get(0);
        assertEquals("@org.springframework.web.bind.annotation.PatchMapping(\"/{id}\")", annotationSpec.toString());
        annotationSpec = methodSpec.annotations.get(1);
        assertEquals("@org.springframework.web.bind.annotation.ResponseStatus(code = org.springframework.http.HttpStatus.OK)", annotationSpec.toString());
        assertEquals(1, methodSpec.modifiers.size());
        assertEquals("[public]", methodSpec.modifiers.toString());

        assertEquals(2, methodSpec.parameters.size());
        ParameterSpec parameterSpec = methodSpec.parameters.get(0);
        assertEquals("@org.springframework.web.bind.annotation.PathVariable(\"id\") java.lang.Long id", parameterSpec.toString());
        assertEquals("java.lang.Long", parameterSpec.type.toString());
        assertEquals("id", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(1);
        assertEquals("[@org.springframework.web.bind.annotation.RequestBody, @javax.validation.Valid]", parameterSpec.annotations.toString());
        assertEquals("the.group.artifact.entityname.web.PartialUpdateEntityNameByIdResource", parameterSpec.type.toString());
        assertEquals("body", parameterSpec.name);

        assertEquals("the.group.artifact.entityname.EntityName original = service.safeGetOne(id);\n" +
                "mapper.partialUpdate(body, original);\n" +
                "service.update(id, original);\n" +
                "the.group.artifact.entityname.EntityName createResult = service.search(id, null, null, null);\n" +
                "the.group.artifact.entityname.web.EntityNameOutResource result = mapper.toResource(createResult);\n" +
                "return new the.group.artifact.entityname.web.EntityNameResponse(result);\n", methodSpec.code.toString());
    }
}
