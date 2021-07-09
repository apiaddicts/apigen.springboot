package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PutEndpointBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Endpoint endPoint = EndpointObjectMother.standardPut("put", "EntityName");
        PutEndpointBuilder builderEndpoint = new PutEndpointBuilder(new Mapping("/entities"), endPoint, null, "the.base.package");
        TypeSpec.Builder builder = TypeSpec.classBuilder("PutEndpoint");
        builderEndpoint.apply(builder);
        typeSpec = builder.build();
    }

    @Test
    void givenPutEndpointBuilder_whenBuild_thenNameCorrect() {
        assertEquals("PutEndpoint", typeSpec.name);
    }

    @Test
    void givenPutEndpointBuilder_whenBuild_thenModifierIsPublic() {
        assertEquals(0, typeSpec.modifiers.size());
    }

    @Test
    void givenPutEndpointBuilder_whenBuild_thenKindIsClass() {
        assertEquals("CLASS", typeSpec.kind.toString());
    }

    @Test
    void givenPutEndpointBuilder_whenBuild_thenNoHaveAnnotation() {
        assertEquals(0, typeSpec.annotations.size());
    }

    @Test
    void givenPutEndpointBuilder_whenBuild_thenNoHaveModifier() {
        assertEquals(0, typeSpec.modifiers.size());
    }

    @Test
    void givenPutEndpointBuilder_whenBuild_thenNoHaveFieldsSpecs() {
        assertEquals(0, typeSpec.fieldSpecs.size());
    }

    @Test
    void givenPutEndpointBuilder_whenBuild_thenHaveSuperClassAndIsCorrect() {
        assertEquals("java.lang.Object", typeSpec.superclass.toString());
    }

    @Test
    void givenPutEndpointBuilder_whenBuild_thenHaveMethodSpecIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertFalse(methodSpec.isConstructor());
        assertEquals("put", methodSpec.name);

        assertEquals(2, methodSpec.annotations.size());
        AnnotationSpec annotationSpec = methodSpec.annotations.get(0);
        assertEquals("@org.springframework.web.bind.annotation.PutMapping(\"/{id}\")", annotationSpec.toString());
        annotationSpec = methodSpec.annotations.get(1);
        assertEquals("@org.springframework.web.bind.annotation.ResponseStatus(code = org.springframework.http.HttpStatus.OK)", annotationSpec.toString());
        assertEquals(1, methodSpec.modifiers.size());
        assertEquals("[public]", methodSpec.modifiers.toString());

        assertEquals(3, methodSpec.parameters.size());
        ParameterSpec parameterSpec = methodSpec.parameters.get(0);
        assertEquals("@org.springframework.web.bind.annotation.PathVariable(\"id\") java.lang.Long id", parameterSpec.toString());
        assertEquals("java.lang.Long", parameterSpec.type.toString());
        assertEquals("id", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(1);
        assertEquals("[@org.springframework.web.bind.annotation.RequestBody, @javax.validation.Valid]", parameterSpec.annotations.toString());
        assertEquals("the.base.package.entityname.web.UpdateEntityNameByIdResource", parameterSpec.type.toString());
        assertEquals("body", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(2);
        assertEquals("[@org.springframework.web.bind.annotation.RequestAttribute]", parameterSpec.annotations.toString());
        assertEquals("java.util.Set<java.lang.String>", parameterSpec.type.toString());
        assertEquals("updatedFields", parameterSpec.name);

        assertEquals("the.base.package.entityname.EntityName updateRequest = mapper.toEntity(body);\n" +
                "service.update(id, updateRequest, updatedFields);\n" +
                "the.base.package.entityname.EntityName createResult = service.search(id, null, null, null);\n" +
                "the.base.package.entityname.web.EntityNameOutResource result = mapper.toResource(createResult);\n" +
                "return new the.base.package.entityname.web.EntityNameResponse(result);\n", methodSpec.code.toString());
    }
}
