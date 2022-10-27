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

class GetByIdEndpointBuilderTests {

    private static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Endpoint endPoint = EndpointObjectMother.standardGetById("getById", "EntityName");
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        GetByIdEndpointBuilder<ApigenContext> builderEndpoint = new GetByIdEndpointBuilder<>(new Mapping("/entities"), endPoint, ctx,
                ConfigurationObjectMother.create());
        TypeSpec.Builder builder = TypeSpec.classBuilder("GetByIdEndpoint");
        builderEndpoint.apply(builder);
        typeSpec = builder.build();
    }

    @Test
    void givenGetByIdEndpointBuilder_whenBuild_thenNameCorrect() {
        assertEquals("GetByIdEndpoint", typeSpec.name);
    }

    @Test
    void givenGetByIdEndpointBuilder_whenBuild_thenModifierIsPublic() {
        assertEquals(0, typeSpec.modifiers.size());
    }

    @Test
    void givenGetByIdEndpointBuilder_whenBuild_thenKindIsClass() {
        assertEquals("CLASS", typeSpec.kind.toString());
    }

    @Test
    void givenGetByIdEndpointBuilder_whenBuild_thenNoHaveAnnotation() {
        assertEquals(0, typeSpec.annotations.size());
    }

    @Test
    void givenGetByIdEndpointBuilder_whenBuild_thenNoHaveModifier() {
        assertEquals(0, typeSpec.modifiers.size());
    }

    @Test
    void givenGetByIdEndpointBuilder_whenBuild_thenNoHaveFieldsSpecs() {
        assertEquals(0, typeSpec.fieldSpecs.size());
    }

    @Test
    void givenGetByIdEndpointBuilder_whenBuild_thenHaveSuperClassAndIsCorrect() {
        assertEquals("java.lang.Object", typeSpec.superclass.toString());
    }

    @Test
    void givenGetByIdEndpointBuilder_whenBuild_thenHaveMethodSpecIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertFalse(methodSpec.isConstructor());
        assertEquals("getById", methodSpec.name);

        assertEquals(2, methodSpec.annotations.size());
        AnnotationSpec annotationSpec = methodSpec.annotations.get(0);
        assertEquals("@org.springframework.web.bind.annotation.GetMapping(\"/{id}\")", annotationSpec.toString());
        annotationSpec = methodSpec.annotations.get(1);
        assertEquals("@org.springframework.web.bind.annotation.ResponseStatus(code = org.springframework.http.HttpStatus.OK)", annotationSpec.toString());

        assertEquals(1, methodSpec.modifiers.size());
        assertEquals("[public]", methodSpec.modifiers.toString());

        assertEquals(4, methodSpec.parameters.size());
        ParameterSpec parameterSpec = methodSpec.parameters.get(0);
        assertEquals("[@org.springframework.web.bind.annotation.PathVariable(\"id\")]", parameterSpec.annotations.toString());
        assertEquals("java.lang.Long", parameterSpec.type.toString());
        assertEquals("id", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(1);
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(value = \"$select\", required = false)]", parameterSpec.annotations.toString());
        assertEquals("java.util.List<java.lang.String>", parameterSpec.type.toString());
        assertEquals("select", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(2);
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(value = \"$exclude\", required = false)]", parameterSpec.annotations.toString());
        assertEquals("java.util.List<java.lang.String>", parameterSpec.type.toString());
        assertEquals("exclude", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(3);
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(value = \"$expand\", required = false)]", parameterSpec.annotations.toString());
        assertEquals("java.util.List<java.lang.String>", parameterSpec.type.toString());
        assertEquals("expand", parameterSpec.name);

        assertEquals("namingTranslator.translate(select, exclude, expand, null, the.group.artifact.entityname.web.EntityNameOutResource.class);\n" +
                "the.group.artifact.entityname.EntityName searchResult = service.search(id, select, exclude, expand, null);\n" +
                "the.group.artifact.entityname.web.EntityNameOutResource result = mapper.toResource(searchResult);\n" +
                "return new the.group.artifact.entityname.web.EntityNameResponse(result);\n", methodSpec.code.toString());
    }
}
