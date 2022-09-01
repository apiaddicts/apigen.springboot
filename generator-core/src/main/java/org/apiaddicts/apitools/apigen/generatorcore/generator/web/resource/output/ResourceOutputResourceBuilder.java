package org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.*;
import lombok.Data;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.*;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.ApigenExt2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.Openapi2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

@Deprecated
public class ResourceOutputResourceBuilder extends OutputResourceBuilder {

    private String entityName;
    private String basePackage;
    private Mapping rootMapping;
    private List<Attribute> attributes;
    private Endpoint endpoint;

    public ResourceOutputResourceBuilder(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        Response endpointResponseBody = endpoint.getResponse();
        this.basePackage = basePackage;
        this.entityName = endpointResponseBody.getRelatedEntity();
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

    private void initializeBuilder() {
        builder = getClass(getName(rootMapping, endpoint))
                .addAnnotation(Data.class);
    }

    private void addAttributes() {
        addAttributes(attributes, builder, getTypeName(rootMapping, endpoint, basePackage));
    }

    private void addAttributes(List<Attribute> attributes, TypeSpec.Builder builder, TypeName parentType) {

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

    private boolean requiredNestedObject(Attribute attribute) {
        return Openapi2JavapoetType.TYPE_OBJECT.equals(attribute.getType()) || Openapi2JavapoetType.TYPE_ARRAY.equals(attribute.getType());
    }

    private TypeName createNestedObject(String javaName, List<Attribute> attributes, TypeName parentType) {
        String nestedName = StringUtils.capitalize(javaName);
        TypeName type = ((ClassName) parentType).nestedClass(nestedName);
        TypeSpec.Builder nestedBuilder = getPublicInnerClass(nestedName).addAnnotation(Data.class);
        addAttributes(attributes, nestedBuilder, type);
        builder.addType(nestedBuilder.build());
        return type;
    }

    private void addAttribute(TypeName type, String name, String jsonName, boolean nested, TypeSpec.Builder builder) {
        FieldSpec.Builder fieldBuilder = getField(type, name);
        addJsonName(jsonName, fieldBuilder);
        if (nested) fieldBuilder.addAnnotation(Valid.class);
        builder.addField(fieldBuilder.build());
    }

    private void addJsonName(String name, FieldSpec.Builder builder) {
        builder.addAnnotation(getAnnotation(JsonProperty.class).addMember(VALUE, STRING, name).build());
    }
}
