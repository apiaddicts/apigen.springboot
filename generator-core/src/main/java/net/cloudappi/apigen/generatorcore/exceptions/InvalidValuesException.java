package net.cloudappi.apigen.generatorcore.exceptions;

import lombok.AllArgsConstructor;

import javax.validation.ConstraintViolation;
import java.util.Set;

@AllArgsConstructor
public class InvalidValuesException extends RuntimeException {
    private transient Set<ConstraintViolation<Object>> violations;

    public Set<ConstraintViolation<Object>> getViolations() {
        return violations;
    }
}
