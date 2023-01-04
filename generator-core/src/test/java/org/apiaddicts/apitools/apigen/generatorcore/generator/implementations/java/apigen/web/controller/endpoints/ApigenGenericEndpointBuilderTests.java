package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Constants.JSON_MIME_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ApigenGenericEndpointBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Endpoint endPoint = EndpointObjectMother.customEndpoint();
        endPoint.getRequest().setMimeType(JSON_MIME_TYPE);
        endPoint.getResponse().setMimeType(JSON_MIME_TYPE);
        ApigenGenericEndpointBuilder<ApigenContext>
                builderEndpoint = new ApigenGenericEndpointBuilder<>(new Mapping("/custom"), endPoint,
                ApigenContextObjectMother.create(), ConfigurationObjectMother.create());
        TypeSpec.Builder builder = TypeSpec.classBuilder("Endpoint");
        builderEndpoint.apply(builder);

        Endpoint endPointWithMimeType = EndpointObjectMother.customEndpoint();
        endPointWithMimeType.getRequest().setMimeType("application/pdf");
        endPointWithMimeType.getResponse().setMimeType("application/pdf");
        ApigenGenericEndpointBuilder<ApigenContext>
                builderEndpointWithMimeType = new ApigenGenericEndpointBuilder<>(new Mapping("/custom"), endPointWithMimeType,
                ApigenContextObjectMother.create(), ConfigurationObjectMother.create());
        builderEndpointWithMimeType.apply(builder);
        typeSpec = builder.build();
    }

    @Test
    void givenCustomEndpointBuilder_whenBuild_thenHaveMethodSpecIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertFalse(methodSpec.isConstructor());
        assertEquals("custom", methodSpec.name);

        assertEquals(2, methodSpec.annotations.size());
        AnnotationSpec annotationSpec = methodSpec.annotations.get(0);
        assertEquals("@org.springframework.web.bind.annotation.PostMapping(\"/endpoint\")", annotationSpec.toString());
        annotationSpec = methodSpec.annotations.get(1);
        assertEquals("@org.springframework.web.bind.annotation.ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)", annotationSpec.toString());

        assertEquals(1, methodSpec.modifiers.size());
        assertEquals("[public]", methodSpec.modifiers.toString());

        assertEquals(1, methodSpec.parameters.size());
        ParameterSpec parameterSpec = methodSpec.parameters.get(0);
        assertEquals("[@org.springframework.web.bind.annotation.RequestBody, @javax.validation.Valid]", parameterSpec.annotations.toString());
        assertEquals("the.group.artifact.custom.web.CreateCustomEndpointResource", parameterSpec.type.toString());
        assertEquals("body", parameterSpec.name);

        assertEquals("// TODO: Implement this non standard endpoint\n" +
                "throw new org.apiaddicts.apitools.apigen.archetypecore.exceptions.NotImplementedException(\"POST /endpoint\");\n", methodSpec.code.toString());
    }

    @Test
    void givenCustomEndpointBuilderWithMimeType_whenBuild_thenHaveMethodSpecIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(1);
        assertFalse(methodSpec.isConstructor());
        assertEquals("custom", methodSpec.name);

        assertEquals(2, methodSpec.annotations.size());
        AnnotationSpec annotationSpec = methodSpec.annotations.get(0);
        assertEquals(
                "@org.springframework.web.bind.annotation.PostMapping(value = \"/endpoint\", consumes = \"application/pdf\", produces = \"application/pdf\")", annotationSpec.toString());
        annotationSpec = methodSpec.annotations.get(1);
        assertEquals("@org.springframework.web.bind.annotation.ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)", annotationSpec.toString());

        assertEquals(1, methodSpec.modifiers.size());
        assertEquals("[public]", methodSpec.modifiers.toString());

        assertEquals(1, methodSpec.parameters.size());
        ParameterSpec parameterSpec = methodSpec.parameters.get(0);
        assertEquals("[@org.springframework.web.bind.annotation.RequestBody, @javax.validation.Valid]", parameterSpec.annotations.toString());
        assertEquals("java.lang.Object", parameterSpec.type.toString());
        assertEquals("body", parameterSpec.name);

        assertEquals("java.lang.Object", methodSpec.returnType.toString());

        assertEquals("// TODO: Implement this non standard endpoint\n" +
                "throw new org.apiaddicts.apitools.apigen.archetypecore.exceptions.NotImplementedException(\"POST /endpoint\");\n", methodSpec.code.toString());
    }
}
