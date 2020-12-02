package net.cloudappi.apigen.generatorcore.generator.persistence.relations;

import com.squareup.javapoet.AnnotationSpec;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.ManyToOne;

import java.util.List;

@Slf4j
public class ManyToOneBuilder extends RelatedFieldBuilder {

    private List<ColumnRelation> columns;

    public ManyToOneBuilder(List<ColumnRelation> columns) {
        this.columns = columns;
    }

    @Override
    protected void initialize() {
        annotations.add(AnnotationSpec.builder(ManyToOne.class).build());
        for (ColumnRelation cr : columns) {
            annotations.add(joinColumn(cr.getName(), cr.getReferencedName()));
        }
    }
}
