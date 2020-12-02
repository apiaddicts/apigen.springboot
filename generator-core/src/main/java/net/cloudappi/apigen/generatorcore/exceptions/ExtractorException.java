package net.cloudappi.apigen.generatorcore.exceptions;

import lombok.Getter;
import net.cloudappi.apigen.generatorcore.config.extractors.context.ExtractorContext;

import java.util.List;

@Getter
public class ExtractorException extends RuntimeException {
    private final List<ExtractorContext.ExtractorError> errors;

    public ExtractorException(List<ExtractorContext.ExtractorError> errors) {
        this.errors = errors;
    }
}
