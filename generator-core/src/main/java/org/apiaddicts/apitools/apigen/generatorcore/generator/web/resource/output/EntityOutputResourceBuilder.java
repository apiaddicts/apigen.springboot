package org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.*;
import lombok.Data;
import org.apiaddicts.apitools.apigen.archetypecore.core.resource.ApigenEntityOutResource;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Attribute;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.ApigenExt2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.Openapi2JavapoetType;

import java.util.List;
import java.util.Set;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

@Deprecated
public class EntityOutputResourceBuilder extends OutputResourceBuilder {

    private String entityName;
    private String basePackage;
    private List<Attribute> attributes;

    public EntityOutputResourceBuilder(Response response, String basePackage) {
        this.basePackage = basePackage;
        this.entityName = response.getRelatedEntity();
        this.attributes = response.getAttributes();
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

    private void initializeBuilder() {
        builder = getClass(getName(entityName))
                .addAnnotation(Data.class)
                .addAnnotation(ApigenEntityOutResource.class);
    }

    private void addAttributes() {
        for (Attribute attribute : attributes) {
            if (attribute.getRelatedEntity() != null) {
                addRelatedAttribute(attribute, builder, basePackage);
            } else {
                addSimpleAttribute(attribute, builder);
            }
        }
    }

    private void addRelatedAttribute(Attribute attribute, TypeSpec.Builder builder, String basePackage) {
        String relatedEntity = attribute.getRelatedEntity();
        TypeName type = getTypeName(relatedEntity, basePackage);
        if (attribute.isCollection()) {
            type = ParameterizedTypeName.get(ClassName.get(Set.class), type);
        }
        addAttribute(type, attribute.getEntityFieldName(), attribute.getName(), builder);
    }

    private void addSimpleAttribute(Attribute attribute, TypeSpec.Builder builder) {
        TypeName type;
        if (attribute.getImplementationType() != null) {
            type = ApigenExt2JavapoetType.transformType(attribute.getImplementationType());
        } else {
            type = Openapi2JavapoetType.transformSimpleType(attribute.getType(), attribute.getFormat());
        }
        addAttribute(type, attribute.getEntityFieldName(), attribute.getName(), builder);
    }

    private void addAttribute(TypeName type, String name, String jsonName, TypeSpec.Builder builder) {
        FieldSpec.Builder fieldBuilder = getField(type, name);
        addJsonName(jsonName, fieldBuilder);
        builder.addField(fieldBuilder.build());
    }

    private void addJsonName(String name, FieldSpec.Builder builder) {
        builder.addAnnotation(getAnnotation(JsonProperty.class).addMember(VALUE, STRING, name).build());
    }
}
