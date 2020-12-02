package net.cloudappi.apigen.generatorcore.config.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Controller {
    @JsonProperty("entity")
    private String entity;
    @JsonProperty("mapping")
    private String mapping;
    @JsonProperty("endpoints")
    private List<Endpoint> endpoints;
}
