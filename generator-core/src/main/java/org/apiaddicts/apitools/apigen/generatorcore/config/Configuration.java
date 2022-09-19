package org.apiaddicts.apitools.apigen.generatorcore.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Configuration {

    @NotBlank
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @NotBlank
    @JsonProperty("group")
    private String group;
    @NotBlank
    @JsonProperty("artifact")
    private String artifact;
    @NotBlank
    @JsonProperty("version")
    private String version;
    @NotBlank
    @JsonProperty("base_package")
    public String basePackage;
    @NotNull
    @JsonProperty("partial")
    private Boolean partial;
    @NotNull
    @JsonProperty("entities")
    private List<Entity> entities;
    @NotEmpty
    @JsonProperty("controllers")
    private List<Controller> controllers;

    @JsonProperty("standard_response_operations")
    private ArrayNode standardResponseOperations;
}
