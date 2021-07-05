package org.apiaddicts.apitools.apigen.generatorcore.exceptions;

import lombok.Getter;
import org.apiaddicts.apitools.apigen.generatorcore.config.extractors.context.ExtractorContext;

import java.util.List;

@Getter
public class ExtractorException extends RuntimeException {
    private final List<ExtractorContext.ExtractorError> errors;

    public ExtractorException(List<ExtractorContext.ExtractorError> errors) {
        this.errors = errors;
    }
}
