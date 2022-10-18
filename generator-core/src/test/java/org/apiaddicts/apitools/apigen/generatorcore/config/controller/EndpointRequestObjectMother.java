package org.apiaddicts.apitools.apigen.generatorcore.config.controller;

import java.util.ArrayList;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Constants.JSON_MIME_TYPE;

public class EndpointRequestObjectMother {
    public static Request requestWithoutAttributes(String entityName) {
        Request request = new Request();
        request.setRelatedEntity(entityName);
        request.setAttributes(new ArrayList<>());
        request.setMimeType(JSON_MIME_TYPE);
        return request;
    }

    public static Request customRequestWithoutAttributes() {
        Request request = new Request();
        request.setAttributes(new ArrayList<>());
        request.setMimeType(JSON_MIME_TYPE);
        return request;
    }
}
