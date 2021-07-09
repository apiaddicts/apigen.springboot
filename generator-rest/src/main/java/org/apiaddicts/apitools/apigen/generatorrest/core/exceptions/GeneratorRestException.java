package org.apiaddicts.apitools.apigen.generatorrest.core.exceptions;

import java.util.Collections;
import java.util.List;

public class GeneratorRestException extends RuntimeException {
    private final List<GeneratorRestErrors> errors;

    public GeneratorRestException(GeneratorRestErrors error) {
        this.errors = Collections.singletonList(error);
    }

    public List<GeneratorRestErrors> getErrors() {
        return errors;
    }
}
