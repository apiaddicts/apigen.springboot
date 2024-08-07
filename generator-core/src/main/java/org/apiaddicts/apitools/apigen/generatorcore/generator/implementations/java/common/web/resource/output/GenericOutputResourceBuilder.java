package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Attribute;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.ApigenExt2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.Openapi2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

public class GenericOutputResourceBuilder<C extends JavaContext> extends OutputResourceBuilder<C> {

    protected final String entityName;
    protected final String basePackage;
    protected final Mapping rootMapping;
    protected final List<Attribute> attributes;
    protected final Endpoint endpoint;

    public GenericOutputResourceBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(ctx, cfg);
        Response endpointResponseBody = endpoint.getResponse();
        this.basePackage = cfg.getBasePackage();
        this.entityName = endpointResponseBody.getRelatedEntity() == null ? endpoint.getRelatedEntity() : endpointResponseBody.getRelatedEntity();
        this.rootMapping = rootMapping;
        this.attributes = endpointResponseBody.getAttributes();
        this.endpoint = endpoint;
    }

    public static TypeName getTypeName(Endpoint endpoint, String basePackage) {
        String rootEntityName = endpoint.getRelatedEntity();
        return ClassName.get(getPackage(rootEntityName, basePackage), getName(null, endpoint));
    }

    public static TypeName getTypeName(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        String rootEntityName = endpoint.getRelatedEntity();
        if (rootEntityName == null) rootEntityName = rootMapping.toName().toLowerCase();
        return ClassName.get(getPackage(rootEntityName, basePackage), getName(rootMapping, endpoint));
    }

    private static String getName(Mapping rootMapping, Endpoint endpoint) {
        String rootEntityName = endpoint.getRelatedEntity();
        String entityName = endpoint.getResponse().getRelatedEntity();
        if (entityName == null) entityName = rootEntityName;
        if (entityName == null) entityName = rootMapping == null ? "" : rootMapping.toName();
        Endpoint.Method method = endpoint.getMethod();
        Mapping mapping = new Mapping(endpoint.getMapping());
        String mappingName = mapping.toName();
        return method.prefix + entityName + mappingName + "OutResource";
    }

    private static String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName, "web");
    }

    public TypeName getTypeName() {
        return getTypeName(rootMapping, endpoint, basePackage);
    }

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

    protected void addAttributes(List<Attribute> attributes, TypeSpec.Builder builder, TypeName parentType) {

        for (Attribute attribute : attributes) {
            boolean nested = requiredNestedObject(attribute);
            String javaName = attribute.getEntityFieldName();
            TypeName type;

            if (nested) {
                type = createNestedObject(javaName, attribute.getAttributes(), parentType);
            } else {
                if (attribute.getImplementationType() != null) {
                    type = ApigenExt2JavapoetType.transformType(attribute.getImplementationType());
                } else {
                    type = Openapi2JavapoetType.transformSimpleType(attribute.getType(), attribute.getFormat());
                }
            }
            if (attribute.isCollection()) {
                type = ParameterizedTypeName.get(ClassName.get(Set.class), type);
            }
            addAttribute(type, javaName, attribute.getName(), nested, builder);
        }
    }

    protected boolean requiredNestedObject(Attribute attribute) {
        return Openapi2JavapoetType.TYPE_OBJECT.equals(attribute.getType()) || Openapi2JavapoetType.TYPE_ARRAY.equals(attribute.getType());
    }

    protected TypeName createNestedObject(String javaName, List<Attribute> attributes, TypeName parentType) {
        String nestedName = StringUtils.capitalize(javaName);
        TypeName type = ((ClassName) parentType).nestedClass(nestedName);
        TypeSpec.Builder nestedBuilder = getPublicInnerClass(nestedName).addAnnotation(Data.class);
        addAttributes(attributes, nestedBuilder, type);
        builder.addType(nestedBuilder.build());
        return type;
    }

    protected void addAttribute(TypeName type, String name, String jsonName, boolean nested, TypeSpec.Builder builder) {
        FieldSpec.Builder fieldBuilder = getField(type, name);
        addJsonName(jsonName, fieldBuilder);
        if (nested) fieldBuilder.addAnnotation(Valid.class);
        builder.addField(fieldBuilder.build());
    }

    protected void addJsonName(String name, FieldSpec.Builder builder) {
        builder.addAnnotation(getAnnotation(JsonProperty.class).addMember(VALUE, STRING, name).build());
    }
}
