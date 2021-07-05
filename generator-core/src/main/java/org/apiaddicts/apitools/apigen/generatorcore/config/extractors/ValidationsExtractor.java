package org.apiaddicts.apitools.apigen.generatorcore.config.extractors;

import io.swagger.v3.oas.models.media.Schema;
import org.apiaddicts.apitools.apigen.generatorcore.config.validation.Validation;
import org.apiaddicts.apitools.apigen.generatorcore.config.validation.ValidationType;
import org.apiaddicts.apitools.apigen.generatorcore.spec.components.ApigenModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ValidationsExtractor {

    public List<Validation> getValidations(ApigenModel.ApigenModelAttribute modelAttribute) {
        return modelAttribute.getValidations().stream().map(this::toValidation).collect(Collectors.toList());
    }

    private Validation toValidation(ApigenModel.AttributeValidation modelValidation) {
        String type = modelValidation.getType();
        switch (type) {
            case "NotNull":
                return new Validation(ValidationType.NOT_NULL);
            case "Size":
                return new Validation(ValidationType.SIZE, modelValidation.getMin(), modelValidation.getMax());
            case "Min":
                return new Validation(ValidationType.MIN, Long.parseLong(modelValidation.getValue()));
            case "Max":
                return new Validation(ValidationType.MAX, Long.parseLong(modelValidation.getValue()));
            case "Email":
                return new Validation(ValidationType.EMAIL);
            case "NotEmpty":
                return new Validation(ValidationType.NOT_EMPTY);
            case "NotBlank":
                return new Validation(ValidationType.NOT_BLANK);
            case "Positive":
                return new Validation(ValidationType.POSITIVE);
            case "PositiveOrZero":
                return new Validation(ValidationType.POSITIVE_OR_ZERO);
            case "Negative":
                return new Validation(ValidationType.NEGATIVE);
            case "NegativeOrZero":
                return new Validation(ValidationType.NEGATIVE_OR_ZERO);
            case "Past":
                return new Validation(ValidationType.PAST);
            case "PastOrPresent":
                return new Validation(ValidationType.PAST_OR_PRESENT);
            case "Future":
                return new Validation(ValidationType.FUTURE);
            case "FutureOrPresent":
                return new Validation(ValidationType.FUTURE_OR_PRESENT);
            case "Pattern":
                return new Validation(ValidationType.PATTERN, modelValidation.getRegex());
            case "Digits":
                return new Validation(ValidationType.DIGITS, modelValidation.getInteger(), modelValidation.getFraction());
            case "DecimalMin":
                return new Validation(ValidationType.DECIMAL_MIN, new BigDecimal(modelValidation.getValue()), modelValidation.isInclusive());
            case "DecimalMax":
                return new Validation(ValidationType.DECIMAL_MAX, new BigDecimal(modelValidation.getValue()), modelValidation.isInclusive());
            default:
                throw new IllegalArgumentException("Model validation type " + type + " not supported");
        }
    }

    public List<Validation> getValidations(Schema schema, boolean required) {
        List<Validation> validations = new ArrayList<>();
        addRequiredValidation(required, validations);
        addNumberValidations(schema, validations);
        addSizeValidation(schema, validations);
        addPatternValidation(schema, validations);
        addEmailValidation(schema, validations);
        return validations;
    }

    private void addRequiredValidation(boolean required, List<Validation> validations) {
        if (!required) return;
        validations.add(new Validation(ValidationType.NOT_NULL));
    }

    private void addNumberValidations(Schema schema, List<Validation> validations) {
        boolean isDecimalNumber = "number".equalsIgnoreCase(schema.getType());
        addNumberMinimumValidations(schema, isDecimalNumber, validations);
        addNumberMaximumValidations(schema, isDecimalNumber, validations);
    }

    private void addNumberMinimumValidations(Schema schema, boolean isDecimal, List<Validation> validations) {
        if (isNull(schema.getMinimum())) return;
        BigDecimal minimum = schema.getMinimum();
        boolean inclusive = schema.getExclusiveMinimum() == null || !schema.getExclusiveMinimum();
        if (isDecimal) {
            validations.add(new Validation(ValidationType.DECIMAL_MIN, minimum, inclusive));
        } else {
            long minimumLong = minimum.longValue();
            if (!inclusive) minimumLong += 1;
            validations.add(new Validation(ValidationType.MIN, minimumLong));
        }
    }

    private void addNumberMaximumValidations(Schema schema, boolean isDecimal, List<Validation> validations) {
        if (isNull(schema.getMaximum())) return;
        BigDecimal maximum = schema.getMaximum();
        boolean inclusive = schema.getExclusiveMaximum() == null || !schema.getExclusiveMaximum();
        if (isDecimal) {
            validations.add(new Validation(ValidationType.DECIMAL_MAX, maximum, inclusive));
        } else {
            long maximumLong = maximum.longValue();
            if (!inclusive) maximumLong -= 1;
            validations.add(new Validation(ValidationType.MAX, maximumLong));
        }
    }

    private void addSizeValidation(Schema schema, List<Validation> validations) {
        if (nonNull(schema.getMinLength()) || nonNull(schema.getMaxLength())) {
            validations.add(new Validation(ValidationType.SIZE, schema.getMinLength(), schema.getMaxLength()));
        }
        if (nonNull(schema.getMinItems()) || nonNull(schema.getMaxItems())) {
            validations.add(new Validation(ValidationType.SIZE, schema.getMinItems(), schema.getMaxItems()));
        }
    }

    private void addPatternValidation(Schema schema, List<Validation> validations) {
        if (isNull(schema.getPattern())) return;
        validations.add(new Validation(ValidationType.PATTERN, schema.getPattern()));
    }

    private void addEmailValidation(Schema schema, List<Validation> validations) {
        if (!"string".equals(schema.getType()) || !"email".equals(schema.getFormat())) return;
        validations.add(new Validation(ValidationType.EMAIL));
    }
}
