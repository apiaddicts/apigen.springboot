package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common;

public class JavaContextObjectMother {

    private JavaContextObjectMother() {
        // Intentional blank
    }

    public static JavaContext create() {
        return new JavaContext("the.parent", "artifact", "1.0.0");
    }

}
