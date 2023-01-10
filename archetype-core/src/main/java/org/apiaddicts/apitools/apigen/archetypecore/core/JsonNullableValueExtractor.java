package org.apiaddicts.apitools.apigen.archetypecore.core;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.UnwrapByDefault;
import jakarta.validation.valueextraction.ValueExtractor;
import org.openapitools.jackson.nullable.JsonNullable;

@UnwrapByDefault
public class JsonNullableValueExtractor implements ValueExtractor<JsonNullable<@ExtractedValue ?>> {

    @Override
    public void extractValues(JsonNullable<?> originalValue, ValueReceiver receiver) {
        if (originalValue.isPresent()) {
            receiver.value(null, originalValue.get());
        }
    }
}
