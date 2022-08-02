package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence;

import com.squareup.javapoet.FieldSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Deprecated
class EntityRelationManagerTest {

    @Test
    void manyToOne() {
        Entity entity = EntityObjectMother.withManyToOne("One", "field", "Other", "column");

        EntityRelationManager m = new EntityRelationManager(Arrays.asList(entity));

        FieldSpec.Builder builder = FieldSpec.builder(String.class, "field", Modifier.PRIVATE);

        m.applyRelation("One", entity.getAttributes().get(0), builder);

        assertEquals("[" +
                        "@javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY), " +
                        "@javax.persistence.JoinColumn(name = \"column\")" +
                        "]",
                builder.annotations.toString());
    }
}
