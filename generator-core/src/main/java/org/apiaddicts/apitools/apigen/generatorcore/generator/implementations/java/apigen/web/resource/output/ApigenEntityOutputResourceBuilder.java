package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.resource.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.*;
import lombok.Data;
import org.apiaddicts.apitools.apigen.archetypecore.core.resource.ApigenEntityOutResource;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Attribute;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.ApigenExt2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.Openapi2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output.OutputResourceBuilder;

import java.util.List;
import java.util.Set;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

public class ApigenEntityOutputResourceBuilder<C extends ApigenContext> extends OutputResourceBuilder<C> {

    protected final String entityName;
    protected final String basePackage;
    protected final List<Attribute> attributes;

    public ApigenEntityOutputResourceBuilder(Endpoint endpoint, C ctx, Configuration cfg) {
        super(ctx, cfg);
        this.basePackage = cfg.getBasePackage();
        this.entityName = endpoint.getResponse().getRelatedEntity();
        this.attributes = endpoint.getResponse().getAttributes();
    }

    public static TypeName getTypeName(String entityName, String basePackage) {
        return ClassName.get(getPackage(entityName, basePackage), getName(entityName));
    }

    private static String getName(String entityName) {
        return entityName + "OutResource";
    }

    private static String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName, "web");
    }

    @Override
    public TypeName getTypeName() {
        return getTypeName(entityName, basePackage);
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public String getPackage() {
        return getPackage(entityName, basePackage);
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addAttributes();
    }

    protected void initializeBuilder() {
        builder = getClass(getName(entityName))
                .addAnnotation(Data.class)
                .addAnnotation(ApigenEntityOutResource.class);
    }

    protected void addAttributes() {
        for (Attribute attribute : attributes) {
            if (attribute.getRelatedEntity() != null) {
                addRelatedAttribute(attribute, builder, basePackage);
            } else {
                addSimpleAttribute(attribute, builder);
            }
        }
    }

    protected void addRelatedAttribute(Attribute attribute, TypeSpec.Builder builder, String basePackage) {
        String relatedEntity = attribute.getRelatedEntity();
        TypeName type = getTypeName(relatedEntity, basePackage);
        if (attribute.isCollection()) {
            type = ParameterizedTypeName.get(ClassName.get(Set.class), type);
        }
        addAttribute(type, attribute.getEntityFieldName(), attribute.getName(), builder);
    }

    protected void addSimpleAttribute(Attribute attribute, TypeSpec.Builder builder) {
        TypeName type;
        if (attribute.getImplementationType() != null) {
            type = ApigenExt2JavapoetType.transformType(attribute.getImplementationType());
        } else {
            type = Openapi2JavapoetType.transformSimpleType(attribute.getType(), attribute.getFormat());
        }
        addAttribute(type, attribute.getEntityFieldName(), attribute.getName(), builder);
    }

    protected void addAttribute(TypeName type, String name, String jsonName, TypeSpec.Builder builder) {
        FieldSpec.Builder fieldBuilder = getField(type, name);
        addJsonName(jsonName, fieldBuilder);
        builder.addField(fieldBuilder.build());
    }

    protected void addJsonName(String name, FieldSpec.Builder builder) {
        builder.addAnnotation(getAnnotation(JsonProperty.class).addMember(VALUE, STRING, name).build());
    }
}
