package net.cloudappi.apigen.generatorcore.config.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Entity {
    @JsonProperty("name")
    private String name;
    @JsonProperty("table")
    private String table;
    @JsonProperty("attributes")
    private List<Attribute> attributes;

    public Entity() {
        // Required for jackson
    }

    public Entity(String name, String table, List<Attribute> attributes) {
        this.name = name;
        this.table = table;
        this.attributes = attributes;
    }
}
