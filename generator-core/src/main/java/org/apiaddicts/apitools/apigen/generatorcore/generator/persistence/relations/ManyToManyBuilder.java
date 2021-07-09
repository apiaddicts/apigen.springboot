package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.relations;

import com.squareup.javapoet.AnnotationSpec;

import javax.persistence.ManyToMany;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.MAPPED_BY;

public class ManyToManyBuilder extends RelatedFieldBuilder {

    private String mappedByFieldName;

    public ManyToManyBuilder(String mappedByFieldName) {
        this.mappedByFieldName = mappedByFieldName;
    }

    @Override
    protected void initialize() {
        AnnotationSpec relationAnnotation = AnnotationSpec.builder(ManyToMany.class)
                .addMember(MAPPED_BY, STRING, mappedByFieldName)
                .build();
        annotations.add(relationAnnotation);
    }
}
