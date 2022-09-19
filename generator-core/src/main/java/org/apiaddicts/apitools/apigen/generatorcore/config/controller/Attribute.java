package org.apiaddicts.apitools.apigen.generatorcore.config.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apiaddicts.apitools.apigen.generatorcore.config.validation.Validation;

import java.util.List;

@Data
public class Attribute {
    @JsonProperty("name")
    protected String name;
    @JsonProperty("validations")
    List<Validation> validations;
    @JsonProperty("attributes")
    List<Attribute> attributes;
    @JsonProperty("type")
    protected String type;
    @JsonProperty("format")
    protected String format;
    @JsonProperty("implementation_type")
    protected String implementationType;
    @JsonProperty("is_collection")
    protected boolean isCollection;
    @JsonProperty("related_entity")
    private String relatedEntity;
    @JsonProperty("entity_field_name")
    private String entityFieldName;
}
