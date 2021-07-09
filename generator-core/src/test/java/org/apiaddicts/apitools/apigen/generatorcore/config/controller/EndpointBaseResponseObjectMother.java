package org.apiaddicts.apitools.apigen.generatorcore.config.controller;

import java.util.ArrayList;

public class EndpointBaseResponseObjectMother {

    private EndpointBaseResponseObjectMother() {
        // Intentional blank
    }

    public static Response simpleResponseWithoutAttributes(String entityName) {
        Response response = new Response();
        response.setIsStandard(true);
        response.setRelatedEntity(entityName);
        response.setAttributes(new ArrayList<>());
        response.setIsCollection(false);
        return response;
    }

    public static Response listResponseWithoutAttributesAndCollectionNameEqualsEntityName(String entityName) {
        return listResponseWithoutAttributes(entityName, entityName.toLowerCase());
    }

    public static Response listResponseWithoutAttributes(String entityName, String collectionName) {
        Response response = new Response();
        response.setIsStandard(true);
        response.setRelatedEntity(entityName);
        response.setAttributes(new ArrayList<>());
        response.setIsCollection(true);
        response.setCollectionName(collectionName);
        return response;
    }

    public static Response simpleResponseWithSimpleAttribute(String entityName) {
        Response response = simpleResponseWithoutAttributes(entityName);
        Attribute attribute = new Attribute();
        attribute.setEntityFieldName("entityField");
        attribute.setType("string");
        attribute.setName("jsonField");
        response.getAttributes().add(attribute);
        return response;
    }

    public static Response simpleResponseWithRelatedAttribute(String entityName, String relatedEntityName) {
        Response response = simpleResponseWithoutAttributes(entityName);
        Attribute attribute = new Attribute();
        attribute.setEntityFieldName("relatedEntityField");
        attribute.setType("object");
        attribute.setName("relatedJsonField");
        attribute.setRelatedEntity(relatedEntityName);
        attribute.setCollection(false);
        response.getAttributes().add(attribute);
        return response;
    }

    public static Response simpleResponseWithRelatedListAttribute(String entityName, String relatedEntityName) {
        Response response = simpleResponseWithoutAttributes(entityName);
        Attribute attribute = new Attribute();
        attribute.setEntityFieldName("relatedEntityListField");
        attribute.setType("array");
        attribute.setName("relatedArrayJsonField");
        attribute.setRelatedEntity(relatedEntityName);
        attribute.setCollection(true);
        response.getAttributes().add(attribute);
        return response;
    }

    public static Response customResponseWithoutAttributes(int responseStatus) {
        Response response = new Response();
        response.setIsStandard(false);
        response.setAttributes(new ArrayList<>());
        response.setIsCollection(false);
        response.setDefaultStatusCode(responseStatus);
        return response;
    }
}
