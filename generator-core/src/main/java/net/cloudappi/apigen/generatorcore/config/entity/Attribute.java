package net.cloudappi.apigen.generatorcore.config.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.cloudappi.apigen.generatorcore.config.validation.Validation;

import java.util.ArrayList;
import java.util.List;

@Data
public class Attribute {

    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("columns")
    private List<Column> columns = new ArrayList<>();
    @JsonProperty("foreign_columns")
    private List<Column> foreignColumns = new ArrayList<>();
    @JsonProperty("relation")
    private Relation relation;
    @JsonProperty("validations")
    private List<Validation> validations = new ArrayList<>();
    @JsonProperty("attributes")
    private List<Attribute> attributes = new ArrayList<>();
    @JsonProperty("is_collection")
    private Boolean isCollection = false;

    public Attribute() {
        // Required for jackson
    }

    public Attribute(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
