package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.relations;

import com.squareup.javapoet.AnnotationSpec;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.OneToMany;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.MAPPED_BY;

@Slf4j
public class OneToManyBuilder extends RelatedFieldBuilder {

    private String mappedByFieldName;

    public OneToManyBuilder(String mappedByFieldName) {
        this.mappedByFieldName = mappedByFieldName;
    }

    @Override
    protected void initialize() {
        AnnotationSpec relationAnnotation = AnnotationSpec.builder(OneToMany.class)
                .addMember(MAPPED_BY, STRING, mappedByFieldName)
                .build();
        annotations.add(relationAnnotation);
    }
}
