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

class PostParentChildEndpointBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Endpoint endPoint = EndpointObjectMother.standardParentChildPost("postParentChild", "Child");
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        PostParentChildEndpointBuilder<ApigenContext>
                builderEndpoint = new PostParentChildEndpointBuilder<>(new Mapping("/parents/{id}/children"), endPoint, ctx,
                ConfigurationObjectMother.create());
        TypeSpec.Builder builder = TypeSpec.classBuilder("PostEndpointParentChild");
        builderEndpoint.apply(builder);
        typeSpec = builder.build();
    }

    @Test
    void givenPostEndpointBuilder_whenBuild_thenNameCorrect() {
        assertEquals("PostEndpointParentChild", typeSpec.name);
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
        assertEquals("postParentChild", methodSpec.name);
        assertEquals(2, methodSpec.annotations.size());

        AnnotationSpec annotationSpec = methodSpec.annotations.get(0);
        assertEquals("@org.springframework.web.bind.annotation.PostMapping", annotationSpec.toString());
        annotationSpec = methodSpec.annotations.get(1);
        assertEquals("@org.springframework.web.bind.annotation.ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)", annotationSpec.toString());

        assertEquals(1, methodSpec.modifiers.size());
        assertEquals("[public]", methodSpec.modifiers.toString());

        ParameterSpec parameterSpec = methodSpec.parameters.get(0);
        assertEquals("[@org.springframework.web.bind.annotation.PathVariable(\"parent_id\")]", parameterSpec.annotations.toString());
        assertEquals("java.lang.Long", parameterSpec.type.toString());
        assertEquals("parentId", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(1);
        assertEquals("[@org.springframework.web.bind.annotation.RequestBody, @javax.validation.Valid]", parameterSpec.annotations.toString());
        assertEquals("the.group.artifact.child.web.CreateParentChildResource", parameterSpec.type.toString());
        assertEquals("body", parameterSpec.name);

        assertEquals("the.group.artifact.child.Child createRequest = mapper.toEntity(body);\n" +
                "createRequest.setParentProp(new the.group.artifact.parent.Parent(parentId));\n" +
                "service.create(createRequest);\n" +
                "the.group.artifact.child.Child createResult = service.search(createRequest.getId(), null, null, null);\n" +
                "the.group.artifact.child.web.ChildOutResource result = mapper.toResource(createResult);\n" +
                "return new the.group.artifact.child.web.ChildResponse(result);\n", methodSpec.code.toString());
    }
}
