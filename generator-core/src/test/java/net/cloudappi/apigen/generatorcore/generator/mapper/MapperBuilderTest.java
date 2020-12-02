package net.cloudappi.apigen.generatorcore.generator.mapper;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import net.cloudappi.apigen.generatorcore.config.mapper.MapperBuilderObjectMother;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperBuilderTest {

    private static TypeSpec generatedMapper;

    @BeforeAll
    static void prepareTest() {
        generatedMapper = MapperBuilderObjectMother
                .createMapper("EntityName", "the.base.package", "simpleAttribute", "RelatedEntity")
                .build();
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenFileStructureIsCorrect() {
        assertEquals(Modifier.PUBLIC, generatedMapper.modifiers.toArray()[0], "Public modifier is wrong");
        assertEquals(TypeSpec.Kind.INTERFACE, generatedMapper.kind, "Interface declaration is wrong");
        assertEquals("EntityNameMapper", generatedMapper.name, "The name is wrong");
        assertEquals(1, generatedMapper.annotations.size(), "Number of annotations is wrong");
        assertEquals(4, generatedMapper.methodSpecs.size(), "Number of methods is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenPackageIsCorrect() {
        MapperBuilder builder = new MapperBuilder("EntityName", "the.base.package", Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), null);
        assertEquals("the.base.package.entityname", builder.getPackage(), "The package is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenTypeNameIsCorrect() {
        TypeName typeName = MapperBuilder.getTypeName("EntityName", "the.base.package");
        assertEquals("the.base.package.entityname.EntityNameMapper", typeName.toString(), "TypeName is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenSuperinterfaceIsCorrect() {
        assertEquals("[net.cloudappi.apigen.archetypecore.core.ApigenMapper<the.base.package.entityname.EntityName>]", generatedMapper.superinterfaces.toString());
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedAnnotationIsCorrect() {
        assertEquals("@org.mapstruct.Mapper(componentModel = \"spring\", uses = {the.base.package.relatedentity.RelatedEntityMapper.class})",
                generatedMapper.annotations.get(0).toString(), "Annotation is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedToResourceMethodIsCorrect() {
        assertEquals("public abstract the.base.package.entityname.web.EntityNameOutResource toResource(\n"
                        + "    the.base.package.entityname.EntityName entity);\n",
                generatedMapper.methodSpecs.get(0).toString(), "toResource is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedListToResourceMethodIsCorrect() {
        assertEquals("public abstract java.util.List<the.base.package.entityname.web.EntityNameOutResource> toResource(\n"
                        + "    java.util.List<the.base.package.entityname.EntityName> entities);\n",
                generatedMapper.methodSpecs.get(1).toString(), "List toResource is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedSetToResourceMethodIsCorrect() {
        assertEquals("public abstract java.util.Set<the.base.package.entityname.web.EntityNameOutResource> toResource(\n"
                        + "    java.util.Set<the.base.package.entityname.EntityName> entities);\n",
                generatedMapper.methodSpecs.get(2).toString(), "Set toResource is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedUpdateBasicDataIsCorrect() {
        MethodSpec methodSpec = generatedMapper.methodSpecs.get(3);
        assertEquals("" +
                        "[" +
                        "@java.lang.Override, " +
                        "@org.mapstruct.BeanMapping(ignoreByDefault = true), " +
                        "@org.mapstruct.Mappings(@org.mapstruct.Mapping(source = \"simpleAttribute\", target = \"simpleAttribute\"))" +
                        "]",
                methodSpec.annotations.toString());
        assertEquals("[the.base.package.entityname.EntityName source, @org.mapstruct.MappingTarget the.base.package.entityname.EntityName target]"
                , methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
    }

}
