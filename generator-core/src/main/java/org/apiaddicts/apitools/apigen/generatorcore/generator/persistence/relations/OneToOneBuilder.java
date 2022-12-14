package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.relations;

import com.squareup.javapoet.AnnotationSpec;

import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.ENUM_VALUE;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.FETCH;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.MAPPED_BY;

public class OneToOneBuilder extends RelatedFieldBuilder {

    private String mappedByFieldName;

    public OneToOneBuilder(String mappedByFieldName) {
        this.mappedByFieldName = mappedByFieldName;
    }

    @Override
    protected void initialize() {
        AnnotationSpec relationAnnotation = AnnotationSpec.builder(OneToOne.class)
                .addMember(MAPPED_BY, STRING, mappedByFieldName)
                .addMember(FETCH, ENUM_VALUE, FetchType.class, FetchType.LAZY.name())
                .build();
        annotations.add(relationAnnotation);
    }
}
