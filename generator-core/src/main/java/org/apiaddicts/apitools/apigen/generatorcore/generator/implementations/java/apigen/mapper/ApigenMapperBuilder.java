package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.mapper;

import com.squareup.javapoet.*;
import org.apiaddicts.apitools.apigen.archetypecore.core.ApigenMapper;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output.ApigenEntityOutputResourceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.mapper.MapperBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.JavaSubResourcesData;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.*;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.*;

public class ApigenMapperBuilder<C extends ApigenContext> extends MapperBuilder<C> {

    protected final TypeName resourceType;
    protected final TypeName partialUpdateResourceType;

    public ApigenMapperBuilder(Entity entity, C ctx, Configuration cfg) {
        super(entity, ctx, cfg);
        this.resourceType = ApigenEntityOutputResourceBuilder.getTypeName(entityName, basePackage);
        this.partialUpdateResourceType = this.resourcesToEntity.stream()
                .filter(it -> it.toString().contains(Endpoint.Method.PATCH.prefix))
                .findFirst().orElse(null);
        this.resourcesToEntity.remove(partialUpdateResourceType);
    }

    public static TypeName getTypeName(String entityName, String basePackage) {
        return ClassName.get(getPackage(entityName, basePackage), getName(entityName));
    }

    private static String getName(String entityName) {
        return entityName + "Mapper";
    }

    private static String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName);
    }

    @Override
    protected void initialize() {
        super.initialize();
        addUpdateBasicData();
    }

    @Override
    protected void initializeBuilder() {
        builder = getClass(getName(entityName))
                .addModifiers(Modifier.ABSTRACT)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ApigenMapper.class), entityType));
    }

    protected void addUpdateBasicData() {
        MethodSpec methodSpecBuilder = MethodSpec.methodBuilder(UPDATE_BASIC_DATA)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(Override.class)
                .addAnnotation(getAnnotation(BeanMapping.class)
                        .addMember(IGNORE_BY_DEFAULT, LITERAL, true)
                        .addMember(QUALIFIED_BY_NAME, STRING, UPDATE_BASIC_DATA)
                        .build())
                .addAnnotations(getMappingsAnnotations())
                .addParameter(entityType, "source")
                .addParameter(ParameterSpec.builder(entityType, "target").addAnnotation(MappingTarget.class).build())
                .build();
        builder.addMethod(methodSpecBuilder);
    }

    @Override
    protected void addResourcesToEntity() {
        super.addResourcesToEntity();
        if (partialUpdateResourceType != null) {
            addPartialUpdate();
            addSubResourcesToEntity();
        }
    }

    protected void addPartialUpdate() {
        MethodSpec methodSpecBuilder = MethodSpec.methodBuilder(PARTIAL_UPDATE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(getAnnotation(BeanMapping.class)
                        .addMember(NULL_VALUE_PROPERTY_MAPPING_STRATEGY, ENUM_VALUE, NullValuePropertyMappingStrategy.class, "IGNORE")
                        .build())
                .addParameter(partialUpdateResourceType, "source")
                .addParameter(ParameterSpec.builder(entityType, "target").addAnnotation(MappingTarget.class).build())
                .build();
        builder.addMethod(methodSpecBuilder);
    }

    protected void addSubResourcesToEntity() {
        for (JavaSubResourcesData subResourceToEntity : subResourcesToEntity) {
            String relatedEntity = subResourceToEntity.getRelatedEntity();
            TypeName entityTypeName = ClassName.get(getPackage(relatedEntity, basePackage), relatedEntity);
            MethodSpec methodSpec = MethodSpec.methodBuilder(MAP)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(subResourceToEntity.getSubResource(), "resource")
                    .returns(entityTypeName)
                    .addStatement("if (resource == null || resource.getId() == null) return null")
                    .addStatement("return new $T(resource.getId())", entityTypeName)
                    .build();
            builder.addMethod(methodSpec);
        }
    }

    @Override
    protected void addEntityToResources() {
        addEntityToResource();
        addEntityListToResource();
        addEntitySetToResource();
    }

    protected void addEntityToResource() {
        if (!entityToResources.contains(resourceType)) return;
        MethodSpec methodSpec = MethodSpec.methodBuilder(TO_RESOURCE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, "entity")
                .returns(resourceType)
                .build();
        builder.addMethod(methodSpec);
    }

    protected void addEntityListToResource() {
        if (!entityToResources.contains(resourceType)) return;
        MethodSpec methodSpec = MethodSpec.methodBuilder(TO_RESOURCE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterizedTypeName.get(ClassName.get(List.class), entityType), "entities")
                .returns(ParameterizedTypeName.get(ClassName.get(List.class), resourceType))
                .build();
        builder.addMethod(methodSpec);
    }

    protected void addEntitySetToResource() {
        if (!entityToResources.contains(resourceType)) return;
        MethodSpec methodSpec = MethodSpec.methodBuilder(TO_RESOURCE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(getAnnotation(BeanMapping.class)
                        .addMember(QUALIFIED_BY_NAME, STRING, TO_RESOURCE)
                        .build())
                .addParameter(ParameterizedTypeName.get(ClassName.get(Set.class), entityType), "entities")
                .returns(ParameterizedTypeName.get(ClassName.get(Set.class), resourceType))
                .build();
        builder.addMethod(methodSpec);
    }

    protected List<AnnotationSpec> getMappingsAnnotations() {
        return basicAttributes.stream().sorted()
                .map(attribute -> getAnnotation(Mapping.class).addMember(SOURCE, STRING, attribute).addMember(TARGET, STRING, attribute).build())
                .collect(Collectors.toList());
    }
}
