package org.apiaddicts.apitools.apigen.generatorcore.config.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Request {
    @JsonProperty("related_entity")
    private String relatedEntity;
    @JsonProperty("attributes")
    private List<Attribute> attributes;
    @JsonProperty("mime_type")
    private String mimeType;
}
