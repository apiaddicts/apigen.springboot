package org.apiaddicts.apitools.apigen.generatorcore.config.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

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
    @JsonProperty("parent-entity")
    private String ParentEntity;
    @JsonProperty("child-parent-relation-property")
    private String ChildParentRelationProperty;

    public enum Method {
        GET("Find"), POST("Create"), PUT("Update"), DELETE("Delete");
        public final String prefix;

        Method(String prefix) {
            this.prefix = prefix;
        }
    }
}
