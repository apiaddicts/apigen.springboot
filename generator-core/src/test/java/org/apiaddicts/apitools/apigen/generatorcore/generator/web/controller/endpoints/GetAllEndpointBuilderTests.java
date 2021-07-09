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

class GetAllEndpointBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Endpoint endPoint = EndpointObjectMother.standardGetAll("getAll", "EntityName");
        GetAllEndpointBuilder builderEndpoint = new GetAllEndpointBuilder(new Mapping("/entities"), endPoint, "the.base.package");
        TypeSpec.Builder builder = TypeSpec.classBuilder("GetAllEndpoint");
        builderEndpoint.apply(builder);
        typeSpec = builder.build();
    }

    @Test
    void givenGetAllEndpointBuilder_whenBuild_thenNameCorrect() {
        assertEquals("GetAllEndpoint", typeSpec.name);
    }

    @Test
    void givenGetAllEndpointBuilder_whenBuild_thenNoHaveModifier() {
        assertEquals(0, typeSpec.modifiers.size());
    }

    @Test
    void givenGetAllEndpointBuilder_whenBuild_thenKindIsClass() {
        assertEquals("CLASS", typeSpec.kind.toString());
    }

    @Test
    void givenGetAllEndpointBuilder_whenBuild_thenNoHaveAnnotation() {
        assertEquals(0, typeSpec.annotations.size());
    }

    @Test
    void givenGetAllEndpointBuilder_whenBuild_thenNoHaveFieldsSpecs() {
        assertEquals(0, typeSpec.fieldSpecs.size());
    }

    @Test
    void givenGetAllEndpointBuilder_whenBuild_thenHaveSuperClassAndIsCorrect() {
        assertEquals("java.lang.Object", typeSpec.superclass.toString());
    }

    @Test
    void givenGetAllEndpointBuilder_whenBuild_thenHaveMethodSpecIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertFalse(methodSpec.isConstructor());
        assertEquals("getAll", methodSpec.name);

        assertEquals(2, methodSpec.annotations.size());
        AnnotationSpec annotationSpec = methodSpec.annotations.get(0);
        assertEquals("@org.springframework.web.bind.annotation.GetMapping", annotationSpec.toString());
        annotationSpec = methodSpec.annotations.get(1);
        assertEquals("@org.springframework.web.bind.annotation.ResponseStatus(code = org.springframework.http.HttpStatus.PARTIAL_CONTENT)", annotationSpec.toString());

        assertEquals(1, methodSpec.modifiers.size());
        assertEquals("[public]", methodSpec.modifiers.toString());

        assertEquals(7, methodSpec.parameters.size());
        ParameterSpec parameterSpec = methodSpec.parameters.get(0);
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(value = \"$init\", required = true, defaultValue = \"0\")]", parameterSpec.annotations.toString());
        assertEquals("java.lang.Integer", parameterSpec.type.toString());
        assertEquals("init", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(1);
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(value = \"$limit\", required = true, defaultValue = \"25\")]", parameterSpec.annotations.toString());
        assertEquals("java.lang.Integer", parameterSpec.type.toString());
        assertEquals("limit", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(2);
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(value = \"$total\", required = true, defaultValue = \"false\")]", parameterSpec.annotations.toString());
        assertEquals("java.lang.Boolean", parameterSpec.type.toString());
        assertEquals("total", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(3);
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(value = \"$select\", required = false)]", parameterSpec.annotations.toString());
        assertEquals("java.util.List<java.lang.String>", parameterSpec.type.toString());
        assertEquals("select", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(4);
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(value = \"$exclude\", required = false)]", parameterSpec.annotations.toString());
        assertEquals("java.util.List<java.lang.String>", parameterSpec.type.toString());
        assertEquals("exclude", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(5);
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(value = \"$expand\", required = false)]", parameterSpec.annotations.toString());
        assertEquals("java.util.List<java.lang.String>", parameterSpec.type.toString());
        assertEquals("expand", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(6);
        assertEquals("[@org.springframework.web.bind.annotation.RequestParam(value = \"$orderby\", required = false)]", parameterSpec.annotations.toString());
        assertEquals("java.util.List<java.lang.String>", parameterSpec.type.toString());
        assertEquals("orderby", parameterSpec.name);

        assertEquals("namingTranslator.translate(select, exclude, expand, orderby, the.base.package.entityname.web.EntityNameOutResource.class);\n" +
                "org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenSearchResult<the.base.package.entityname.EntityName> searchResult = service.search(select, exclude, expand, null, orderby, init, limit, total);\n" +
                "java.util.List<the.base.package.entityname.web.EntityNameOutResource> result = mapper.toResource(searchResult.getSearchResult());\n" +
                "return new the.base.package.entityname.web.EntityNameListResponse(result).withMetadataPagination(init, limit, searchResult.getTotal());\n", methodSpec.code.toString());
    }

}
