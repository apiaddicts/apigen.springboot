package org.apiaddicts.apitools.apigen.generatorcore.generator.mapper;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apiaddicts.apitools.apigen.generatorcore.config.mapper.MapperBuilderObjectMother;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperBuilderTest {

    private static TypeSpec generatedMapper;
    private static TypeSpec generatedMapperWithComposedId;

    @BeforeAll
    static void prepareTest() {
        generatedMapper = MapperBuilderObjectMother
                .createMapperWithDefaultOutResource("EntityName", "the.base.package", "simpleAttribute", "RelatedEntity")
                .build();
        generatedMapperWithComposedId = MapperBuilderObjectMother
        		.createMapperWithComposedID("EntityNameWithComposedID", "the.base.package", "simpleAttribute", "RelatedEntity")
        		.build();
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenFileStructureIsCorrect() {
        assertEquals(Modifier.PUBLIC, generatedMapper.modifiers.toArray()[0], "Public modifier is wrong");
        assertEquals(TypeSpec.Kind.INTERFACE, generatedMapper.kind, "Interface declaration is wrong");
        assertEquals("EntityNameMapper", generatedMapper.name, "The name is wrong");
        assertEquals(1, generatedMapper.annotations.size(), "Number of annotations is wrong");
        assertEquals(5, generatedMapper.methodSpecs.size(), "Number of methods is wrong");
    }
    
    @Test
    void givenValidAttributesWithComposedID_whenBuildMapper_thenMapMethodsAreCorrect() {
    	assertEquals(7, generatedMapperWithComposedId.methodSpecs.size(), "Number of methods is wrong");
    	assertEquals("public default java.lang.String map(\n" + 
    			"    the.base.package.entitynamewithcomposedid.EntityNameWithComposedIDID id) {\n" +
    			"  return id.toString();\n" + 
    			"}\n" + 
    			"",generatedMapperWithComposedId.methodSpecs.get(4).toString(),"The map method is wrong");
    	assertEquals("public default the.base.package.entitynamewithcomposedid.EntityNameWithComposedIDID map(\n" +
    			"    java.lang.String id) {\n" + 
    			"  return the.base.package.entitynamewithcomposedid.EntityNameWithComposedIDID.from(id);\n" +
    			"}\n" + 
    			"",generatedMapperWithComposedId.methodSpecs.get(5).toString(),"The map method is wrong");
    	assertEquals("public default the.base.package.entitynamewithcomposedid.EntityNameWithComposedID mapToEntity(\n" +
    			"    java.lang.String id) {\n" + 
    			"  return new the.base.package.entitynamewithcomposedid.EntityNameWithComposedID(map(id));\n" + 
    			"}\n" + 
    			"",generatedMapperWithComposedId.methodSpecs.get(6).toString(),"The map method to transform an id to an entity is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenPackageIsCorrect() {
        MapperBuilder builder = new MapperBuilder("EntityName", "the.base.package", Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(),null);
        assertEquals("the.base.package.entityname", builder.getPackage(), "The package is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenTypeNameIsCorrect() {
        TypeName typeName = MapperBuilder.getTypeName("EntityName", "the.base.package");
        assertEquals("the.base.package.entityname.EntityNameMapper", typeName.toString(), "TypeName is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenSuperinterfaceIsCorrect() {
        assertEquals("[org.apiaddicts.apitools.apigen.archetypecore.core.ApigenMapper<the.base.package.entityname.EntityName>]", generatedMapper.superinterfaces.toString());
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
                        "@org.mapstruct.Mapping(source = \"simpleAttribute\", target = \"simpleAttribute\")" +
                        "]",
                methodSpec.annotations.toString());
        assertEquals("[the.base.package.entityname.EntityName source, @org.mapstruct.MappingTarget the.base.package.entityname.EntityName target]"
                , methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
    }

    @Test
    void givenMultipleValidAttributes_whenBuildMapper_thenGeneratedUpdateBasicDataIsCorrect() {
        TypeSpec spec = new MapperBuilder("EntityName", "the.base.package", new HashSet<>(Arrays.asList("one", "two")), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), TypeName.get(String.class)).build();
        MethodSpec methodSpec = spec.methodSpecs.get(0);
        assertEquals("" +
                        "[" +
                        "@java.lang.Override, " +
                        "@org.mapstruct.BeanMapping(ignoreByDefault = true), " +
                        "@org.mapstruct.Mapping(source = \"one\", target = \"one\"), " +
                        "@org.mapstruct.Mapping(source = \"two\", target = \"two\")" +
                        "]",
                methodSpec.annotations.toString());
        assertEquals("[the.base.package.entityname.EntityName source, @org.mapstruct.MappingTarget the.base.package.entityname.EntityName target]"
                , methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
    }
    

    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedIdToEntityIsCorrect() {
    	assertEquals("public default the.base.package.entityname.EntityName toEntity(java.lang.Long id) {\n"
    			+ "  if (id == null) return null;\n"
    			+ "  return new the.base.package.entityname.EntityName(id);\n"
    			+ "}\n",
                generatedMapper.methodSpecs.get(4).toString(), "idToEntity is wrong");
    }
}
