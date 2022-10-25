package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.service;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.apiaddicts.apitools.apigen.archetypecore.core.AbstractRelationsManager;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrors;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrorsException;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.persistence.AttributeData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntityBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.service.ServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.ENUM_VALUE;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.PROPAGATION;

public class ApigenRelationManagerBuilder<C extends ApigenContext> extends AbstractJavaClassBuilder<C> {

    protected final String basePackage;
    protected final String entityName;
    protected final List<String> sortedRelatedEntities;
    protected final Map<String, AttributeData<TypeName>> attributes;

    public ApigenRelationManagerBuilder(Entity entity, C ctx, Configuration cfg) {
        super(ctx, cfg);
        this.entityName = entity.getName();
        this.basePackage = cfg.getBasePackage();
        this.attributes = ctx.getEntitiesData().getAttributes(entityName);
        this.sortedRelatedEntities = attributes.values().stream()
                .filter(AttributeData::isOwned)
                .map(AttributeData::getRelatedEntity)
                .distinct().sorted().collect(Collectors.toList());
    }

    public static TypeName getTypeName(String entityName, String basePackage) {
        return ClassName.get(getPackage(entityName, basePackage), getName(entityName));
    }

    private static String getName(String entityName) {
        return entityName + "RelationManager";
    }

    private static String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName);
    }

    @Override
    public String getPackage() {
        return getPackage(this.entityName, this.basePackage);
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addServiceFields();
        addCreateOrRetrieveRelationsMethod();
        addUpdateRelationsMethod();
    }

    private void initializeBuilder() {
        TypeName entityType = EntityBuilder.getTypeName(entityName, basePackage);
        builder = getClass(getName(entityName)).superclass(
                ParameterizedTypeName.get(ClassName.get(AbstractRelationsManager.class), entityType))
                .addAnnotation(Component.class);
    }

    private void addServiceFields() {
        sortedRelatedEntities.forEach(relatedEntityName -> {
            TypeName serviceType = ServiceBuilder.getTypeName(relatedEntityName, basePackage);
            String serviceName = StringUtils.uncapitalize(
                    ServiceBuilder.getName(relatedEntityName));
            FieldSpec fieldSpec = FieldSpec.builder(serviceType, serviceName, Modifier.PROTECTED)
                    .addAnnotation(Autowired.class).build();
            builder.addField(fieldSpec);
        });
    }

    private void addCreateOrRetrieveRelationsMethod() {
        TypeName entityType = EntityBuilder.getTypeName(entityName, basePackage);
        String paramName = StringUtils.uncapitalize(entityName);
        List<String> sortedAttributes = attributes.keySet().stream().sorted().collect(Collectors.toList());
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("createOrRetrieveRelations")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(getAnnotation(Transactional.class).addMember(PROPAGATION, ENUM_VALUE, Propagation.class, Propagation.MANDATORY.name()).build())
                .addParameter(entityType, paramName);
        methodSpecBuilder.addStatement("$1T errors = new $1T()", RelationalErrors.class);
        List<MethodSpec.Builder> fieldMethodBuilders = new ArrayList<>(sortedAttributes.size());
        for (String attribute : sortedAttributes) {
            AttributeData<?> attributeData = attributes.get(attribute);
            if (!attributeData.isOwned()) continue;
            String capAttribute = StringUtils.capitalize(attribute);
            String serviceName = StringUtils.uncapitalize(
                    ServiceBuilder.getName(attributeData.getRelatedEntity()));
            methodSpecBuilder.addStatement("createOrRetrieveRelations$2L($1L, errors)", paramName, capAttribute);

            MethodSpec.Builder fieldMethodBuilder = MethodSpec.methodBuilder("createOrRetrieveRelations" + capAttribute)
                    .addModifiers(Modifier.PROTECTED)
                    .addParameter(entityType, paramName)
                    .addParameter(RelationalErrors.class, "errors");
            fieldMethodBuilder.addStatement("$1L.set$2L(createOrRetrieve($1L.get$2L(), $3L, errors))", paramName, capAttribute, serviceName);
            fieldMethodBuilders.add(fieldMethodBuilder);
        }
        methodSpecBuilder.beginControlFlow("if (!errors.isEmpty())");
        methodSpecBuilder.addStatement("throw new $T(errors)", RelationalErrorsException.class);
        methodSpecBuilder.endControlFlow();

        builder.addMethod(methodSpecBuilder.build());
        fieldMethodBuilders.forEach(b -> builder.addMethod(b.build()));
    }

    private void addUpdateRelationsMethod() {
        TypeName entityType = EntityBuilder.getTypeName(entityName, basePackage);
        String persistedParamName = "persisted" + entityName;
        String paramName = StringUtils.uncapitalize(entityName);
        List<String> sortedAttributes = attributes.keySet().stream().sorted().collect(Collectors.toList());
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("updateRelations")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(getAnnotation(Transactional.class).addMember(PROPAGATION, ENUM_VALUE, Propagation.class, Propagation.MANDATORY.name()).build())
                .addParameter(entityType, persistedParamName)
                .addParameter(entityType, paramName)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Set.class), ClassName.get(String.class)), "fields");

        methodSpecBuilder.addStatement("$1T errors = new $1T()", RelationalErrors.class);
        methodSpecBuilder.addStatement("boolean updateAll = (fields == null)");
        List<MethodSpec.Builder> fieldMethodBuilders = new ArrayList<>(sortedAttributes.size());
        for (String attribute : sortedAttributes) {
            AttributeData<?> attributeData = attributes.get(attribute);
            if (!attributeData.isOwned()) continue;
            String capAttribute = StringUtils.capitalize(attribute);
            String serviceName = StringUtils.uncapitalize(ServiceBuilder.getName(attributeData.getRelatedEntity()));

            methodSpecBuilder.beginControlFlow("if (updateAll || fields.contains($S))", attribute);
            methodSpecBuilder.addStatement("updateRelations$3L($1L, $2L, fields, errors)", persistedParamName, paramName, capAttribute);
            methodSpecBuilder.endControlFlow();

            MethodSpec.Builder fieldMethodBuilder = MethodSpec.methodBuilder("updateRelations" + capAttribute)
                    .addModifiers(Modifier.PROTECTED)
                    .addParameter(entityType, persistedParamName)
                    .addParameter(entityType, paramName)
                    .addParameter(ParameterizedTypeName.get(ClassName.get(Set.class), ClassName.get(String.class)), "fields")
                    .addParameter(RelationalErrors.class, "errors");

            if (attributeData.isCollection()) {
                fieldMethodBuilder.addStatement("replace($1L.get$3L(), retrieve($2L.get$3L(), $4L, errors)",
                        persistedParamName, paramName, capAttribute, serviceName);
            } else {
                fieldMethodBuilder.addStatement("$1L.set$3L(retrieve($2L.get$3L(), $4L, errors))", persistedParamName, paramName, capAttribute, serviceName);
            }

            fieldMethodBuilders.add(fieldMethodBuilder);
        }
        methodSpecBuilder.beginControlFlow("if (!errors.isEmpty())");
        methodSpecBuilder.addStatement("throw new $T(errors)", RelationalErrorsException.class);
        methodSpecBuilder.endControlFlow();

        builder.addMethod(methodSpecBuilder.build());
        fieldMethodBuilders.forEach(b -> builder.addMethod(b.build()));
    }
}
