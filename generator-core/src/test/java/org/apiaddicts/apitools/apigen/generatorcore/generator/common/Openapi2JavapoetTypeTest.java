package org.apiaddicts.apitools.apigen.generatorcore.generator.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Openapi2JavapoetTypeTest {

    @Test
    void givenStringTypeWithoutFormat_whenTransformToSimpleType_thenReturnJavaString() {
        assertEquals("java.lang.String", Openapi2JavapoetType.transformSimpleType("string", null).toString());
    }

    @Test
    void givenStringTypeWithByteFormat_whenTransformToSimpleType_thenReturnJavaString() {
        assertEquals("java.lang.String", Openapi2JavapoetType.transformSimpleType("string", "byte").toString());
    }

    @Test
    void givenStringTypeWithBinaryFormat_whenTransformToSimpleType_thenReturnJavaByteArray() {
        assertEquals("byte[]", Openapi2JavapoetType.transformSimpleType("string", "binary").toString());
    }

    @Test
    void givenStringTypeWithDateFormat_whenTransformToSimpleType_thenReturnJavaLocalDate() {
        assertEquals("java.time.LocalDate", Openapi2JavapoetType.transformSimpleType("string", "date").toString());
    }

    @Test
    void givenStringTypeWithDateTimeFormat_whenTransformToSimpleType_thenReturnJavaOffsetDateTime() {
        assertEquals("java.time.OffsetDateTime", Openapi2JavapoetType.transformSimpleType("string", "date-time").toString());
    }

    @Test
    void givenNumberTypeWithoutFormat_whenTransformToSimpleType_thenReturnJavaDouble() {
        assertEquals("java.lang.Double", Openapi2JavapoetType.transformSimpleType("number", null).toString());
    }

    @Test
    void givenNumberTypeWithFloatFormat_whenTransformToSimpleType_thenReturnJavaFloat() {
        assertEquals("java.lang.Float", Openapi2JavapoetType.transformSimpleType("number", "float").toString());
    }

    @Test
    void givenNumberTypeWitDoubleFormat_whenTransformToSimpleType_thenReturnJavaDouble() {
        assertEquals("java.lang.Double", Openapi2JavapoetType.transformSimpleType("number", "double").toString());
    }

    @Test
    void givenIntegerTypeWithoutFormat_whenTransformToSimpleType_thenReturnJavaLong() {
        assertEquals("java.lang.Long", Openapi2JavapoetType.transformSimpleType("integer", null).toString());
    }

    @Test
    void givenIntegerTypeWithInt32Format_whenTransformToSimpleType_thenReturnJavaInteger() {
        assertEquals("java.lang.Integer", Openapi2JavapoetType.transformSimpleType("integer", "int32").toString());
    }

    @Test
    void givenIntegerTypeWithInt64Format_whenTransformToSimpleType_thenReturnJavaLong() {
        assertEquals("java.lang.Long", Openapi2JavapoetType.transformSimpleType("integer", "int64").toString());
    }

    @Test
    void givenBooleanTypeWithoutFormat_whenTransformToSimpleType_thenReturnJavaBoolean() {
        assertEquals("java.lang.Boolean", Openapi2JavapoetType.transformSimpleType("boolean", null).toString());
    }

    @Test
    void givenNonSimpleTypeObject_whenTransformToSimpleType_thenReturnObject() {
        assertEquals("java.lang.Object", Openapi2JavapoetType.transformSimpleType("object", null).toString());
    }

    @Test
    void givenNonSimpleTypeArray_whenTransformToSimpleType_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                Openapi2JavapoetType.transformSimpleType("array", null));
    }

    @Test
    void givenNoType_whenTransformToSimpleType_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                Openapi2JavapoetType.transformSimpleType(null, null));
    }
}