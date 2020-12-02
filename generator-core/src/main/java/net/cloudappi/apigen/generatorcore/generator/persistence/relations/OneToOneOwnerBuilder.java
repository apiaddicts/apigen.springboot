package net.cloudappi.apigen.generatorcore.generator.persistence.relations;

import com.squareup.javapoet.AnnotationSpec;

import javax.persistence.OneToOne;

import java.util.List;

public class OneToOneOwnerBuilder extends RelatedFieldBuilder {

    private List<ColumnRelation> columns;

    public OneToOneOwnerBuilder(List<ColumnRelation> columns) {
        this.columns = columns;
    }

    @Override
    protected void initialize() {
        annotations.add(AnnotationSpec.builder(OneToOne.class).build());
        for (ColumnRelation cr : columns) {
            annotations.add(joinColumn(cr.getName(), cr.getReferencedName()));
        }
    }
}
