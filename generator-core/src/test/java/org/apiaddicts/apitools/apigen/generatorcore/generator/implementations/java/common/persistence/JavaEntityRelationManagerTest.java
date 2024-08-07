package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence;

import com.squareup.javapoet.FieldSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JavaEntityRelationManagerTest {

    @Test
    void manyToOne() {
        Entity entity = EntityObjectMother.withManyToOne("One", "field", "Other", "column");

        JavaEntityRelationManager m = new JavaEntityRelationManager(Collections.singletonList(entity));

        FieldSpec.Builder builder = FieldSpec.builder(String.class, "field", Modifier.PRIVATE);

        m.applyRelation("One", entity.getAttributes().get(0), builder);

        assertEquals("[" +
                        "@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY), " +
                        "@jakarta.persistence.JoinColumn(name = \"column\")" +
                        "]",
                builder.annotations.toString());
    }
}
