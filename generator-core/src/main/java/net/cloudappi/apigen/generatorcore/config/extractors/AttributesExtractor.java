package net.cloudappi.apigen.generatorcore.config.extractors;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import net.cloudappi.apigen.generatorcore.config.controller.Attribute;
import net.cloudappi.apigen.generatorcore.generator.common.Openapi2JavapoetType;
import net.cloudappi.apigen.generatorcore.utils.CustomStringUtils;

import java.util.*;

import static net.cloudappi.apigen.generatorcore.spec.components.Extensions.*;

public class AttributesExtractor {

    private static final int MAX_DEPTH_LEVEL = 10;

    private Map<String, Schema> schemas;
    private final ValidationsExtractor validationsExtractor;

    public AttributesExtractor(Map<String, Schema> schemas) {
        this.schemas = schemas;
        this.validationsExtractor = new ValidationsExtractor();
    }


    @SuppressWarnings("unchecked")
    public List<Attribute> getFirstLvlAttributes(Schema schema) {
        List<Attribute> attributes = new ArrayList<>();
        for (Map.Entry<String, Schema> property : (Set<Map.Entry<String, Schema>>) schema.getProperties().entrySet()) {
            String propName = property.getKey();
            Schema propSchema = property.getValue();

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

    public List<Attribute> getAttributes(Schema schema) {
        return getAttributes(schema, 0);
    }

    @SuppressWarnings("unchecked")
    private List<Attribute> getAttributes(Schema schema, int level) {

        List<Attribute> attributes = new ArrayList<>();
        if (schema.getProperties() == null || level > MAX_DEPTH_LEVEL) return attributes;
        List<String> required = schema.getRequired();
        if (required == null) required = Collections.emptyList();

        for (Map.Entry<String, Schema> property : (Set<Map.Entry<String, Schema>>) schema.getProperties().entrySet()) {
            String propName = property.getKey();
            Schema propSchema = property.getValue();

            Attribute attribute = new Attribute();
            attribute.setName(propName);

            boolean isCollection = propSchema instanceof ArraySchema;
            attribute.setCollection(isCollection);

            if (isCollection) {
                attribute.setValidations(validationsExtractor.getValidations(propSchema, required.contains(propName)));
                propSchema = ((ArraySchema) propSchema).getItems();
            } else {
                attribute.setValidations(validationsExtractor.getValidations(propSchema, required.contains(propName)));
            }

            propSchema = solveSchema(propSchema);
            String entityFieldName = getEntityFieldName(propSchema, propName);
            attribute.setEntityFieldName(entityFieldName);
            String relatedEntity = getMappingEntity(propSchema);
            attribute.setRelatedEntity(relatedEntity);

            attribute.setType(propSchema.getType());
            attribute.setFormat(propSchema.getFormat());

            attribute.setAttributes(getAttributes(propSchema, level++));
            attributes.add(attribute);
        }
        return attributes;
    }

    private String getMappingEntity(Schema schema) {
        Map<String, Object> apigenExtension = getMappingExtension(schema);
        if (apigenExtension == null) return null;
        return (String) apigenExtension.get(MAPPING_MODEL);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMappingExtension(Schema schema) {
        Map<String, Object> extensions = schema.getExtensions();
        if (extensions == null) return null;
        return (Map<String, Object>) extensions.get(MAPPING);
    }

    private String getEntityFieldName(Schema schema, String defaultName) {
        Map<String, Object> apigenExtension = getMappingExtension(schema);
        defaultName = CustomStringUtils.snakeCaseToCamelCase(defaultName);
        if (apigenExtension == null) return defaultName;
        return (String) apigenExtension.getOrDefault(MAPPING_FIELD, defaultName);
    }

    private Schema solveSchema(Schema schema) {
        if (schema.get$ref() == null) return schema;
        String schemaName = schema.get$ref();
        schemaName = schemaName.substring(schemaName.lastIndexOf('/') + 1);
        return schemas.get(schemaName);
    }
}
