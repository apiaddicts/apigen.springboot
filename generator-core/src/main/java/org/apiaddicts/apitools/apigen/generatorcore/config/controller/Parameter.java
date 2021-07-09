package org.apiaddicts.apitools.apigen.generatorcore.config.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Parameter extends Attribute {
    @JsonProperty("required")
    protected Boolean required;
    @JsonProperty("in")
    private String in;
    @JsonProperty("default_value")
    private Object defaultValue;
}
