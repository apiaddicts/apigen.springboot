package net.cloudappi.apigen.generatorcore.generator.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApigenExt2JavapoetTypeTests {

    private static Stream<Arguments> typesProvider() {
        return Stream.of(
                Arguments.of("String", "java.lang.String"),
                Arguments.of("Boolean", "java.lang.Boolean"),
                Arguments.of("Float", "java.lang.Float"),
                Arguments.of("Double", "java.lang.Double"),
                Arguments.of("Integer", "java.lang.Integer"),
                Arguments.of("Long", "java.lang.Long"),
                Arguments.of("LocalDate", "java.time.LocalDate"),
                Arguments.of("OffsetDateTime", "java.time.OffsetDateTime")
        );
    }

    @MethodSource("typesProvider")
    @ParameterizedTest(name = "{index} => type={0} -> javaType={1}")
    void givenType_whenTransformToSimpleType_thenReturnJavaType(String type, String javaType) {
        assertEquals(javaType, ApigenExt2JavapoetType.transformSimpleType(type).toString());
    }

    @Test
    void givenNonSimpleType_whenTransformToSimpleType_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                ApigenExt2JavapoetType.transformSimpleType("Other"));
    }
}
