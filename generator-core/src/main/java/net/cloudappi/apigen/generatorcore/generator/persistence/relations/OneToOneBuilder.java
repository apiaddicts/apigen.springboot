package net.cloudappi.apigen.generatorcore.generator.persistence.relations;

import com.squareup.javapoet.AnnotationSpec;

import javax.persistence.OneToOne;

import static net.cloudappi.apigen.generatorcore.generator.common.Formats.STRING;
import static net.cloudappi.apigen.generatorcore.generator.common.Members.MAPPED_BY;

public class OneToOneBuilder extends RelatedFieldBuilder {

    private String mappedByFieldName;

    public OneToOneBuilder(String mappedByFieldName) {
        this.mappedByFieldName = mappedByFieldName;
    }

    @Override
    protected void initialize() {
        AnnotationSpec relationAnnotation = AnnotationSpec.builder(OneToOne.class)
                .addMember(MAPPED_BY, STRING, mappedByFieldName)
                .build();
        annotations.add(relationAnnotation);
    }
}
