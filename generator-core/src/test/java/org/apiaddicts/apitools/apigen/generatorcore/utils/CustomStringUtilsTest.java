package org.apiaddicts.apitools.apigen.generatorcore.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomStringUtilsTest {

    @Test
    void givenSnakeCase_whenConvertsSnakeCaseToCamelCase_thenSuccess() {
        assertEquals("snakeCase", CustomStringUtils.snakeCaseToCamelCase("snake_case"));
    }

    @Test
    void givenSnakeCaseLowerAndUpper_whenConvertsSnakeCaseToCamelCase_thenSuccess() {
        assertEquals("snakeCase", CustomStringUtils.snakeCaseToCamelCase("sNAkE_cAsE"));
    }

    @Test
    void givenSnakeCaseSimple_whenConvertsSnakeCaseToCamelCase_thenSuccess() {
        assertEquals("snake", CustomStringUtils.snakeCaseToCamelCase("snake"));
    }

    @Test
    void givenEmpty_whenConvertsSnakeCaseToCamelCase_thenSuccess() {
        assertEquals("", CustomStringUtils.snakeCaseToCamelCase(""));
    }

    @Test
    void givenNull_whenConvertsSnakeCaseToCamelCase_thenSuccess() {
        assertEquals(null, CustomStringUtils.snakeCaseToCamelCase(null));
    }

    @Test
    void givenSnakeCase_whenConvertsKebabCaseToCamelCase_thenSuccess() {
        assertEquals("kebabCase", CustomStringUtils.kebabCaseToCamelCase("kebab-case"));
    }

    @Test
    void givenSnakeCaseLowerAndUpper_whenConvertsKebabCaseToCamelCase_thenSuccess() {
        assertEquals("kebabCase", CustomStringUtils.kebabCaseToCamelCase("kEbAb-cAsE"));
    }

    @Test
    void givenSnakeCaseSimple_whenConvertsKebabCaseToCamelCase_thenSuccess() {
        assertEquals("kebab", CustomStringUtils.kebabCaseToCamelCase("kebab"));
    }

    @Test
    void givenEmpty_whenConvertsKebabCaseToCamelCase_thenSuccess() {
        assertEquals("", CustomStringUtils.kebabCaseToCamelCase(""));
    }

    @Test
    void givenNull_whenConvertsKebabCaseToCamelCase_thenSuccess() {
        assertEquals(null, CustomStringUtils.kebabCaseToCamelCase(null));
    }

    @Test
    void givenCamelCase_whenConvertsCamelCaseToSnakeCase_thenSuccess() {
        assertEquals("camel_case", CustomStringUtils.camelCaseToSnakeCase("camelCase"));
    }

    @Test
    void givenCamelCaseWithMoreThan1UpperCase_whenConvertsCamelCaseToSnakeCase_thenSuccess() {
        assertEquals("ca_mel_ca_se", CustomStringUtils.camelCaseToSnakeCase("caMelCaSe"));
    }

    @Test
    void givenCamelCaseWithFirstUpperCase_whenConvertsCamelCaseToSnakeCase_thenSuccess() {
        assertEquals("camel_case", CustomStringUtils.camelCaseToSnakeCase("CamelCase"));
    }

    @Test
    void givenCamelCaseSimple_whenConvertsCamelCaseToSnakeCase_thenSuccess() {
        assertEquals("camel", CustomStringUtils.camelCaseToSnakeCase("camel"));
    }

    @Test
    void givenEmpty_whenConvertsCamelCaseToSnakeCase_thenSuccess() {
        assertEquals("", CustomStringUtils.camelCaseToSnakeCase(""));
    }

    @Test
    void givenNull_whenConvertsCamelCaseToSnakeCase_thenSuccess() {
        assertEquals(null, CustomStringUtils.camelCaseToSnakeCase(null));
    }

}