package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.mapper;

import com.squareup.javapoet.*;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntityBuilder;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

import javax.lang.model.element.Modifier;
import java.util.Set;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.LITERAL;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.*;

public class MapperBuilder<C extends JavaContext> extends AbstractJavaClassBuilder<C> {

    protected static final String TO_RESOURCE = "toResource";
    protected static final String TO_ENTITY = "toEntity";
    protected static final String UPDATE_BASIC_DATA = "updateBasicData";

    protected final String basePackage;
    protected final String entityName;
    protected final Set<String> basicAttributes;
    protected final Set<String> relatedEntitiesName;
    protected final Set<TypeName> resourcesToEntity;
    protected final Set<TypeName> entityToResources;

    protected final TypeName entityType;
    protected final TypeName idType;

    public MapperBuilder(Entity entity, C ctx, Configuration cfg) {
        super(ctx, cfg);
        this.basePackage = cfg.getBasePackage();
        this.entityName = entity.getName();
        this.relatedEntitiesName = ctx.getEntitiesData().getRelatedEntities(entityName);
        this.basicAttributes = ctx.getEntitiesData().getBasicAttributes(entityName);
        this.resourcesToEntity = ctx.getResourcesData().getInputResources(entityName);
        this.entityToResources = ctx.getResourcesData().getOutputResources(entityName);
        this.entityType = EntityBuilder.getTypeName(entityName, basePackage);
        this.idType = ctx.getEntitiesData().getIDType(entityName);
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
        addResourcesToEntity();
        addEntityToResources();
        addComposedIDMapping();
        addIdToEntityMapping();
    }

    protected void initializeBuilder() {
        builder = getClass(getName(entityName))
                .addModifiers(Modifier.ABSTRACT);
    }

    protected void addMapperAnnotation() {
        AnnotationSpec annotationSpec = AnnotationSpec.builder(Mapper.class)
                .addMember(COMPONENT_MODEL, STRING, "spring")
                .addMember(USES, LITERAL, getRelatedEntitiesCodeBlock())
                .build();
        builder.addAnnotation(annotationSpec);
    }

    protected CodeBlock getRelatedEntitiesCodeBlock() {
        return relatedEntitiesName.stream()
                .sorted()
                .map(name -> MapperBuilder.getTypeName(name, basePackage))
                .map(type -> CodeBlock.of("$T.class", type))
                .collect(CodeBlock.joining(",", "{", "}"));
    }


    protected void addResourcesToEntity() {
        for (TypeName type : resourcesToEntity) {
            MethodSpec methodSpec = MethodSpec.methodBuilder(TO_ENTITY)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addAnnotation(getAnnotation(BeanMapping.class)
                            .addMember(QUALIFIED_BY_NAME, STRING, TO_ENTITY)
                            .build())
                    .addParameter(type, "resource")
                    .returns(entityType)
                    .build();
            builder.addMethod(methodSpec);
        }
    }

    protected void addEntityToResources() {
        for (TypeName type : entityToResources) {
            MethodSpec methodSpec = MethodSpec.methodBuilder(TO_RESOURCE)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addAnnotation(getAnnotation(BeanMapping.class)
                            .addMember(QUALIFIED_BY_NAME, STRING, TO_RESOURCE)
                            .build())
                    .addParameter(entityType, "entity")
                    .returns(type)
                    .build();
            builder.addMethod(methodSpec);
        }
    }

    protected void addComposedIDMapping() {
        if (!isComposed(idType)) return;
        MethodSpec mapIDToString = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(idType, "id")
                .returns(String.class)
                .addStatement("return id.toString()")
                .build();
        builder.addMethod(mapIDToString);
        MethodSpec mapStringToID = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "id")
                .returns(idType)
                .addStatement("return $T.from(id)", idType)
                .build();
        builder.addMethod(mapStringToID);
        MethodSpec mapStringIDToEntity = MethodSpec.methodBuilder("mapToEntity")
        		.addModifiers(Modifier.PUBLIC)
        		.addParameter(String.class,"id")
        		.returns(entityType)
        		.addStatement("return new $T(map(id))",entityType)
        		.build();
        builder.addMethod(mapStringIDToEntity);
    }

    protected void addIdToEntityMapping() {
        if (isComposed(idType)) return;
    	MethodSpec methodSpec = MethodSpec.methodBuilder(TO_ENTITY)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(idType, "id")
                .returns(entityType)
                .addStatement("if (id == null) return null")
                .addStatement("return new $T(id)", entityType)
                .build();
        builder.addMethod(methodSpec);
	}

    protected boolean isComposed(TypeName type) {
        return type != null && !type.toString().startsWith("java");
    }
}
