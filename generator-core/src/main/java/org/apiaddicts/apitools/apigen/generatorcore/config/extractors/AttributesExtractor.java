package org.apiaddicts.apitools.apigen.generatorcore.config.extractors;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Attribute;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.Openapi2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.utils.CustomStringUtils;

import java.util.*;

import static org.apiaddicts.apitools.apigen.generatorcore.spec.components.Extensions.*;

public class AttributesExtractor {

    private static final int MAX_DEPTH_LEVEL = 10;

    private final Map<String, Schema<?>> schemas;
    private final ValidationsExtractor validationsExtractor;

    public AttributesExtractor(Map<String, Schema<?>> schemas) {
        this.schemas = schemas;
        this.validationsExtractor = new ValidationsExtractor();
    }


    @SuppressWarnings("unchecked")
    public List<Attribute> getFirstLvlAttributes(Schema<?> schema) {
        List<Attribute> attributes = new ArrayList<>();

        final Map<String, Schema> properties = Optional.ofNullable(schema.getProperties()).orElseGet(HashMap::new);

        // TODO: Remove this temporal fix when polymorphism is supported
        if (schema.getOneOf() != null && !schema.getOneOf().isEmpty()) {
            properties.clear();
            schema.getOneOf().forEach(it -> {
                if (it.getProperties() != null) properties.putAll(it.getProperties());
            });
        }

        for (Map.Entry<String, Schema> property : properties.entrySet()) {
            String propName = property.getKey();
            Schema<?> propSchema = property.getValue();

            Attribute attribute = new Attribute();
            attribute.setName(propName);

            boolean isCollection = propSchema instanceof ArraySchema;
            attribute.setCollection(isCollection);

            if (isCollection) {
                propSchema = ((ArraySchema) propSchema).getItems();
            }

            propSchema = solveSchema(propSchema);
            String entityFieldName = getEntityFieldName(propSchema, propName);
            attribute.setEntityFieldName(entityFieldName);

            if (!isCollection) {
                attribute.setType(propSchema.getType());
                attribute.setFormat(propSchema.getFormat());
                attribute.setImplementationType(getImplementationType(propSchema));
            }

            boolean isObject = Openapi2JavapoetType.TYPE_OBJECT.equalsIgnoreCase(attribute.getType());
            if (isObject || isCollection) {
                String relatedEntity = getMappingEntity(propSchema);
                attribute.setRelatedEntity(relatedEntity);
            }

            attributes.add(attribute);
        }
        return attributes;
    }

    public List<Attribute> getAttributes(Schema<?> schema) {
        return getAttributes(schema, 0);
    }

    @SuppressWarnings("unchecked")
    private List<Attribute> getAttributes(Schema<?> schema, int level) {

        List<Attribute> attributes = new ArrayList<>();
        final Map<String, Schema> properties = Optional.ofNullable(schema.getProperties()).orElseGet(HashMap::new);
        List<String> required = schema.getRequired();

        // TODO: Remove this temporal fix when polymorphism is supported
        if (schema.getOneOf() != null && !schema.getOneOf().isEmpty()) {
            properties.clear();
            schema.getOneOf().forEach(it -> {
                if (it.getProperties() != null) properties.putAll(it.getProperties());
            });
        }

        if (properties.isEmpty() || level > MAX_DEPTH_LEVEL) return attributes;
        if (required == null) required = Collections.emptyList();

        for (Map.Entry<String, Schema> property : properties.entrySet()) {
            String propName = property.getKey();
            Schema<?> propSchema = property.getValue();

            Attribute attribute = new Attribute();
            attribute.setName(propName);

            boolean isCollection = propSchema instanceof ArraySchema;
            attribute.setCollection(isCollection);

            if (isCollection) {
                attribute.setValidations(validationsExtractor.getValidations(propSchema, required.contains(propName)));
                propSchema = propSchema.getItems();
            } else {
                attribute.setValidations(validationsExtractor.getValidations(propSchema, required.contains(propName)));
            }

            propSchema = solveSchema(propSchema);
            String entityFieldName = getEntityFieldName(propSchema, propName);
            attribute.setEntityFieldName(entityFieldName);
            String relatedEntity = getMappingEntity(propSchema);
            attribute.setRelatedEntity(relatedEntity);

            attribute.setType(propSchema.getType());
            if (attribute.getType() == null) attribute.setType("object");
            attribute.setFormat(propSchema.getFormat());
            attribute.setImplementationType(getImplementationType(propSchema));

            attribute.setAttributes(getAttributes(propSchema, level++));
            attributes.add(attribute);
        }
        return attributes;
    }

    private String getImplementationType(Schema<?> schema) {
        Map<String, Object> extensions = schema.getExtensions();
        if (extensions == null) return null;
        return (String) extensions.get(TYPE);
    }

    private String getMappingEntity(Schema<?> schema) {
        Map<String, Object> apigenExtension = getMappingExtension(schema);
        if (apigenExtension == null) return null;
        return (String) apigenExtension.get(MAPPING_MODEL);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMappingExtension(Schema<?> schema) {
        Map<String, Object> extensions = schema.getExtensions();
        if (extensions == null) return null;
        return (Map<String, Object>) extensions.get(MAPPING);
    }

    private String getEntityFieldName(Schema<?> schema, String defaultName) {
        Map<String, Object> apigenExtension = getMappingExtension(schema);
        defaultName = CustomStringUtils.snakeCaseToCamelCase(defaultName);
        if (apigenExtension == null) return defaultName;
        return (String) apigenExtension.getOrDefault(MAPPING_FIELD, defaultName);
    }

    private Schema<?> solveSchema(Schema<?> schema) {
        if (schema.get$ref() == null) return schema;
        String schemaName = schema.get$ref();
        schemaName = schemaName.substring(schemaName.lastIndexOf('/') + 1);
        return schemas.get(schemaName);
    }
}
