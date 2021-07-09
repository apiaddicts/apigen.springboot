package org.apiaddicts.apitools.apigen.generatorcore.config.extractors;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;

import java.util.ArrayList;
import java.util.List;

public class ParametersExtractor {

    private final ValidationsExtractor validationsExtractor;

    public ParametersExtractor() {
        this.validationsExtractor = new ValidationsExtractor();
    }

    public List<Parameter> readParameters(List<io.swagger.v3.oas.models.parameters.Parameter> parameters) {
        List<Parameter> params = new ArrayList<>();
        if (parameters == null) return params;
        for (io.swagger.v3.oas.models.parameters.Parameter parameter : parameters) {
            Parameter param = new Parameter();
            param.setName(parameter.getName());
            Schema<?> itemSchema = parameter.getSchema();

            param.setDefaultValue(itemSchema.getDefault());
            param.setValidations(validationsExtractor.getValidations(itemSchema, false));

            boolean isCollection = itemSchema instanceof ArraySchema;
            param.setCollection(isCollection);
            if (isCollection) itemSchema = ((ArraySchema) itemSchema).getItems();

            Boolean required = parameter.getRequired();
            if (required == null) required = false;
            param.setRequired(required);

            param.setType(itemSchema.getType());
            param.setFormat(itemSchema.getFormat());
            param.setIn(parameter.getIn());
            params.add(param);
        }
        return params;
    }
}
