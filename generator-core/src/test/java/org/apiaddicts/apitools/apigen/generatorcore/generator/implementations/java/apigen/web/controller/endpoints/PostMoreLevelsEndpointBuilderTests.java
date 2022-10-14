package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints.PostEndpointBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PostMoreLevelsEndpointBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Endpoint endPoint = EndpointObjectMother.standardPostMoreLevels("postMoreLevels", "EntityName");
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        PostMoreLevelsEndpointBuilder<ApigenContext>
                builderEndpoint = new PostMoreLevelsEndpointBuilder<>(new Mapping("/entities"), endPoint, ctx,
                ConfigurationObjectMother.create());
        TypeSpec.Builder builder = TypeSpec.classBuilder("PostEndpointMoreLevels");
        builderEndpoint.apply(builder);
        typeSpec = builder.build();
    }

    @Test
    void givenPostEndpointBuilder_whenBuild_thenNameCorrect() {
        assertEquals("PostEndpointMoreLevels", typeSpec.name);
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
        assertEquals("postMoreLevels", methodSpec.name);
        assertEquals(2, methodSpec.annotations.size());

        AnnotationSpec annotationSpec = methodSpec.annotations.get(0);
        assertEquals("@org.springframework.web.bind.annotation.PostMapping(\"/{id}/elements\")", annotationSpec.toString());
        annotationSpec = methodSpec.annotations.get(1);
        assertEquals("@org.springframework.web.bind.annotation.ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)", annotationSpec.toString());

        assertEquals(1, methodSpec.modifiers.size());
        assertEquals("[public]", methodSpec.modifiers.toString());

        assertEquals("[@org.springframework.web.bind.annotation.PathVariable(\"id\")]", methodSpec.parameters.get(0).annotations.toString());
        assertEquals("java.lang.Long", methodSpec.parameters.get(0).type.toString());
        assertEquals("id", methodSpec.parameters.get(0).name);

        assertEquals("the.group.artifact.entityname.EntityName createRequest = mapper.toEntity(body);\n" +
                "createRequest.setmain(new the.group.artifact.main.main(id));\n" +
                "service.create(createRequest);\n" +
                "the.group.artifact.entityname.EntityName createResult = service.search(createRequest.getId(), null, null, null);\n" +
                "the.group.artifact.entityname.web.EntityNameOutResource result = mapper.toResource(createResult);\n" +
                "return new the.group.artifact.entityname.web.EntityNameResponse(result);\n", methodSpec.code.toString());
    }
}
