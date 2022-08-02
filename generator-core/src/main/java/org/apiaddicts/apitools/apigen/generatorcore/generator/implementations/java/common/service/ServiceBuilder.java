package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.service;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntityBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.repository.RepositoryBuilder;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;
import java.util.Set;

public class ServiceBuilder<C extends JavaContext> extends AbstractJavaClassBuilder<C> {

    protected final String entityName;
    protected final String basePackage;
    protected final TypeName identifierType;
    protected final TypeName entityType;
    protected final TypeName repositoryType;
    protected final Set<String> basicAttributes;

    public ServiceBuilder(Entity entity, C ctx, Configuration cfg) {
        super(ctx, cfg);
        this.entityName = entity.getName();
        this.basePackage = cfg.getBasePackage();
        this.entityType = EntityBuilder.getTypeName(entityName, basePackage);
        this.repositoryType = RepositoryBuilder.getTypeName(entityName, basePackage);
        this.identifierType = ctx.getEntitiesData().getIDType(entityName);
        this.basicAttributes = ctx.getEntitiesData().getBasicAttributes(entityName);
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
    }

    protected void initializeBuilder() {
        builder = getClass(getName(entityName));
    }

    protected void addSuperclass() {
        // Intentional blank
    }

    protected void addAnnotations() {
        builder.addAnnotation(Slf4j.class)
                .addAnnotation(Service.class);
    }

    protected void addConstructor() {
        builder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameter(repositoryType, "repository").build());
    }

    protected ParameterizedTypeName getParentClass() {
        return null;
    }
}
