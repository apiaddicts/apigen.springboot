package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.executor;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApigenSearchExecutorTypeConvertersTest {

    @MethodSource("converterProvider")
    @ParameterizedTest(name = "{index} => class={0} strValue={1} -> value={2}")
    void givenDefaultTypes_whenConvert_thenSuccess(Class clazz, String strValue, Comparable value) {
        Comparable result = ApigenSearchExecutorTypeConverters.convert(clazz, strValue);
        assertEquals(value, result);
    }

    @Test
    void givenRegisteredType_whenConvert_thenSuccess() {
        ApigenSearchExecutorTypeConverters.register(CustomType.class, s -> new CustomType(s));
        Comparable result = ApigenSearchExecutorTypeConverters.convert(CustomType.class, "red");
        assertEquals(new CustomType("red"), result);
    }

    @Test
    void givenUnregisteredType_whenConvert_thenError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ApigenSearchExecutorTypeConverters.convert(OtherType.class, "red"));
        assertEquals("No converter found for type: class org.apiaddicts.apitools.apigen.archetypecore.core.persistence.executor.ApigenSearchExecutorTypeConvertersTest$OtherType", ex.getMessage());
    }

    private static Stream<Arguments> converterProvider() {
        return Stream.of(
                Arguments.of(String.class, "a", "a"),
                Arguments.of(Integer.class, "1", 1),
                Arguments.of(Long.class, "1", 1L),
                Arguments.of(Float.class, "1.1", 1.1F),
                Arguments.of(Double.class, "1.1", 1.1D),
                Arguments.of(LocalDate.class, "2020-02-01", LocalDate.of(2020, 2, 1)),
                Arguments.of(OffsetDateTime.class, "2021-01-28T12:44:27.688Z", OffsetDateTime.of(2021, 1, 28, 12, 44, 27, 688000000, ZoneOffset.UTC)),
                Arguments.of(OffsetDateTime.class, "2021-01-28T12:44:27.688+01:00", OffsetDateTime.of(2021, 1, 28, 12, 44, 27, 688000000, ZoneOffset.ofHours(1))),
                Arguments.of(Instant.class, "2021-01-28T12:44:27.688Z", OffsetDateTime.of(2021, 1, 28, 12, 44, 27, 688000000, ZoneOffset.UTC).toInstant()),
                Arguments.of(Instant.class, "2021-01-28T12:44:27.688+01:00", OffsetDateTime.of(2021, 1, 28, 12, 44, 27, 688000000, ZoneOffset.ofHours(1)).toInstant()),
                Arguments.of(Boolean.class, "true", true),
                Arguments.of(BigInteger.class, "1", new BigInteger("1")),
                Arguments.of(BigDecimal.class, "1.1", new BigDecimal("1.1"))
        );
    }

    @Data
    @AllArgsConstructor
    private static class CustomType implements Comparable<CustomType> {
        private String value;

        @Override
        public int compareTo(CustomType o) {
            return value.compareTo(o.value);
        }
    }

    @Data
    @AllArgsConstructor
    private static class OtherType implements Comparable<OtherType> {
        private String value;

        @Override
        public int compareTo(OtherType o) {
            return value.compareTo(o.value);
        }
    }
}