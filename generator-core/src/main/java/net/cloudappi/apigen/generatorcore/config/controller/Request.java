package net.cloudappi.apigen.generatorcore.config.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.cloudappi.apigen.generatorcore.config.validation.Validation;

import java.util.List;

@Data
public class Request {
    @JsonProperty("related_entity")
    private String relatedEntity;
    @JsonProperty("attributes")
    private List<Attribute> attributes;
}
