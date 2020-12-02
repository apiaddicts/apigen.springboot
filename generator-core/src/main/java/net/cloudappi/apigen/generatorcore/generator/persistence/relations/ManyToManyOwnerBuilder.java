package net.cloudappi.apigen.generatorcore.generator.persistence.relations;

import com.squareup.javapoet.AnnotationSpec;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static net.cloudappi.apigen.generatorcore.generator.common.Formats.LITERAL;
import static net.cloudappi.apigen.generatorcore.generator.common.Formats.STRING;
import static net.cloudappi.apigen.generatorcore.generator.common.Members.*;

public class ManyToManyOwnerBuilder extends RelatedFieldBuilder {

    private String intermediateTable;
    private List<ColumnRelation> columns;
    private List<ColumnRelation> inverseColumns;

    public ManyToManyOwnerBuilder(String intermediateTable, List<ColumnRelation> columns, List<ColumnRelation> inverseColumns) {
        this.intermediateTable = intermediateTable;
        this.columns = columns;
        this.inverseColumns = inverseColumns;
    }

    @Override
    protected void initialize() {
        AnnotationSpec relationAnnotation = AnnotationSpec.builder(ManyToMany.class).build();
        annotations.add(relationAnnotation);
        AnnotationSpec joinTable = joinTable(intermediateTable, columns, inverseColumns);
        annotations.add(joinTable);
    }

    private AnnotationSpec joinTable(String intermediateTable, List<ColumnRelation> columns, List<ColumnRelation> inverseColumns) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(JoinTable.class).addMember(NAME, STRING, intermediateTable);
        List<String> columnNames = new ArrayList<>();
        for (ColumnRelation cr : columns) {
            columnNames.add(cr.getName());
            builder.addMember(JOIN_COLUMNS, LITERAL, joinColumn(cr.getName(), cr.getReferencedName()));
        }
        for (ColumnRelation cr : inverseColumns) {
            columnNames.add(cr.getName());
            builder.addMember(INVERSE_JOIN_COLUMNS, LITERAL, joinColumn(cr.getName(), cr.getReferencedName()));
        }
        return builder.addMember(UNIQUE_CONSTRAINTS, LITERAL, uniqueConstraint(columnNames)).build();
    }

    private AnnotationSpec uniqueConstraint(List<String> constrains) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(UniqueConstraint.class);
        for (String constrain : constrains) {
            builder.addMember(COLUMN_NAMES, STRING, constrain);
        }
        return builder.build();
    }
}
