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

class PutParentChildEndpointBuilderTests {

    static TypeSpec typeSpec;

    @BeforeAll
    static void init() {
        Endpoint endPoint = EndpointObjectMother.standardParentChildPut("putParentChild", "Child");
        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        PutParentChildEndpointBuilder<ApigenContext>
                builderEndpoint = new PutParentChildEndpointBuilder<>(new Mapping("/parents/{parent_id}/children"), endPoint, ctx,
                ConfigurationObjectMother.create());
        TypeSpec.Builder builder = TypeSpec.classBuilder("PutEndpointParentChild");
        builderEndpoint.apply(builder);
        typeSpec = builder.build();
    }

    @Test
    void givenPutEndpointBuilder_whenBuild_thenNameCorrect() {
        assertEquals("PutEndpointParentChild", typeSpec.name);
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
        assertEquals("putParentChild", methodSpec.name);

        assertEquals(2, methodSpec.annotations.size());
        AnnotationSpec annotationSpec = methodSpec.annotations.get(0);
        assertEquals("@org.springframework.web.bind.annotation.PutMapping(\"/{child_id}\")", annotationSpec.toString());
        annotationSpec = methodSpec.annotations.get(1);
        assertEquals("@org.springframework.web.bind.annotation.ResponseStatus(code = org.springframework.http.HttpStatus.OK)", annotationSpec.toString());
        assertEquals(1, methodSpec.modifiers.size());
        assertEquals("[public]", methodSpec.modifiers.toString());

        assertEquals(3, methodSpec.parameters.size());

        ParameterSpec parameterSpec = methodSpec.parameters.get(0);
        assertEquals("[@org.springframework.web.bind.annotation.PathVariable(\"parent_id\")]", parameterSpec.annotations.toString());
        assertEquals("java.lang.Long", parameterSpec.type.toString());
        assertEquals("parentId", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(1);
        assertEquals("@org.springframework.web.bind.annotation.PathVariable(\"child_id\") java.lang.Long childId", parameterSpec.toString());
        assertEquals("java.lang.Long", parameterSpec.type.toString());
        assertEquals("childId", parameterSpec.name);

        parameterSpec = methodSpec.parameters.get(2);
        assertEquals("[@org.springframework.web.bind.annotation.RequestBody, @javax.validation.Valid]", parameterSpec.annotations.toString());
        assertEquals("the.group.artifact.child.web.UpdateParentChildByChildIdResource", parameterSpec.type.toString());
        assertEquals("body", parameterSpec.name);

        assertEquals("" +
                "org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter filter = getParentFilter(parentId, null, \"parentProp.id\");\n" +
                "List<String> expand = getParentExpand(null, \"parentProp\");\n" +
                "service.search(childId, null, null, expand, filter);\n" +
                "the.group.artifact.child.Child updateRequest = mapper.toEntity(body);\n" +
                "service.update(childId, updateRequest, updatedFields);\n" +
                "the.group.artifact.child.Child createResult = service.search(childId, null, null, null);\n" +
                "the.group.artifact.child.web.ChildOutResource result = mapper.toResource(createResult);\n" +
                "return new the.group.artifact.child.web.ChildResponse(result);\n", methodSpec.code.toString());
    }
}
