package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.relations;

import com.squareup.javapoet.AnnotationSpec;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import java.util.List;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.ENUM_VALUE;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.FETCH;

@Slf4j
public class ManyToOneBuilder extends RelatedFieldBuilder {

    private List<ColumnRelation> columns;

    public ManyToOneBuilder(List<ColumnRelation> columns) {
        this.columns = columns;
    }

    @Override
    protected void initialize() {
        annotations.add(AnnotationSpec.builder(ManyToOne.class)
                .addMember(FETCH, ENUM_VALUE, FetchType.class, FetchType.LAZY.name())
                .build());
        for (ColumnRelation cr : columns) {
            annotations.add(joinColumn(cr.getName(), cr.getReferencedName()));
        }
    }
}
