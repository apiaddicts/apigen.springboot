package net.cloudappi.apigen.generatorcore.generator.persistence.relations;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnRelation {
    private String name;
    private String referencedName;
}
