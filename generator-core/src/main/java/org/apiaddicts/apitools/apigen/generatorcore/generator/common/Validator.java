package org.apiaddicts.apitools.apigen.generatorcore.generator.common;

import org.apiaddicts.apitools.apigen.generatorcore.exceptions.InvalidValuesException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import java.util.Set;

public class Validator {

    private static final jakarta.validation.Validator javaValidator = Validation.buildDefaultValidatorFactory().getValidator();

    private Validator() {
        // Intentional blank
    }

    public static void validate(Object object) {
        Set<ConstraintViolation<Object>> violations = javaValidator.validate(object);
        if (!violations.isEmpty()) {
            throw new InvalidValuesException(violations);
        }
    }
}
