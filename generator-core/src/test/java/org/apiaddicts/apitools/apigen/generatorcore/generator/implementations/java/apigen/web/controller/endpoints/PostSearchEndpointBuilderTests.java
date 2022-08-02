package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints.PostSearchEndpointBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PostSearchEndpointBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Endpoint endPoint = EndpointObjectMother.standardSearch("postSearch", "EntityName");
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        PostSearchEndpointBuilder<ApigenContext>
                builderEndpoint = new PostSearchEndpointBuilder<>(new Mapping("/entities"), endPoint, ctx,
                ConfigurationObjectMother.create());
        TypeSpec.Builder builder = TypeSpec.classBuilder("PostSearch");
        builderEndpoint.apply(builder);
        typeSpec = builder.build();
    }

    @Test
    void givenPostSearchEndpointBuilder_whenBuild_thenNameCorrect() {
        assertEquals("PostSearch", typeSpec.name);
    }

    @Test
    void givenPostSearchEndpointBuilder_whenBuild_thenKindIsClass() {
        assertEquals("CLASS", typeSpec.kind.toString());
    }

    @Test
    void givenPostSearchEndpointBuilder_whenBuild_thenNoHaveAnnotation() {
        assertEquals(0, typeSpec.annotations.size());
    }

    @Test
    void givenPostSearchEndpointBuilder_whenBuild_thenNoHaveModifier() {
        assertEquals(0, typeSpec.modifiers.size());
    }

    @Test
    void givenPostSearchEndpointBuilder_whenBuild_thenNoHaveFieldsSpecs() {
        assertEquals(0, typeSpec.fieldSpecs.size());
    }

    @Test
    void givenPostSearchEndpointBuilder_whenBuild_thenHaveSuperClassAndIsCorrect() {
        assertEquals("java.lang.Object", typeSpec.superclass.toString());
    }

    @Test
    void givenPostSearchEndpointBuilder_whenBuild_thenHaveMethodSpecIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertFalse(methodSpec.isConstructor());
        assertEquals("postSearch", methodSpec.name);
        assertEquals(2, methodSpec.annotations.size());

        AnnotationSpec annotationSpec = methodSpec.annotations.get(0);
        assertEquals("@org.springframework.web.bind.annotation.PostMapping(\"/search\")", annotationSpec.toString());
        annotationSpec = methodSpec.annotations.get(1);
        assertEquals("@org.springframework.web.bind.annotation.ResponseStatus(code = org.springframework.http.HttpStatus.PARTIAL_CONTENT)", annotationSpec.toString());
        assertEquals(1, methodSpec.modifiers.size());
        assertEquals("[public]", methodSpec.modifiers.toString());

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

        parameterSpec = methodSpec.parameters.get(7);
        assertEquals("[@org.springframework.web.bind.annotation.RequestBody, @javax.validation.Valid]", parameterSpec.annotations.toString());
        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.resource.FilterResource", parameterSpec.type.toString());
        assertEquals("body", parameterSpec.name);

        assertEquals("org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter filter = body.getFilter();\n" +
                "namingTranslator.translate(select, exclude, expand, filter, orderby, the.group.artifact.entityname.web.EntityNameOutResource.class);\n" +
                "org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenSearchResult<the.group.artifact.entityname.EntityName> searchResult = service.search(select, exclude, expand, filter, orderby, init, limit, total);\n" +
                "java.util.List<the.group.artifact.entityname.web.EntityNameOutResource> result = mapper.toResource(searchResult.getSearchResult());\n" +
                "return new the.group.artifact.entityname.web.EntityNameListResponse(result).withMetadataPagination(init, limit, searchResult.getTotal());\n", methodSpec.code.toString());
    }
}
