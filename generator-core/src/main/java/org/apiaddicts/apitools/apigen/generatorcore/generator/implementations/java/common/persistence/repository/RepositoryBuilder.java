package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.repository;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntityBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public class RepositoryBuilder<C extends JavaContext> extends AbstractJavaClassBuilder<C> {

    protected final String entityName;
    protected final String basePackage;

    protected final TypeName identifierType;
    protected final TypeName entityType;

    public RepositoryBuilder(Entity entity, C ctx, Configuration cfg) {
        super(ctx, cfg);
        this.entityName = entity.getName();
        this.basePackage = cfg.getBasePackage();
        this.entityType = EntityBuilder.getTypeName(entityName, basePackage);
        this.identifierType = ctx.getEntitiesData().getIDType(entityName);
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

    protected ParameterizedTypeName getInterface() {
        return ParameterizedTypeName.get(
                ClassName.get(JpaRepository.class),
                entityType,
                identifierType
        );
    }
}
