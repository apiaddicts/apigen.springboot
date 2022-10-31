package org.apiaddicts.apitools.apigen.generatorcore.config.extractors;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Request;

import java.util.LinkedList;
import java.util.Map;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Constants.JSON_MIME_TYPE;
import static org.apiaddicts.apitools.apigen.generatorcore.spec.components.Extensions.MAPPING;
import static org.apiaddicts.apitools.apigen.generatorcore.spec.components.Extensions.MAPPING_MODEL;

public class RequestExtractor {

    private final AttributesExtractor attributesExtractor;

    public RequestExtractor(Map<String, Schema<?>> schemas) {
        this.attributesExtractor = new AttributesExtractor(schemas);
    }

    public Request getRequest(Operation operation) {
        RequestBody requestBody = operation.getRequestBody();
        if (requestBody == null) return null;
        Request request = new Request();
        request.setMimeType(requestBody.getContent().keySet().iterator().next());
        if (requestBody.getContent().get(JSON_MIME_TYPE) == null) {
            request.setAttributes(new LinkedList<>());
            return request;
        }
        Schema<?> bodySchema = requestBody.getContent().get(JSON_MIME_TYPE).getSchema();
        request.setAttributes(attributesExtractor.getAttributes(bodySchema));
        request.setRelatedEntity(getMappingEntity(bodySchema));
        return request;
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
}
