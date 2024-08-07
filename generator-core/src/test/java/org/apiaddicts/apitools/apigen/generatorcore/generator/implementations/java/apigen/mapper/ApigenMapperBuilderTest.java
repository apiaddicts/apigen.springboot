package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.mapper;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output.ApigenEntityOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.ComposedIdBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.JavaSubResourcesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input.GenericInputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApigenMapperBuilderTest {

    private static ApigenMapperBuilder<ApigenContext> builder;
    private static TypeSpec generatedMapper;
    private static TypeSpec generatedMapperWithComposedId;
    private static TypeSpec generatedMapperPatch;

    @BeforeAll
    static void prepareTest() {
        Configuration cfg = ConfigurationObjectMother.create();
        Entity basicIdEntity = EntityObjectMother.createEntityWithAutogeneratedLongId();
        basicIdEntity.setName("EntityName");
        Entity composedIdEntity = EntityObjectMother.createSimpleEntityWithComposedID();
        composedIdEntity.setName("EntityNameWithComposedID");
        Entity petEntity = EntityObjectMother.createEntityWithAutogeneratedLongId();
        petEntity.setName("Pet");

        builder = ApigenMapperBuilderObjectMother.create(
                basicIdEntity,
                TypeName.get(Long.class),
                Collections.singleton("RelatedEntity"),
                Collections.singleton("simpleAttribute"),
                Collections.emptySet(),
                Collections.singleton(ApigenEntityOutputResourceBuilder.getTypeName(basicIdEntity.getName(), cfg.getBasePackage())),
                new ArrayList<>()
        );
        generatedMapper = builder.build();
        generatedMapperWithComposedId = ApigenMapperBuilderObjectMother.create(
                composedIdEntity,
                ComposedIdBuilder.getTypeName(composedIdEntity.getName(), cfg.getBasePackage()),
                Collections.singleton("RelatedEntity"),
                Collections.singleton("simpleAttribute"),
                Collections.emptySet(),
                Collections.singleton(ApigenEntityOutputResourceBuilder.getTypeName(composedIdEntity.getName(), cfg.getBasePackage())),
                new ArrayList<>()
        ).build();

        List<JavaSubResourcesData> subResources = new ArrayList<>();
        TypeName entityFieldNameOwner = ClassName.get("the.group.artifact.pet.web", "PartialUpdatePetByIdResource.OwnerSubresource");
        TypeName entityFieldNameTag = ClassName.get("the.group.artifact.pet.web", "PartialUpdatePetByIdResource.TagSubresource");

        subResources.add(new JavaSubResourcesData("Owner", entityFieldNameOwner));
        subResources.add(new JavaSubResourcesData("Tag", entityFieldNameTag));

        Endpoint endpoint = EndpointObjectMother.standardPatch("partialUpdate", petEntity.getName());

        generatedMapperPatch = ApigenMapperBuilderObjectMother.create(
                petEntity,
                TypeName.get(Long.class),
                new HashSet<>(Arrays.asList("Owner", "Tag")),
                Collections.singleton("name"),
                new HashSet<>(Collections.singleton(GenericInputResourceBuilder.getTypeName(endpoint, cfg.getBasePackage()))),
                Collections.singleton(ApigenEntityOutputResourceBuilder.getTypeName(petEntity.getName(), cfg.getBasePackage())),
                subResources
        ).build();
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenFileStructureIsCorrect() {
        assertEquals(Modifier.PUBLIC, generatedMapper.modifiers.toArray()[0], "Public modifier is wrong");
        assertEquals(TypeSpec.Kind.CLASS, generatedMapper.kind, "Class declaration is wrong");
        assertEquals("EntityNameMapper", generatedMapper.name, "The name is wrong");
        assertEquals(1, generatedMapper.annotations.size(), "Number of annotations is wrong");
        assertEquals(5, generatedMapper.methodSpecs.size(), "Number of methods is wrong");
    }

    @Test
    void givenValidAttributesWithComposedID_whenBuildMapper_thenMapMethodsAreCorrect() {
        assertEquals(7, generatedMapperWithComposedId.methodSpecs.size(), "Number of methods is wrong");
        assertEquals(
                "public java.lang.String map(\n" + "    the.group.artifact.entitynamewithcomposedid" +
                        ".EntityNameWithComposedIDID id) {\n" + "  return id.toString();\n" + "}\n" + "",
                generatedMapperWithComposedId.methodSpecs.get(3).toString(), "The map method is wrong");
        assertEquals(
                "public the.group.artifact.entitynamewithcomposedid.EntityNameWithComposedIDID map(\n" + "    java" +
                        ".lang.String id) {\n" + "  return the.group.artifact.entitynamewithcomposedid" +
                        ".EntityNameWithComposedIDID.from(id);\n" + "}\n" + "",
                generatedMapperWithComposedId.methodSpecs.get(4).toString(), "The map method is wrong");
        assertEquals(
                "public the.group.artifact.entitynamewithcomposedid.EntityNameWithComposedID mapToEntity(\n" + "    " +
                        "java.lang.String id) {\n" + "  return new the.group.artifact.entitynamewithcomposedid" +
                        ".EntityNameWithComposedID(map(id));\n" + "}\n" + "",
                generatedMapperWithComposedId.methodSpecs.get(5).toString(),
                "The map method to transform an id to an entity is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenPackageIsCorrect() {
        assertEquals("the.group.artifact.entityname", builder.getPackage(), "The package is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenTypeNameIsCorrect() {
        TypeName typeName = ApigenMapperBuilder.getTypeName("EntityName", "the.group.artifact");
        assertEquals("the.group.artifact.entityname.EntityNameMapper", typeName.toString(), "TypeName is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenSuperinterfaceIsCorrect() {
        assertEquals(
                "[org.apiaddicts.apitools.apigen.archetypecore.core.ApigenMapper<the.group.artifact.entityname" +
                        ".EntityName>]",
                generatedMapper.superinterfaces.toString());
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedAnnotationIsCorrect() {
        assertEquals(
                "@org.mapstruct.Mapper(componentModel = \"spring\", uses = {" +
                        "org.apiaddicts.apitools.apigen.archetypecore.core.JsonNullableMapper.class, " +
                        "the.group.artifact.relatedentity.RelatedEntityMapper.class})",
                generatedMapper.annotations.get(0).toString(), "Annotation is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedToResourceMethodIsCorrect() {
        assertEquals(
                "public abstract the.group.artifact.entityname.web.EntityNameOutResource toResource(\n" + "    the" +
                        ".group.artifact.entityname.EntityName entity);\n",
                generatedMapper.methodSpecs.get(0).toString(), "toResource is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedListToResourceMethodIsCorrect() {
        assertEquals(
                "public abstract java.util.List<the.group.artifact.entityname.web.EntityNameOutResource> toResource" +
                        "(\n" + "    java.util.List<the.group.artifact.entityname.EntityName> entities);\n",
                generatedMapper.methodSpecs.get(1).toString(), "List toResource is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedSetToResourceMethodIsCorrect() {
        assertEquals(
                "@org.mapstruct.BeanMapping(\n" + "    qualifiedByName = \"toResource\"\n" + ")\n" + "public abstract" +
                        " java.util.Set<the.group.artifact.entityname.web.EntityNameOutResource> toResource(\n" + "  " +
                        "  java.util.Set<the.group.artifact.entityname.EntityName> entities);\n",
                generatedMapper.methodSpecs.get(2).toString(), "Set toResource is wrong");
    }

    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedUpdateBasicDataIsCorrect() {
        MethodSpec methodSpec = generatedMapper.methodSpecs.get(4);
        assertEquals(
                "" + "[" + "@java.lang.Override, " + "@org.mapstruct.BeanMapping(ignoreByDefault = true, " +
                        "qualifiedByName = \"updateBasicData\"), " + "@org.mapstruct.Mapping(source = " +
                        "\"simpleAttribute\", target = \"simpleAttribute\")" + "]",
                methodSpec.annotations.toString());
        assertEquals(
                "[the.group.artifact.entityname.EntityName source, @org.mapstruct.MappingTarget the.group.artifact" +
                        ".entityname.EntityName target]",
                methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
    }

    @Test
    void givenMultipleValidAttributes_whenBuildMapper_thenGeneratedUpdateBasicDataIsCorrect() {
        Entity entity = EntityObjectMother.createEntityWithAutogeneratedLongId();
        entity.setName("EntityName");
        TypeSpec spec = ApigenMapperBuilderObjectMother.create(
                entity,
                TypeName.get(String.class),
                Collections.singleton("RelatedEntity"),
                new HashSet<>(Arrays.asList("one", "two")),
                Collections.emptySet(),
                Collections.emptySet(),
                new ArrayList<JavaSubResourcesData>()
        ).build();
        MethodSpec methodSpec = spec.methodSpecs.get(1);
        assertEquals(
                "" + "[" + "@java.lang.Override, " + "@org.mapstruct.BeanMapping(ignoreByDefault = true, " +
                        "qualifiedByName = \"updateBasicData\"), " + "@org.mapstruct.Mapping(source = \"one\", target" +
                        " = \"one\"), " + "@org.mapstruct.Mapping(source = \"two\", target = \"two\")" + "]",
                methodSpec.annotations.toString());
        assertEquals(
                "[the.group.artifact.entityname.EntityName source, @org.mapstruct.MappingTarget the.group.artifact" +
                        ".entityname.EntityName target]",
                methodSpec.parameters.toString());
        assertEquals("void", methodSpec.returnType.toString());
    }


    @Test
    void givenValidAttributes_whenBuildMapper_thenGeneratedIdToEntityIsCorrect() {
        assertEquals(
                "public the.group.artifact.entityname.EntityName toEntity(java.lang.Long id) {\n" + "  if (id == " +
                        "null) return null;\n" + "  return new the.group.artifact.entityname.EntityName(id);\n" + "}\n",
                generatedMapper.methodSpecs.get(3).toString(), "idToEntity is wrong");
    }

    @Test
    void givenValidAttributesPatch_whenBuildMapper_thenMapAndPartialUpdateMethodsAreCorrect() {
        assertEquals(8, generatedMapperPatch.methodSpecs.size(), "Number of methods is wrong");

        assertEquals(
                "@org.mapstruct.BeanMapping(\n" +
                        "    nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE\n" + ")\n" +
                        "public abstract void partialUpdate(the.group.artifact.pet.web.PartialUpdatePetByIdResource source,\n" +
                        "    @org.mapstruct.MappingTarget the.group.artifact.pet.Pet target);\n",
                generatedMapperPatch.methodSpecs.get(0).toString(), "The partialUpdate method is wrong");

        assertEquals(
                "public the.group.artifact.owner.Owner map(\n" +
                        "    the.group.artifact.pet.web.PartialUpdatePetByIdResource.OwnerSubresource resource) {\n" +
                        "  if (resource == null || resource.getId() == null) return null;\n" +
                        "  return new the.group.artifact.owner.Owner(resource.getId());\n" +
                        "}\n",
                generatedMapperPatch.methodSpecs.get(1).toString(), "The map method is wrong");

        assertEquals(
                "public the.group.artifact.tag.Tag map(\n" +
                        "    the.group.artifact.pet.web.PartialUpdatePetByIdResource.TagSubresource resource) {\n" +
                        "  if (resource == null || resource.getId() == null) return null;\n" +
                        "  return new the.group.artifact.tag.Tag(resource.getId());\n" +
                        "}\n",
                generatedMapperPatch.methodSpecs.get(2).toString(), "The map method is wrong");
    }
}
