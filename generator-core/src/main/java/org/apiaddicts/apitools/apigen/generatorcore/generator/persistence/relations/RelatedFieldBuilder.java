package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.relations;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.NAME;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.REFERENCED_COLUMN_NAME;

@Slf4j
public abstract class RelatedFieldBuilder {

    protected List<AnnotationSpec> annotations = new ArrayList<>();

    protected abstract void initialize();

    public void apply(FieldSpec.Builder fieldBuilder) {
        if (annotations.isEmpty()) initialize();
        annotations.forEach(fieldBuilder::addAnnotation);
    }

    protected AnnotationSpec joinColumn(String name, String referencedColumn) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(JoinColumn.class).addMember(NAME, STRING, name);
        if (referencedColumn != null) builder.addMember(REFERENCED_COLUMN_NAME, STRING, referencedColumn);
        return builder.build();
    }

}
