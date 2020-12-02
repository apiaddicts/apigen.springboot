package net.cloudappi.apigen.generatorcore.generator.mapper;

import com.squareup.javapoet.*;
import net.cloudappi.apigen.archetypecore.core.ApigenMapper;
import net.cloudappi.apigen.generatorcore.generator.common.AbstractClassBuilder;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntityBuilder;
import net.cloudappi.apigen.generatorcore.generator.web.resource.output.EntityOutputResourceBuilder;
import org.mapstruct.*;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Set;

import static net.cloudappi.apigen.generatorcore.generator.common.Formats.LITERAL;
import static net.cloudappi.apigen.generatorcore.generator.common.Formats.STRING;
import static net.cloudappi.apigen.generatorcore.generator.common.Members.*;

public class MapperBuilder extends AbstractClassBuilder {

    private static final String TO_RESOURCE = "toResource";
    private static final String TO_ENTITY = "toEntity";
    private static final String UPDATE_BASIC_DATA = "updateBasicData";

    private String basePackage;
    private String entityName;
    private Set<String> basicAttributes;
    private Set<String> relatedEntitiesName;
    private Set<TypeName> resourcesToEntity;

    private TypeName entityType;
    private TypeName composedIdType;
    private TypeName resourceType;

    public MapperBuilder(String entityName, String basePackage, Set<String> basicAttributes, Set<String> relatedEntitiesName, Set<TypeName> resourcesToEntity, TypeName composedIdType) {
        this.basePackage = basePackage;
        this.entityName = entityName;
        this.relatedEntitiesName = relatedEntitiesName;
        this.basicAttributes = basicAttributes;
        this.resourcesToEntity = resourcesToEntity;
        this.entityType = EntityBuilder.getTypeName(entityName, basePackage);
        this.composedIdType = composedIdType;
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
        MethodSpec methodSpec = MethodSpec.methodBuilder(TO_RESOURCE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, "entity")
                .returns(resourceType)
                .build();
        builder.addMethod(methodSpec);
    }

    private void addEntityListToResource() {
        MethodSpec methodSpec = MethodSpec.methodBuilder(TO_RESOURCE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterizedTypeName.get(ClassName.get(List.class), entityType), "entities")
                .returns(ParameterizedTypeName.get(ClassName.get(List.class), resourceType))
                .build();
        builder.addMethod(methodSpec);
    }

    private void addEntitySetToResource() {
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
                .addAnnotation(getMappingsAnnotation())
                .addParameter(entityType, "source")
                .addParameter(ParameterSpec.builder(entityType, "target").addAnnotation(MappingTarget.class).build())
                .build();
        builder.addMethod(methodSpecBuilder);
    }

    private AnnotationSpec getMappingsAnnotation() {
        AnnotationSpec.Builder annotationBuilder = getAnnotation(Mappings.class);
        basicAttributes.stream().sorted()
                .map(attribute -> getAnnotation(Mapping.class).addMember(SOURCE, STRING, attribute).addMember(TARGET, STRING, attribute).build())
                .forEach(annotationSpec -> annotationBuilder.addMember(VALUE, LITERAL, annotationSpec));
        return annotationBuilder.build();
    }

    private void addComposedIDMapping() {
        if (composedIdType == null) return;
        MethodSpec mapIDToString = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addParameter(composedIdType, "id")
                .returns(String.class)
                .addStatement("return id.toString()")
                .build();
        builder.addMethod(mapIDToString);
        MethodSpec mapStringToID = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addParameter(String.class, "id")
                .returns(composedIdType)
                .addStatement("return $T.from(id)", composedIdType)
                .build();
        builder.addMethod(mapStringToID);
    }
}
