package org.apiaddicts.apitools.apigen.generatorcore.config.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Response {
    @JsonProperty("is_standard")
    private Boolean isStandard;
    @JsonProperty("is_collection")
    private Boolean isCollection;
    @JsonProperty("collection_name")
    private String collectionName;
    @JsonProperty("related_entity")
    private String relatedEntity;
    @JsonProperty("attributes")
    private List<Attribute> attributes;
    @JsonProperty("default_status_code")
    private Integer defaultStatusCode;
    @JsonProperty("mime_type")
    private String mimeType;
}
