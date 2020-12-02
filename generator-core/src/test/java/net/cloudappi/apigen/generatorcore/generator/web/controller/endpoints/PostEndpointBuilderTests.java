package net.cloudappi.apigen.generatorcore.generator.web.controller.endpoints;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import net.cloudappi.apigen.generatorcore.config.controller.Endpoint;
import net.cloudappi.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PostEndpointBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Endpoint endPoint = EndpointObjectMother.standardPost("post", "EntityName");
        PostEndpointBuilder builderEndpoint = new PostEndpointBuilder(new Mapping("/entities"), endPoint, "the.base.package");
        TypeSpec.Builder builder = TypeSpec.classBuilder("PostEndpoint");
        builderEndpoint.apply(builder);
        typeSpec = builder.build();
    }

    @Test
    void givenPostEndpointBuilder_whenBuild_thenNameCorrect() {
        assertEquals("PostEndpoint", typeSpec.name);
    }

    @Test
    void givenPostEndpointBuilder_whenBuild_thenNoHaveModifier() {
        assertEquals(0, typeSpec.modifiers.size());
    }

    @Test
    void givenPostEndpointBuilder_whenBuild_thenKindIsClass() {
        assertEquals("CLASS", typeSpec.kind.toString());
    }

    @Test
    void givenPostEndpointBuilder_whenBuild_thenNoHaveAnnotation() {
        assertEquals(0, typeSpec.annotations.size());
    }

    @Test
    void givenPostEndpointBuilder_whenBuild_thenNoHaveFieldsSpecs() {
        assertEquals(0, typeSpec.fieldSpecs.size());
    }

    @Test
    void givenPostEndpointBuilder_whenBuild_thenHaveSuperClassAndIsCorrect() {
        assertEquals("java.lang.Object", typeSpec.superclass.toString());
    }

    @Test
    void givenPostEndpointBuilder_whenBuild_thenHaveMethodSpecIsCorrect() {
        MethodSpec methodSpec = typeSpec.methodSpecs.get(0);
        assertFalse(methodSpec.isConstructor());
        assertEquals("post", methodSpec.name);
        assertEquals(2, methodSpec.annotations.size());

        AnnotationSpec annotationSpec = methodSpec.annotations.get(0);
        assertEquals("@org.springframework.web.bind.annotation.PostMapping", annotationSpec.toString());
        annotationSpec = methodSpec.annotations.get(1);
        assertEquals("@org.springframework.web.bind.annotation.ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)", annotationSpec.toString());

        assertEquals(1, methodSpec.modifiers.size());
        assertEquals("[public]", methodSpec.modifiers.toString());

        assertEquals("[@org.springframework.web.bind.annotation.RequestBody, @javax.validation.Valid]", methodSpec.parameters.get(0).annotations.toString());
        assertEquals("the.base.package.entityname.web.CreateEntityNameResource", methodSpec.parameters.get(0).type.toString());
        assertEquals("body", methodSpec.parameters.get(0).name);

        assertEquals("the.base.package.entityname.EntityName createRequest = mapper.toEntity(body);\n" +
                "service.create(createRequest);\n" +
                "the.base.package.entityname.EntityName createResult = service.search(createRequest.getId(), null, null, null);\n" +
                "the.base.package.entityname.web.EntityNameOutResource result = mapper.toResource(createResult);\n" +
                "return new the.base.package.entityname.web.EntityNameResponse(result);\n", methodSpec.code.toString());
    }
}
