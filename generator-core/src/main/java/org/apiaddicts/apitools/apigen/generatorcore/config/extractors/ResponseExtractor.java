package org.apiaddicts.apitools.apigen.generatorcore.config.extractors;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Constants.JSON_MIME_TYPE;
import static org.apiaddicts.apitools.apigen.generatorcore.spec.components.Extensions.*;

public class ResponseExtractor {

    private final AttributesExtractor attributesExtractor;

    public ResponseExtractor(Map<String, Schema<?>> schemas) {
        this.attributesExtractor = new AttributesExtractor(schemas);
    }

    public Response getResponse(Operation operation) {
        int status = operation.getResponses().keySet().stream()
                .filter(s -> !"default".equals(s)).mapToInt(Integer::parseInt)
                .filter(n -> n >= 200 && n <= 400)
                .sorted()
                .findFirst().orElse(0);

        if (status == 204) {
            return null;
        }

        ApiResponse response = operation.getResponses().get(status == 0 ? "default" : String.valueOf(status));

        Response endpointResponse = new Response();
        endpointResponse.setDefaultStatusCode(status == 0 ? 200 : status);
        endpointResponse.setIsStandard(false);

        if (response == null || response.getContent() == null || response.getContent().get(JSON_MIME_TYPE) == null) {
            if(response != null && response.getContent() != null) {
                endpointResponse.setMimeType(response.getContent().keySet().iterator().next());
                endpointResponse.setAttributes(new LinkedList<>());
            }
            return endpointResponse;
        }

        Schema<?> schema = response.getContent().get(JSON_MIME_TYPE).getSchema();
        endpointResponse.setMimeType(response.getContent().keySet().iterator().next());

        if (isStandardResponse(schema)) {
            endpointResponse.setIsStandard(true);
            Schema<?> dataSchema = getDataSchema(schema);
            boolean isCollection = isNamedCollectionSchema(dataSchema);
            endpointResponse.setIsCollection(isCollection);
            if (isCollection) {
                String collectionName = getNamedCollectionName(dataSchema);
                endpointResponse.setCollectionName(collectionName);
                dataSchema = ((ArraySchema) dataSchema.getProperties().get(collectionName)).getItems();
            }
            String relatedEntity = getMappingEntity(dataSchema);
            if (relatedEntity == null) {
                endpointResponse.setAttributes(attributesExtractor.getAttributes(dataSchema));
            } else {
                endpointResponse.setRelatedEntity(relatedEntity);
                endpointResponse.setAttributes(attributesExtractor.getFirstLvlAttributes(dataSchema));
            }
        } else {
            if (schema instanceof ArraySchema) {
                endpointResponse.setIsCollection(true);
                schema = schema.getItems();
            } else {
                endpointResponse.setIsCollection(false);
            }
            endpointResponse.setAttributes(attributesExtractor.getAttributes(schema));
        }
        return endpointResponse;
    }

    private boolean isStandardResponse(Schema<?> schema) {
        if (schema.getExtensions() != null && schema.getExtensions().containsKey(RESPONSE)) {
            Map<String, Object> apigenResponse = (Map<String, Object>) schema.getExtensions().get(RESPONSE);
            if (apigenResponse != null && apigenResponse.containsKey(RESPONSE_STANDARD)
                    && apigenResponse.get(RESPONSE_STANDARD).equals(false)
                    && !schema.getProperties().containsKey(getStandardDataProperty(apigenResponse))) {
                return false;
            }
        } else if (schema.getProperties() == null || !schema.getProperties().containsKey("data")
                || !schema.getProperties().containsKey("result")) {
            return false;
        }
        return true;
    }

    private Schema<?> getDataSchema(Schema<?> schema) {
        String dataProperty = "data";
        if (schema.getExtensions() != null && schema.getExtensions().containsKey(RESPONSE)) {
            Map<String, Object> apigenResponse = (Map<String, Object>) schema.getExtensions().get(RESPONSE);
            dataProperty = getStandardDataProperty(apigenResponse);
        }
        return schema.getProperties().get(dataProperty);
    }

    private String getStandardDataProperty(Map<String, Object> schema) {
        String dataProperty = "data";
        if (schema != null && schema.containsKey(RESPONSE_STANDARD_DATA_PROPERTY)) {
            dataProperty = (String) schema.get(RESPONSE_STANDARD_DATA_PROPERTY);
        }
        return dataProperty;
    }

    private boolean isNamedCollectionSchema(Schema<?> dataSchema) {
        if (dataSchema.getProperties() == null || dataSchema.getProperties().size() != 1) return false;
        String key = getNamedCollectionName(dataSchema);
        return dataSchema.getProperties().get(key) instanceof ArraySchema;
    }

    private String getNamedCollectionName(Schema<?> dataSchema) {
        return dataSchema.getProperties().keySet().iterator().next();
    }

    private String getMappingEntity(Schema<?> schema) {
        // TODO: Remove this temporal fix when polymorphism is supported
        if (schema.getOneOf() != null && !schema.getOneOf().isEmpty()) {
            schema = schema.getOneOf().get(0);
        }
        Map<String, Object> apigenExtension = getMappingExtension(schema);
        return (String) apigenExtension.get(MAPPING_MODEL);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMappingExtension(Schema<?> schema) {
        Map<String, Object> extensions = schema.getExtensions();
        if (extensions == null) return Collections.emptyMap();
        return (Map<String, Object>) extensions.get(MAPPING);
    }
}
