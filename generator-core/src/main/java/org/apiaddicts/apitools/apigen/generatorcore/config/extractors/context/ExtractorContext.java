package org.apiaddicts.apitools.apigen.generatorcore.config.extractors.context;

import lombok.AllArgsConstructor;
import org.apiaddicts.apitools.apigen.generatorcore.exceptions.ExtractorException;
import org.apiaddicts.apitools.apigen.generatorcore.exceptions.GeneratorErrors;

import java.util.ArrayList;
import java.util.List;

public class ExtractorContext {

    private List<ExtractorError> errors = new ArrayList<>();

    public void registerError(GeneratorErrors generatorError, String... data) {
        errors.add(new ExtractorError(generatorError, data));
    }

    public void validate() {
        if (!errors.isEmpty()) throw new ExtractorException(errors);
    }

    @AllArgsConstructor
    public static class ExtractorError {
        private GeneratorErrors error;
        private String[] data;

        public int getCode() {
            return error.code;
        }

        public String getMessage() {
            return String.format(error.message, data);
        }

        public String getReference() {
            return String.join(".", data);
        }
    }
}
