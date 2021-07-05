package org.apiaddicts.apitools.apigen.generatorcore.config.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterSpec;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Validation {
    @JsonProperty("type")
    private ValidationType type;
    @JsonProperty("integer_value_one")
    private Integer integerValueOne;
    @JsonProperty("integer_value_two")
    private Integer integerValueTwo;
    @JsonProperty("decimal_value")
    private BigDecimal decimalValue;
    @JsonProperty("long_value")
    private Long longValue;
    @JsonProperty("string_value")
    private String stringValue;
    @JsonProperty("inclusive")
    private boolean inclusive;

    private Validation() {
        // Required for jackson
    }

    public Validation(ValidationType type) {
        this.type = type;
    }

    public Validation(ValidationType type, String value) {
        this.type = type;
        this.stringValue = value;
    }

    public Validation(ValidationType type, Integer integerValueOne, Integer integerValueTwo) {
        this.type = type;
        this.integerValueOne = integerValueOne;
        this.integerValueTwo = integerValueTwo;
    }

    public Validation(ValidationType type, BigDecimal value, boolean inclusive) {
        this.type = type;
        this.decimalValue = value;
        this.inclusive = inclusive;
    }

    public Validation(ValidationType type, Long value) {
        this.type = type;
        this.longValue = value;
    }

    public void apply(FieldSpec.Builder builder) {
        type.apply(this, builder);
    }

    public void apply(ParameterSpec.Builder builder) {
        type.apply(this, builder);
    }
}
