package org.apiaddicts.apitools.apigen.generatorcore.config.extractors;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Response;

import java.util.Map;

import static org.apiaddicts.apitools.apigen.generatorcore.spec.components.Extensions.MAPPING;
import static org.apiaddicts.apitools.apigen.generatorcore.spec.components.Extensions.MAPPING_MODEL;

public class ResponseExtractor {

    private final AttributesExtractor attributesExtractor;

    public ResponseExtractor(Map<String, Schema> schemas) {
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

        if (response == null || response.getContent() == null || response.getContent().get("application/json") == null) {
            return endpointResponse;
        }

        Schema schema = response.getContent().get("application/json").getSchema();

        if (isStandardResponse(schema)) {
            endpointResponse.setIsStandard(true);
            Schema dataSchema = getDataSchema(schema);
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
                schema = ((ArraySchema) schema).getItems();
            } else {
                endpointResponse.setIsCollection(false);
            }
            endpointResponse.setAttributes(attributesExtractor.getAttributes(schema));
        }
        return endpointResponse;
    }

    private boolean isStandardResponse(Schema schema) {
        return schema.getProperties() != null && schema.getProperties().containsKey("data") && schema.getProperties().containsKey("result");
    }

    private Schema getDataSchema(Schema schema) {
        return (Schema) schema.getProperties().get("data");
    }

    private boolean isNamedCollectionSchema(Schema dataSchema) {
        if (dataSchema.getProperties().size() != 1) return false;
        String key = getNamedCollectionName(dataSchema);
        return dataSchema.getProperties().get(key) instanceof ArraySchema;
    }

    private String getNamedCollectionName(Schema dataSchema) {
        return (String) dataSchema.getProperties().keySet().iterator().next();
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
}
