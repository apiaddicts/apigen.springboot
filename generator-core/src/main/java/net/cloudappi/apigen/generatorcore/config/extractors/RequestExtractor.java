package net.cloudappi.apigen.generatorcore.config.extractors;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import net.cloudappi.apigen.generatorcore.config.controller.Request;

import java.util.Map;

import static net.cloudappi.apigen.generatorcore.spec.components.Extensions.MAPPING;
import static net.cloudappi.apigen.generatorcore.spec.components.Extensions.MAPPING_MODEL;

public class RequestExtractor {

    private final AttributesExtractor attributesExtractor;

    public RequestExtractor(Map<String, Schema> schemas) {
        this.attributesExtractor = new AttributesExtractor(schemas);
    }

    public Request getRequest(Operation operation) {
        RequestBody requestBody = operation.getRequestBody();
        if (requestBody == null || requestBody.getContent().get("application/json") == null) return null;
        Schema bodySchema = requestBody.getContent().get("application/json").getSchema();
        Request request = new Request();
        request.setAttributes(attributesExtractor.getAttributes(bodySchema));
        request.setRelatedEntity(getMappingEntity(bodySchema));
        return request;
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
