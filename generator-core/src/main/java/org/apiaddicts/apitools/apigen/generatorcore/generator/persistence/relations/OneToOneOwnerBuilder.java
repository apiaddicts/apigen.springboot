package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.relations;

import com.squareup.javapoet.AnnotationSpec;

import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.util.List;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.ENUM_VALUE;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.FETCH;

public class OneToOneOwnerBuilder extends RelatedFieldBuilder {

    private List<ColumnRelation> columns;

    public OneToOneOwnerBuilder(List<ColumnRelation> columns) {
        this.columns = columns;
    }

    @Override
    protected void initialize() {
        annotations.add(AnnotationSpec.builder(OneToOne.class)
                .addMember(FETCH, ENUM_VALUE, FetchType.class, FetchType.LAZY.name())
                .build());
        for (ColumnRelation cr : columns) {
            annotations.add(joinColumn(cr.getName(), cr.getReferencedName()));
        }
    }
}
