package org.apiaddicts.apitools.apigen.generatorcore.generator.common;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class ApigenExt2JavapoetType {
    private static final String TYPE_STRING = "String";
    private static final String TYPE_BOOLEAN = "Boolean";
    private static final String TYPE_FLOAT = "Float";
    private static final String TYPE_DOUBLE = "Double";
    private static final String TYPE_INTEGER = "Integer";
    private static final String TYPE_LONG = "Long";
    private static final String TYPE_LOCAL_DATE = "LocalDate";
    private static final String TYPE_OFFSET_DATE_TIME = "OffsetDateTime";
    private static final String TYPE_COMPOSED_ID = "ComposedID";

    private ApigenExt2JavapoetType() {
        // Intentional blank
    }

    public static boolean isComposedID(String type) {
        return TYPE_COMPOSED_ID.equals(type);
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
            case TYPE_INTEGER:
                return getTypeName(Integer.class);
            case TYPE_LONG:
                return getTypeName(Long.class);
            case TYPE_LOCAL_DATE:
                return getTypeName(LocalDate.class);
            case TYPE_OFFSET_DATE_TIME:
                return getTypeName(OffsetDateTime.class);
            default:
                throw new IllegalArgumentException("Type " + type + " not supported");
        }
    }

    private static TypeName getTypeName(Class clazz) {
        return ClassName.get(clazz);
    }
}
