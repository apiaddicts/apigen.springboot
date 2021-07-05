package org.apiaddicts.apitools.apigen.generatorcore.config.controller;

import java.util.ArrayList;

public class EndpointRequestObjectMother {
    public static Request requestWithoutAttributes(String entityName) {
        Request request = new Request();
        request.setRelatedEntity(entityName);
        request.setAttributes(new ArrayList<>());
        return request;
    }

    public static Request customRequestWithoutAttributes() {
        Request request = new Request();
        request.setAttributes(new ArrayList<>());
        return request;
    }
}
