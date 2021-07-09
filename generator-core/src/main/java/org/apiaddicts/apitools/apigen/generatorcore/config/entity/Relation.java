package org.apiaddicts.apitools.apigen.generatorcore.config.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Relation {
    @JsonProperty("type")
    private RelationType type;
    @JsonProperty("related_entity")
    private String relatedEntity;
    @JsonProperty("intermediate_table")
    private String intermediateTable;
    @JsonProperty("owner")
    private Boolean owner = false;
    @JsonProperty("columns")
    private List<Column> columns = new ArrayList<>();
    @JsonProperty("reverse_columns")
    private List<Column> reverseColumns = new ArrayList<>();

    public Relation() {
    }

    public Relation(String relatedEntity) {
        this.relatedEntity = relatedEntity;
    }

    public Relation(String relatedEntity, Boolean owner) {
        this.relatedEntity = relatedEntity;
        this.owner = owner;
    }

    public Relation(String relatedEntity, List<Column> columns, List<Column> reverseColumns, String intermediateTable) {
        this.relatedEntity = relatedEntity;
        this.columns = columns;
        this.reverseColumns = reverseColumns;
        this.intermediateTable = intermediateTable;
    }

    public Relation(String relatedEntity, List<Column> columns, List<Column> reverseColumns, String intermediateTable, Boolean owner) {
        this.relatedEntity = relatedEntity;
        this.columns = columns;
        this.reverseColumns = reverseColumns;
        this.intermediateTable = intermediateTable;
        this.owner = owner;
    }
}
