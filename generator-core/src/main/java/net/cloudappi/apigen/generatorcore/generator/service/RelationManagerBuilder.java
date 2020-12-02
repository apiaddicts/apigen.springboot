package net.cloudappi.apigen.generatorcore.generator.service;

import com.squareup.javapoet.*;
import net.cloudappi.apigen.archetypecore.core.AbstractRelationsManager;
import net.cloudappi.apigen.archetypecore.exceptions.RelationalErrors;
import net.cloudappi.apigen.archetypecore.exceptions.RelationalErrorsException;
import net.cloudappi.apigen.generatorcore.generator.common.AbstractClassBuilder;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntitiesData.AttributeData;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntityBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.cloudappi.apigen.generatorcore.generator.common.Formats.ENUM_VALUE;
import static net.cloudappi.apigen.generatorcore.generator.common.Members.PROPAGATION;

public class RelationManagerBuilder extends AbstractClassBuilder {

    private String basePackage;
    private String entityName;
    private List<String> sortedRelatedEntities;
    private Map<String, AttributeData> attributes;

    public RelationManagerBuilder(String entityName, String basePackage, Map<String, AttributeData> attributes) {
        this.entityName = entityName;
        this.basePackage = basePackage;
        this.attributes = attributes;
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
        addCreateOrRetrieveMethods();
        addRetrieveMethods();
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
            String serviceName = StringUtils.uncapitalize(ServiceBuilder.getName(relatedEntityName));
            FieldSpec fieldSpec = FieldSpec.builder(serviceType, serviceName, Modifier.PRIVATE)
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
        for (String attribute : sortedAttributes) {
            AttributeData attributeData = attributes.get(attribute);
            if (!attributeData.isOwned()) continue;
            String capAttribute = StringUtils.capitalize(attribute);

            methodSpecBuilder.beginControlFlow("if ($L.get$L() != null)", paramName, capAttribute);
            if (attributeData.isCollection()) {
                methodSpecBuilder.addStatement("$1L.set$2L($1L.get$2L().stream().map(e -> createOrRetrieve(e, errors)).collect($3T.toSet()))",
                        paramName, capAttribute, Collectors.class);
            } else {
                methodSpecBuilder.addStatement("$1L.set$2L(createOrRetrieve($1L.get$2L(), errors))", paramName, capAttribute);
            }
            methodSpecBuilder.endControlFlow();
        }
        methodSpecBuilder.beginControlFlow("if (!errors.isEmpty())");
        methodSpecBuilder.addStatement("throw new $T(errors)", RelationalErrorsException.class);
        methodSpecBuilder.endControlFlow();

        builder.addMethod(methodSpecBuilder.build());
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
        for (String attribute : sortedAttributes) {
            AttributeData attributeData = attributes.get(attribute);
            if (!attributeData.isOwned()) continue;
            String capAttribute = StringUtils.capitalize(attribute);

            methodSpecBuilder.beginControlFlow("if (updateAll || fields.contains($S))", attribute);
            methodSpecBuilder.beginControlFlow("if ($L.get$L() != null)", paramName, capAttribute);
            if (attributeData.isCollection()) {
                methodSpecBuilder.addStatement("$1L.get$2L().clear()", persistedParamName, capAttribute);
                methodSpecBuilder.addStatement("$1L.get$3L().addAll($2L.get$3L().stream().map(e -> retrieve(e, errors)).collect($4T.toSet()))",
                        persistedParamName, paramName, capAttribute, Collectors.class);
            } else {
                methodSpecBuilder.addStatement("$1L.set$3L(retrieve($2L.get$3L(), errors))", persistedParamName, paramName, capAttribute);
            }
            methodSpecBuilder.nextControlFlow("else");
            methodSpecBuilder.addStatement("$L.set$L(null)", persistedParamName, capAttribute);
            methodSpecBuilder.endControlFlow();
            methodSpecBuilder.endControlFlow();
        }
        methodSpecBuilder.beginControlFlow("if (!errors.isEmpty())");
        methodSpecBuilder.addStatement("throw new $T(errors)", RelationalErrorsException.class);
        methodSpecBuilder.endControlFlow();

        builder.addMethod(methodSpecBuilder.build());
    }

    private void addCreateOrRetrieveMethods() {
        sortedRelatedEntities.forEach(this::addCreateOrRetrieveMethod);
    }

    private void addCreateOrRetrieveMethod(String entityName) {
        String serviceName = StringUtils.uncapitalize(ServiceBuilder.getName(entityName));
        TypeName entityType = EntityBuilder.getTypeName(entityName, basePackage);
        String paramName = StringUtils.uncapitalize(entityName);
        MethodSpec methodSpec = MethodSpec.methodBuilder("createOrRetrieve")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(entityType, paramName)
                .addParameter(RelationalErrors.class, "errors")
                .returns(entityType)
                .beginControlFlow("if ($L.isReference())", paramName)
                .addStatement("return retrieve($L, errors)", paramName)
                .nextControlFlow("else", paramName)
                .beginControlFlow("try")
                .addStatement("return $L.create($L)", serviceName, paramName)
                .nextControlFlow("catch ($T e)", RelationalErrorsException.class)
                .addStatement("errors.merge(e.getRelationalErrors())")
                .addStatement("return null")
                .endControlFlow()
                .endControlFlow()
                .build();
        builder.addMethod(methodSpec);
    }

    private void addRetrieveMethods() {
        sortedRelatedEntities.forEach(this::addRetrieveMethod);
    }

    private void addRetrieveMethod(String entityName) {
        String serviceName = StringUtils.uncapitalize(ServiceBuilder.getName(entityName));
        TypeName entityType = EntityBuilder.getTypeName(entityName, basePackage);
        String paramName = StringUtils.uncapitalize(entityName);
        MethodSpec methodSpec = MethodSpec.methodBuilder("retrieve")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(entityType, paramName)
                .addParameter(RelationalErrors.class, "errors")
                .returns(entityType)
                .addStatement("$1T retrieved = ($2L.getId() == null) ? null : $3L.getOne($2L.getId()).orElse(null)", entityType, paramName, serviceName)
                .addStatement("if (retrieved == null) errors.register($T.class, $L.getId())", entityType, paramName)
                .addStatement("return retrieved")
                .build();
        builder.addMethod(methodSpec);
    }
}
