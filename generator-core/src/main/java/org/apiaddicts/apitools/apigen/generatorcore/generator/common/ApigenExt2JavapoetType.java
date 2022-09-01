package org.apiaddicts.apitools.apigen.generatorcore.generator.common;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ApigenExt2JavapoetType {
    private static final String TYPE_STRING = "String";
    private static final String TYPE_BOOLEAN = "Boolean";
    private static final String TYPE_FLOAT = "Float";
    private static final String TYPE_DOUBLE = "Double";
    private static final String TYPE_BIG_DECIMAL = "BigDecimal";
    private static final String TYPE_INTEGER = "Integer";
    private static final String TYPE_LONG = "Long";
    private static final String TYPE_BIG_INTEGER = "BigInteger";
    private static final String TYPE_LOCAL_DATE = "LocalDate";
    private static final String TYPE_OFFSET_DATE_TIME = "OffsetDateTime";
    private static final String TYPE_LOCAL_DATE_TIME = "LocalDateTime";
    private static final String TYPE_ZONED_DATE_TIME = "ZonedDateTime";
    private static final String TYPE_INSTANT = "Instant";
    private static final String TYPE_COMPOSED_ID = "ComposedID";

    private static final Set<String> BASIC_TYPES = new HashSet<>(Arrays.asList(
     TYPE_STRING,
     TYPE_BOOLEAN,
     TYPE_FLOAT,
     TYPE_DOUBLE,
     TYPE_BIG_DECIMAL,
     TYPE_INTEGER,
     TYPE_LONG,
     TYPE_BIG_INTEGER,
     TYPE_LOCAL_DATE,
     TYPE_OFFSET_DATE_TIME,
     TYPE_LOCAL_DATE_TIME,
     TYPE_ZONED_DATE_TIME,
     TYPE_INSTANT
    ));

    private ApigenExt2JavapoetType() {
        // Intentional blank
    }

    public static boolean isComposedID(String type) {
        return TYPE_COMPOSED_ID.equals(type);
    }

    public static boolean isBasicType(String type) {
        return BASIC_TYPES.contains(type);
    }

    public static boolean isString(String type) {
        return TYPE_STRING.equals(type);
    }

    public static boolean isNumeric(String type) {
        return TYPE_INTEGER.equals(type) || TYPE_LONG.equals(type);
    }

    public static TypeName transformSimpleType(String type) {
        switch (type) {
            case TYPE_STRING:
                return getTypeName(String.class);
            case TYPE_BOOLEAN:
                return getTypeName(Boolean.class);
            case TYPE_FLOAT:
                return getTypeName(Float.class);
            case TYPE_DOUBLE:
                return getTypeName(Double.class);
            case TYPE_BIG_DECIMAL:
                return getTypeName(BigDecimal.class);
            case TYPE_INTEGER:
                return getTypeName(Integer.class);
            case TYPE_LONG:
                return getTypeName(Long.class);
            case TYPE_BIG_INTEGER:
                return getTypeName(BigInteger.class);
            case TYPE_LOCAL_DATE:
                return getTypeName(LocalDate.class);
            case TYPE_OFFSET_DATE_TIME:
                return getTypeName(OffsetDateTime.class);
            case TYPE_ZONED_DATE_TIME:
                return getTypeName(ZonedDateTime.class);
            case TYPE_LOCAL_DATE_TIME:
                return getTypeName(LocalDateTime.class);
            case TYPE_INSTANT:
                return getTypeName(Instant.class);
            default:
                throw new IllegalArgumentException("Type " + type + " not supported");
        }
    }

    public static TypeName transformType(String implementationType) {
        if (implementationType == null) return null;
        String[] parts = implementationType.split("\\.", 2);
        if (parts.length == 2) return ClassName.get(parts[0], parts[1]);
        return transformSimpleType(parts[0]);
    }

    private static TypeName getTypeName(Class clazz) {
        return ClassName.get(clazz);
    }
}
