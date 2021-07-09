package org.apiaddicts.apitools.apigen.generatorcore.generator.mapper;

import com.squareup.javapoet.*;
import org.apiaddicts.apitools.apigen.archetypecore.core.ApigenMapper;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntityBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output.EntityOutputResourceBuilder;
import org.mapstruct.*;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.LITERAL;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.*;

public class MapperBuilder extends AbstractClassBuilder {

    private static final String TO_RESOURCE = "toResource";
    private static final String TO_ENTITY = "toEntity";
    private static final String UPDATE_BASIC_DATA = "updateBasicData";

    private String basePackage;
    private String entityName;
    private Set<String> basicAttributes;
    private Set<String> relatedEntitiesName;
    private Set<TypeName> resourcesToEntity;
    private Set<TypeName> entityToResources;

    private TypeName entityType;
    private TypeName idType;
    private TypeName resourceType;

    public MapperBuilder(String entityName, String basePackage, Set<String> basicAttributes, Set<String> relatedEntitiesName, Set<TypeName> resourcesToEntity, Set<TypeName> entityToResources, TypeName idType) {
        this.basePackage = basePackage;
        this.entityName = entityName;
        this.relatedEntitiesName = relatedEntitiesName;
        this.basicAttributes = basicAttributes;
        this.resourcesToEntity = resourcesToEntity;
        this.entityToResources = entityToResources;
        this.entityType = EntityBuilder.getTypeName(entityName, basePackage);
        this.idType = idType;
        this.resourceType = EntityOutputResourceBuilder.getTypeName(entityName, basePackage);
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
    public String getPackage() {
        return getPackage(entityName, basePackage);
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addMapperAnnotation();
        addEntityToResource();
        addEntityListToResource();
        addEntitySetToResource();
        addResourcesToEntity();
        addUpdateBasicData();
        addComposedIDMapping();        
        addIdToEntityMapping();
    }

    private void initializeBuilder() {
        builder = getInterface(getName(entityName))
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ApigenMapper.class), entityType));
    }

    private void addMapperAnnotation() {
        AnnotationSpec annotationSpec = AnnotationSpec.builder(Mapper.class)
                .addMember(COMPONENT_MODEL, STRING, "spring")
                .addMember(USES, LITERAL, getRelatedEntitiesCodeBlock())
                .build();
        builder.addAnnotation(annotationSpec);
    }

    private CodeBlock getRelatedEntitiesCodeBlock() {
        return relatedEntitiesName.stream()
                .sorted()
                .map(name -> MapperBuilder.getTypeName(name, basePackage))
                .map(type -> CodeBlock.of("$T.class", type))
                .collect(CodeBlock.joining(",", "{", "}"));
    }

    private void addEntityToResource() {
        if (!entityToResources.contains(resourceType)) return;
        MethodSpec methodSpec = MethodSpec.methodBuilder(TO_RESOURCE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, "entity")
                .returns(resourceType)
                .build();
        builder.addMethod(methodSpec);
    }

    private void addEntityListToResource() {
        if (!entityToResources.contains(resourceType)) return;
        MethodSpec methodSpec = MethodSpec.methodBuilder(TO_RESOURCE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterizedTypeName.get(ClassName.get(List.class), entityType), "entities")
                .returns(ParameterizedTypeName.get(ClassName.get(List.class), resourceType))
                .build();
        builder.addMethod(methodSpec);
    }

    private void addEntitySetToResource() {
        if (!entityToResources.contains(resourceType)) return;
        MethodSpec methodSpec = MethodSpec.methodBuilder(TO_RESOURCE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Set.class), entityType), "entities")
                .returns(ParameterizedTypeName.get(ClassName.get(Set.class), resourceType))
                .build();
        builder.addMethod(methodSpec);

    }

    private void addResourcesToEntity() {
        for (TypeName type : resourcesToEntity) {
            MethodSpec methodSpec = MethodSpec.methodBuilder(TO_ENTITY)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(type, "resource")
                    .returns(entityType)
                    .build();
            builder.addMethod(methodSpec);
        }
    }

    private void addUpdateBasicData() {
        MethodSpec methodSpecBuilder = MethodSpec.methodBuilder(UPDATE_BASIC_DATA)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(Override.class)
                .addAnnotation(getAnnotation(BeanMapping.class).addMember(IGNORE_BY_DEFAULT, LITERAL, true).build())
                .addAnnotations(getMappingsAnnotations())
                .addParameter(entityType, "source")
                .addParameter(ParameterSpec.builder(entityType, "target").addAnnotation(MappingTarget.class).build())
                .build();
        builder.addMethod(methodSpecBuilder);
    }

    private List<AnnotationSpec> getMappingsAnnotations() {
        return basicAttributes.stream().sorted()
                .map(attribute -> getAnnotation(Mapping.class).addMember(SOURCE, STRING, attribute).addMember(TARGET, STRING, attribute).build())
                .collect(Collectors.toList());
    }

    private void addComposedIDMapping() {
        if (!isComposed(idType)) return;
        MethodSpec mapIDToString = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addParameter(idType, "id")
                .returns(String.class)
                .addStatement("return id.toString()")
                .build();
        builder.addMethod(mapIDToString);
        MethodSpec mapStringToID = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addParameter(String.class, "id")
                .returns(idType)
                .addStatement("return $T.from(id)", idType)
                .build();
        builder.addMethod(mapStringToID);
        MethodSpec mapStringIDToEntity = MethodSpec.methodBuilder("mapToEntity")
        		.addModifiers(Modifier.PUBLIC,Modifier.DEFAULT)
        		.addParameter(String.class,"id")
        		.returns(entityType)
        		.addStatement("return new $T(map(id))",entityType)
        		.build();
        builder.addMethod(mapStringIDToEntity);
    }

    private void addIdToEntityMapping() {
        if (isComposed(idType)) return;
    	MethodSpec methodSpec = MethodSpec.methodBuilder(TO_ENTITY)
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addParameter(idType, "id")
                .returns(entityType)
                .addStatement("if (id == null) return null")
                .addStatement("return new $T(id)", entityType)
                .build();
        builder.addMethod(methodSpec);
	}

	private boolean isComposed(TypeName type) {
        return type != null && !type.toString().startsWith("java");
    }
}
