package org.apiaddicts.apitools.apigen.generatorcore.exceptions;

import lombok.AllArgsConstructor;

import jakarta.validation.ConstraintViolation;
import java.util.Set;

@AllArgsConstructor
public class InvalidValuesException extends RuntimeException {
    private transient Set<ConstraintViolation<Object>> violations;

    public Set<ConstraintViolation<Object>> getViolations() {
        return violations;
    }
}
