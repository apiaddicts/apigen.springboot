package net.cloudappi.apigen.generatorcore.generator.service;

import com.squareup.javapoet.*;
import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.archetypecore.core.AbstractCrudService;
import net.cloudappi.apigen.archetypecore.core.AbstractRelationsManager;
import net.cloudappi.apigen.archetypecore.core.ApigenMapper;
import net.cloudappi.apigen.generatorcore.generator.common.AbstractClassBuilder;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntityBuilder;
import net.cloudappi.apigen.generatorcore.generator.persistence.repository.RepositoryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;
import java.util.Set;

public class ServiceBuilder extends AbstractClassBuilder {

    private String entityName;
    private String basePackage;
    private TypeName identifierType;
    private TypeName entityType;
    private TypeName repositoryType;
    private Set<String> basicAttributes;

    public ServiceBuilder(String entityName, String basePackage, Set<String> basicAttributes, TypeName identifierType) {
        this.entityName = entityName;
        this.basePackage = basePackage;
        this.entityType = EntityBuilder.getTypeName(entityName, basePackage);
        this.repositoryType = RepositoryBuilder.getTypeName(entityName, basePackage);
        this.identifierType = identifierType;
        this.basicAttributes = basicAttributes;
    }

    public static TypeName getTypeName(String entityName, String basePackage) {
        return ClassName.get(getPackage(entityName, basePackage), getName(entityName));
    }

    public static String getName(String entityName) {
        return entityName + "Service";
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
        addSuperclass();
        addAnnotations();
        addConstructor();
        addUpdateBasicDataPartiallyMethod();
    }

    private void initializeBuilder() {
        builder = getClass(getName(entityName));
    }

    private void addSuperclass() {
        builder.superclass(getParentClass());
    }

    private void addAnnotations() {
        builder.addAnnotation(Slf4j.class)
                .addAnnotation(Service.class);

    }

    private void addConstructor() {
        TypeName relationsManagerType = ParameterizedTypeName.get(ClassName.get(AbstractRelationsManager.class), entityType);
        TypeName mapperType = ParameterizedTypeName.get(ClassName.get(ApigenMapper.class), entityType);
        builder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameter(repositoryType, "repository")
                .addParameter(ParameterSpec.builder(relationsManagerType, "relationsManager").addAnnotation(Nullable.class).build())
                .addParameter(ParameterSpec.builder(mapperType, "mapper").addAnnotation(Nullable.class).build())
                .addStatement("super(repository, relationsManager, mapper)").build());
    }

    private ParameterizedTypeName getParentClass() {
        return ParameterizedTypeName.get(ClassName.get(AbstractCrudService.class),
                entityType,
                identifierType,
                repositoryType
        );
    }

    private void addUpdateBasicDataPartiallyMethod() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("updateBasicDataPartially")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .addParameter(entityType, "persistedEntity")
                .addParameter(entityType, "entity")
                .addParameter(ParameterizedTypeName.get(ClassName.get(Set.class), ClassName.get(String.class)), "fields");

        methodBuilder.beginControlFlow("if (fields == null)");
        methodBuilder.addStatement("mapper.updateBasicData(entity, persistedEntity)");
        methodBuilder.nextControlFlow("else");
        basicAttributes.forEach(attribute -> methodBuilder.addStatement("if (fields.contains($1S)) persistedEntity.set$2L(entity.get$2L())", attribute, StringUtils.capitalize(attribute)));
        methodBuilder.endControlFlow();

        builder.addMethod(methodBuilder.build());
    }
}
