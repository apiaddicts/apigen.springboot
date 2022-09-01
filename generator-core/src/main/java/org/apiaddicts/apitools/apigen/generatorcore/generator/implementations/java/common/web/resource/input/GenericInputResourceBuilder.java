package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Attribute;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Request;
import org.apiaddicts.apitools.apigen.generatorcore.config.validation.Validation;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.ApigenExt2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.Openapi2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.LITERAL;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.MODE;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

public class GenericInputResourceBuilder<C extends JavaContext> extends InputResourceBuilder<C> {

    protected final String entityName;
    protected final String basePackage;
    protected final Mapping rootMapping;
    protected final List<Attribute> attributes;
    protected final Endpoint endpoint;

    public GenericInputResourceBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(ctx, cfg);
        Request endpointRequestBody = endpoint.getRequest();
        this.basePackage = cfg.getBasePackage();
        this.entityName = endpointRequestBody.getRelatedEntity();
        this.rootMapping = rootMapping;
        this.attributes = endpointRequestBody.getAttributes();
        this.endpoint = endpoint;
    }

    // FIXME analyze separation
    public static TypeName getTypeName(Endpoint endpoint, String basePackage) {
        String rootEntityName = endpoint.getRelatedEntity();
        return ClassName.get(getPackage(rootEntityName, basePackage), getName(new Mapping(null), endpoint));
    }

    // FIXME analyze separation
    public static TypeName getTypeName(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        String entityName = endpoint.getRequest().getRelatedEntity();
        if (entityName == null) entityName = endpoint.getRelatedEntity();
        if (entityName == null) entityName = rootMapping.toName().toLowerCase();
        return ClassName.get(getPackage(entityName, basePackage), getName(rootMapping, endpoint));
    }

    private static String getName(Mapping rootMapping, Endpoint endpoint) {
        String rootEntityName = endpoint.getRelatedEntity();
        String entityName = endpoint.getRequest().getRelatedEntity();
        if (entityName == null) entityName = rootEntityName;
        if (entityName == null) entityName = rootMapping.toName();
        Endpoint.Method method = endpoint.getMethod();
        Mapping mapping = new Mapping(endpoint.getMapping());
        String mappingName = mapping.toName();
        return method.prefix + entityName + mappingName + "Resource";
    }

    private static String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName, "web");
    }

    @Override
    public TypeName getTypeName() {
        return getTypeName(rootMapping, endpoint, basePackage);
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public String getPackage() {
        if (entityName == null) { // FIXME analyze separation
            return getPackage(rootMapping.toName().toLowerCase(), basePackage);
        } else {
            return getPackage(entityName, basePackage);
        }
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addAttributes();
    }

    protected void initializeBuilder() {
        builder = getClass(getName(rootMapping, endpoint))
                .addAnnotation(Data.class);
    }

    protected void addAttributes() {
        addAttributes(attributes, builder, getTypeName(rootMapping, endpoint, basePackage));
    }

    protected void addAttributes(List<Attribute> attributes, Builder builder, TypeName parentType) {
        for (Attribute attribute : attributes) {
            boolean requiresNestedObject = requiredNestedObject(attribute);
            String javaName = attribute.getEntityFieldName();
            TypeName type;
            boolean nested = false;

            if (requiresNestedObject) {
                type = createNestedObject(javaName, attribute.getAttributes(), parentType, builder);
                nested = true;
            } else {
                if (attribute.getImplementationType() != null) {
                    type = ApigenExt2JavapoetType.transformType(attribute.getImplementationType());
                } else {
                    type = Openapi2JavapoetType.transformSimpleType(attribute.getType(), attribute.getFormat());
                }
                boolean isUnwrappedIdentifier = isUnwrappedIdentifier(attribute);
                if (isUnwrappedIdentifier) {
                    type = createIdentifierNestedObject(javaName, type, parentType);
                    javaName = javaName.split("\\.")[0];
                    nested = true;
                }
            }
            if (attribute.isCollection()) {
                type = ParameterizedTypeName.get(ClassName.get(Set.class), type);
            }
            addAttribute(type, javaName, attribute.getName(), attribute.getValidations(), nested, builder);
        }
    }

    protected boolean requiredNestedObject(Attribute attribute) {
        return Openapi2JavapoetType.TYPE_OBJECT.equals(attribute.getType()) || Openapi2JavapoetType.TYPE_ARRAY.equals(attribute.getType()) || null == attribute.getType();
    }

    protected TypeName createNestedObject(String javaName, List<Attribute> attributes, TypeName parentType, Builder builder) {
        String nestedName = StringUtils.capitalize(javaName);
        TypeName type = ((ClassName) parentType).nestedClass(nestedName);
        Builder nestedBuilder = getPublicInnerClass(nestedName).addAnnotation(Data.class);
        addAttributes(attributes, nestedBuilder, type);
        builder.addType(nestedBuilder.build());
        return type;
    }

    protected boolean isUnwrappedIdentifier(Attribute attribute) {
        return nonNull(attribute.getEntityFieldName()) && nonNull(attribute.getRelatedEntity())
                && attribute.getAttributes().isEmpty() && StringUtils.countMatches(attribute.getEntityFieldName(), '.') == 1;
    }

    protected TypeName createIdentifierNestedObject(String javaName, TypeName type, TypeName parentType) {
        String[] nameParts = javaName.split("\\.");
        javaName = nameParts[0];
        String nestedName = StringUtils.capitalize(javaName);
        String nestedAttributeName = nameParts[1];
        TypeName nestedAttributeType = type;
        type = ((ClassName) parentType).nestedClass(nestedName);
        Builder nestedAttributeBuilder = getPublicInnerClass(nestedName).addAnnotation(Data.class).addAnnotation(NoArgsConstructor.class);
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addAnnotation(AnnotationSpec.builder(JsonCreator.class).addMember(MODE, LITERAL, "JsonCreator.Mode.DELEGATING").build())
                .addParameter(nestedAttributeType, nestedAttributeName)
                .addStatement("this.$1L = $1L", nestedAttributeName)
                .build();
        nestedAttributeBuilder.addMethod(constructor);
        addAttribute(nestedAttributeType, nestedAttributeName, nestedAttributeName, Collections.emptyList(), false, nestedAttributeBuilder);
        builder.addType(nestedAttributeBuilder.build());
        return nestedAttributeType;
    }


    protected void addAttribute(TypeName type, String name, String jsonName, List<Validation> validations, boolean nested, Builder builder) {
        FieldSpec.Builder fieldBuilder = getField(type, name);
        addJsonName(jsonName, fieldBuilder);
        validations.forEach(validation -> validation.apply(fieldBuilder));
        if (nested) fieldBuilder.addAnnotation(Valid.class);
        builder.addField(fieldBuilder.build());
    }

    protected void addJsonName(String name, FieldSpec.Builder builder) {
        builder.addAnnotation(getAnnotation(JsonProperty.class).addMember(VALUE, STRING, name).build());
    }
}

