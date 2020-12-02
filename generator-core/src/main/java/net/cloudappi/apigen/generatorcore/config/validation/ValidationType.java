package net.cloudappi.apigen.generatorcore.config.validation;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterSpec;
import net.cloudappi.apigen.generatorcore.generator.common.Members;

import javax.validation.constraints.*;
import java.math.BigDecimal;

import static java.util.Objects.nonNull;
import static net.cloudappi.apigen.generatorcore.generator.common.Formats.LITERAL;
import static net.cloudappi.apigen.generatorcore.generator.common.Formats.STRING;

public enum ValidationType {

    NOT_NULL(NotNull.class),
    EMAIL(Email.class),
    NOT_EMPTY(NotEmpty.class),
    NOT_BLANK(NotBlank.class),
    POSITIVE(Positive.class),
    POSITIVE_OR_ZERO(PositiveOrZero.class),
    NEGATIVE(Negative.class),
    NEGATIVE_OR_ZERO(NegativeOrZero.class),
    PAST(Past.class),
    PAST_OR_PRESENT(PastOrPresent.class),
    FUTURE(Future.class),
    FUTURE_OR_PRESENT(FutureOrPresent.class),
    MIN(Min.class) {
        @Override
        public void apply(Validation validation, FieldSpec.Builder builder) {
            builder.addAnnotation(getLongAnnotation(this.enumClass, validation.getLongValue()));
        }

        @Override
        public void apply(Validation validation, ParameterSpec.Builder builder) {
            builder.addAnnotation(getLongAnnotation(this.enumClass, validation.getLongValue()));
        }
    },
    MAX(Max.class) {
        @Override
        public void apply(Validation validation, FieldSpec.Builder builder) {
            builder.addAnnotation(getLongAnnotation(this.enumClass, validation.getLongValue()));
        }

        @Override
        public void apply(Validation validation, ParameterSpec.Builder builder) {
            builder.addAnnotation(getLongAnnotation(this.enumClass, validation.getLongValue()));
        }
    },
    DECIMAL_MIN(DecimalMin.class) {
        @Override
        public void apply(Validation validation, FieldSpec.Builder builder) {
            builder.addAnnotation(getDecimalAnnotation(this.enumClass, validation.getDecimalValue(), validation.isInclusive()));
        }

        @Override
        public void apply(Validation validation, ParameterSpec.Builder builder) {
            builder.addAnnotation(getDecimalAnnotation(this.enumClass, validation.getDecimalValue(), validation.isInclusive()));
        }
    },
    DECIMAL_MAX(DecimalMax.class) {
        @Override
        public void apply(Validation validation, FieldSpec.Builder builder) {
            builder.addAnnotation(getDecimalAnnotation(this.enumClass, validation.getDecimalValue(), validation.isInclusive()));
        }

        @Override
        public void apply(Validation validation, ParameterSpec.Builder builder) {
            builder.addAnnotation(getDecimalAnnotation(this.enumClass, validation.getDecimalValue(), validation.isInclusive()));
        }
    },
    SIZE(Size.class) {
        @Override
        public void apply(Validation validation, FieldSpec.Builder builder) {
            builder.addAnnotation(getSizeAnnotation(validation.getIntegerValueOne(), validation.getIntegerValueTwo()));
        }

        @Override
        public void apply(Validation validation, ParameterSpec.Builder builder) {
            builder.addAnnotation(getSizeAnnotation(validation.getIntegerValueOne(), validation.getIntegerValueTwo()));
        }
    },
    DIGITS(Digits.class) {
        @Override
        public void apply(Validation validation, FieldSpec.Builder builder) {
            builder.addAnnotation(getDigitsAnnotation(validation.getIntegerValueOne(), validation.getIntegerValueTwo()));
        }

        @Override
        public void apply(Validation validation, ParameterSpec.Builder builder) {
            builder.addAnnotation(getDigitsAnnotation(validation.getIntegerValueOne(), validation.getIntegerValueTwo()));
        }
    },
    PATTERN(Pattern.class) {
        @Override
        public void apply(Validation validation, FieldSpec.Builder builder) {
            builder.addAnnotation(getPatternAnnotation(validation.getStringValue()));
        }

        @Override
        public void apply(Validation validation, ParameterSpec.Builder builder) {
            builder.addAnnotation(getPatternAnnotation(validation.getStringValue()));
        }
    };

    protected Class<?> enumClass;

    ValidationType(Class enumClass) {
        this.enumClass = enumClass;
    }

    private static AnnotationSpec getDefaultAnnotation(Class<?> enumClass, String value) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(enumClass);
        if (value != null) builder.addMember(Members.VALUE, value);
        return builder.build();
    }

    private static AnnotationSpec getLongAnnotation(Class<?> enumClass, Long value) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(enumClass);
        if (value != null) builder.addMember(Members.VALUE, LITERAL, value);
        return builder.build();
    }

    private static AnnotationSpec getDecimalAnnotation(Class<?> enumClass, BigDecimal value, boolean inclusive) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(enumClass);
        if (value != null)
            builder.addMember(Members.VALUE, STRING, value.toString()).addMember(Members.INCLUSIVE, LITERAL, inclusive);
        return builder.build();
    }

    private static AnnotationSpec getSizeAnnotation(Integer min, Integer max) {
        AnnotationSpec.Builder annotationSpec = AnnotationSpec.builder(Size.class);
        if (nonNull(min)) annotationSpec.addMember(Members.MIN, LITERAL, min);
        if (nonNull(max)) annotationSpec.addMember(Members.MAX, LITERAL, max);
        return annotationSpec.build();
    }

    private static AnnotationSpec getDigitsAnnotation(int integer, int fraction) {
        return AnnotationSpec.builder(Digits.class)
                .addMember(Members.INTEGER, LITERAL, integer)
                .addMember(Members.FRACTION, LITERAL, fraction)
                .build();
    }

    private static AnnotationSpec getPatternAnnotation(String regex) {
        return AnnotationSpec.builder(Pattern.class).addMember(Members.REGEXP, STRING, regex).build();
    }

    public void apply(Validation validation, FieldSpec.Builder builder) {
        builder.addAnnotation(getDefaultAnnotation(enumClass, validation.getStringValue()));
    }

    public void apply(Validation validation, ParameterSpec.Builder builder) {
        builder.addAnnotation(getDefaultAnnotation(enumClass, validation.getStringValue()));
    }
}
