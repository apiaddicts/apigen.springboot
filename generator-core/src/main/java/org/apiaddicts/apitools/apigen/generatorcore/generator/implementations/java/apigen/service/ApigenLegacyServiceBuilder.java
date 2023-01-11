package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.service;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.apiaddicts.apitools.apigen.archetypecore.core.AbstractCrudService;
import org.apiaddicts.apitools.apigen.archetypecore.core.AbstractRelationsManager;
import org.apiaddicts.apitools.apigen.archetypecore.core.ApigenMapper;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.service.ServiceBuilder;
import org.springframework.lang.Nullable;

import javax.lang.model.element.Modifier;
import java.util.Set;

@Deprecated
public class ApigenLegacyServiceBuilder<C extends ApigenContext> extends ServiceBuilder<C> {

    public ApigenLegacyServiceBuilder(Entity entity, C ctx, Configuration cfg) {
        super(entity, ctx, cfg);
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
        super.initialize();
        addUpdateBasicDataPartiallyMethod();
    }

    @Override
    protected void addSuperclass() {
        builder.superclass(getParentClass());
    }

    @Override
    protected void addConstructor() {
        TypeName relationsManagerType = ParameterizedTypeName.get(ClassName.get(AbstractRelationsManager.class), entityType);
        TypeName mapperType = ParameterizedTypeName.get(ClassName.get(ApigenMapper.class), entityType);
        builder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameter(repositoryType, "repository")
                .addParameter(ParameterSpec.builder(relationsManagerType, "relationsManager").addAnnotation(Nullable.class).build())
                .addParameter(ParameterSpec.builder(mapperType, "mapper").addAnnotation(Nullable.class).build())
                .addStatement("super(repository, relationsManager, mapper)").build());
    }

    @Override
    protected ParameterizedTypeName getParentClass() {
        return ParameterizedTypeName.get(ClassName.get(AbstractCrudService.class),
                entityType,
                identifierType,
                repositoryType
        );
    }

    protected void addUpdateBasicDataPartiallyMethod() {
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
