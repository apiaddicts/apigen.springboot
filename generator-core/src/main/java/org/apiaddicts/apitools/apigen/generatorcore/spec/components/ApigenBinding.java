package org.apiaddicts.apitools.apigen.generatorcore.spec.components;

import lombok.Data;

@Data
public class ApigenBinding {
    private String model;
    private String childModel;
    private String childParentRelationProperty;
}
