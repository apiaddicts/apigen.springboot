package org.apiaddicts.apitools.apigen.generatorcore.generator.common;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Openapi2JavapoetType {

    public static final String TYPE_STRING = "string";
    public static final String TYPE_NUMBER = "number";
    public static final String TYPE_INTEGER = "integer";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_ARRAY = "array";
    public static final String TYPE_OBJECT = "object";

    private Openapi2JavapoetType() {
        // intentional blank
    }

    public static boolean isSimpleType(String type) {
        return TYPE_STRING.equals(type) || TYPE_NUMBER.equals(type) || TYPE_INTEGER.equals(type) || TYPE_BOOLEAN.equals(type);
    }

    public static TypeName transformSimpleType(String type, String format) {
        Class clazz = getClass(type, format);
        if (clazz.equals(byte[].class)) {
            return ArrayTypeName.of(byte.class);
        }
        return ClassName.get(clazz);
    }

    private static Class getClass(String type, String format) {
        if (type == null) throw new IllegalArgumentException("Type is required");
        type = low(type);
        format = low(format);
        switch (type) {
            case TYPE_STRING:
                return getStringClass(format);
            case TYPE_NUMBER:
                return getNumberClass(format);
            case TYPE_INTEGER:
                return getIntegerClass(format);
            case TYPE_BOOLEAN:
                return getBooleanClass();
            case TYPE_OBJECT:
                return getObjectClass();
            default:
                throw new IllegalArgumentException("Type " + type + " with format " + format + " not supported");
        }
    }

    private static Class getStringClass(String format) {
        if (format == null) return String.class;
        switch (format) {
            case "date":
                return LocalDate.class;
            case "date-time":
                return OffsetDateTime.class;
            case "binary":
                return byte[].class;
            case "byte":
            default:
                return String.class;
        }
    }

    private static Class getNumberClass(String format) {
        if (format == null) return Double.class;
        switch (format) {
            case "float":
                return Float.class;
            case "double":
            default:
                return Double.class;
        }

    }

    private static Class getIntegerClass(String format) {
        if (format == null) return Long.class;
        switch (format) {
            case "int32":
                return Integer.class;
            case "int64":
            default:
                return Long.class;
        }
    }

    private static Class getBooleanClass() {
        return Boolean.class;
    }

    private static Class getObjectClass() {
        return Object.class;
    }

    private static String low(String value) {
        return value != null ? value.toLowerCase() : null;
    }
}
