package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.repository;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenRepository;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntityBuilder;
import org.springframework.stereotype.Repository;

public class RepositoryBuilder extends AbstractClassBuilder {

    private String entityName;
    private String basePackage;

    private TypeName identifierType;
    private TypeName entityType;

    public RepositoryBuilder(String entityName, String basePackage, TypeName identifierType) {
        this.entityName = entityName;
        this.basePackage = basePackage;
        this.entityType = EntityBuilder.getTypeName(entityName, basePackage);
        this.identifierType = identifierType;
    }

    public static TypeName getTypeName(String entityName, String basePackage) {
        return ClassName.get(getPackage(entityName, basePackage), getName(entityName));
    }

    public static String getName(String entityName) {
        return entityName + "Repository";
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
        addSuperinterface();
        addAnnotations();
    }

    private void initializeBuilder() {
        builder = getInterface(getName(entityName));
    }

    private void addSuperinterface() {
        builder.addSuperinterface(getInterface());
    }

    private void addAnnotations() {
        builder.addAnnotation(Repository.class);
    }

    private ParameterizedTypeName getInterface() {
        return ParameterizedTypeName.get(
                ClassName.get(ApigenRepository.class),
                entityType,
                identifierType
        );
    }
}
