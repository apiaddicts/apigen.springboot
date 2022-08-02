package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen;

public class ApigenContextObjectMother {

    private ApigenContextObjectMother() {
        // Intentional blank
    }

    public static ApigenContext create() {
        return new ApigenContext("the.parent", "artifact", "1.0.0");
    }

}
