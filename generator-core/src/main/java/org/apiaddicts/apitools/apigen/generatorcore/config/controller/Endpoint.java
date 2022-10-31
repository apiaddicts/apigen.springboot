package org.apiaddicts.apitools.apigen.generatorcore.config.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Endpoint {
    @JsonProperty("method")
    private Method method;
    @JsonProperty("mapping")
    private String mapping;
    @JsonProperty("name")
    private String name;
    @JsonProperty("parameters")
    private List<Parameter> parameters;
    @JsonProperty("response")
    private Response response;
    @JsonProperty("request")
    private Request request;
    @JsonProperty("related_entity")
    private String relatedEntity;
    @JsonProperty("parent_entity")
    private String parentEntity;
    @JsonProperty("child_parent_relation_property")
    private String childParentRelationProperty;

    public enum Method {
        GET("Find"), POST("Create"), PATCH("PartialUpdate"), PUT("Update"), DELETE("Delete");
        public final String prefix;

        Method(String prefix) {
            this.prefix = prefix;
        }
    }
}
