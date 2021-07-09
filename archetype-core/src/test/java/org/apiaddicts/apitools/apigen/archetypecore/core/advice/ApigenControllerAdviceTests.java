package org.apiaddicts.apitools.apigen.archetypecore.core.advice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ApigenControllerAdviceTests {

    private static final LocalDate NOW = LocalDate.now();
    private static final String TODAY = NOW.toString();
    private static final String YESTERDAY = NOW.minusDays(1L).toString();
    private static final String TOMORROW = NOW.plusDays(1L).toString();

    @Autowired
    private MockMvc mvc;

    @Test
    void givenEndpointWithPathVariable_whenPathVariableHasWrongType_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/path-variable/error")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1109)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Error parsing path variable 'id'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("id")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenMissingRequestParameter_whenInitDoesExist_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/missing-request-parameter?$limit=10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1110)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Query parameter '$init' is required")))
                .andExpect(jsonPath("$.result.errors[0].element", is("$init")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenConstrainViolation_whenValueInitIsIncorrect_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/constrain-violation?$init=-1&$limit=10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1003)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property '$init' must have a value greater or equal to '0'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("$init")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationNotNull_whenValueIsNotNull_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-not-null")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1000)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'name_property' must be not null")))
                .andExpect(jsonPath("$.result.errors[0].element", is("name_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationNull_whenValueNull_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-null")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name_property\": \"aaa\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1015)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'name_property' must be null")))
                .andExpect(jsonPath("$.result.errors[0].element", is("name_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationEmail_whenValueIsErrorNotEmail_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email_property\": \"aaa\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1009)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'email_property' must be an email")))
                .andExpect(jsonPath("$.result.errors[0].element", is("email_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationNotEmpty_whenValueIsNotEmpty_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-not-empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name_property\": \"\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1007)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'name_property' must not be empty")))
                .andExpect(jsonPath("$.result.errors[0].element", is("name_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationNotBlank_whenValueIsNotBlank_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-not-blank")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name_property\": \" \"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1008)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'name_property' must not be blank")))
                .andExpect(jsonPath("$.result.errors[0].element", is("name_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationPositive_whenValueIsNotPositive_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-positive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": 0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1002)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must have a value greater than '0'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationPositiveOrZero_whenValueIsNotPositiveOrZero_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-positive-or-zero")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": -1}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1003)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must have a value greater or equal to '0'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void giveValidationNegative_whenValueIsNotNegative_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-negative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": 0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1004)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must have a value less than '0'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationNegativeOrZero_whenValueIsNotNegativeOrZero_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-negative-or-zero")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": 1}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1005)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must have a value less or equal to '0'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationPast_whenValueIsNotPast_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-past")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"date_property\": \""+ TODAY +"\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1010)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'date_property' must be a past date")))
                .andExpect(jsonPath("$.result.errors[0].element", is("date_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationPastOrPresent_whenValueIsNotPastOrPresent_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-past-or-present")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"date_property\": \""+ TOMORROW +"\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1011)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'date_property' must be a past or present date")))
                .andExpect(jsonPath("$.result.errors[0].element", is("date_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationFuture_whenValueIsNotFuture_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-future")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"date_property\": \""+ TODAY +"\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1012)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'date_property' must be a future")))
                .andExpect(jsonPath("$.result.errors[0].element", is("date_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationFutureOrPresent_whenValueIsNotFutureOrPresent_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-future-or-present")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"date_property\": \""+ YESTERDAY +"\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1013)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'date_property' must be a future or present")))
                .andExpect(jsonPath("$.result.errors[0].element", is("date_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationMin_whenValueIsNotMin_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": 9}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1003)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must have a value greater or equal to '10'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationDecimalMinWithFalse_whenValueIsNotDecimalMin_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-decimal-min-with-false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": 10.25}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1002)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must have a value greater than '10.25'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationDecimalMinWithTrue_whenValueIsNotDecimalMin_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-decimal-min-with-true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": 10.24}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1003)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must have a value greater or equal to '10.25'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationMax_whenValueIsNotMax_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-max")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": 21}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1005)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must have a value less or equal to '20'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationDecimalMaxWithFalse_whenValueIsNotDecimalMax_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-decimal-max-with-false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": 20.25}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1004)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must have a value less than '20.25'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationDecimalMaxWithTrue_whenValueIsNotDecimalMax_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-decimal-max-with-true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": 20.26}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1005)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must have a value less or equal to '20.25'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationSize_whenValueIsNotMinimumSize_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-size")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name_property\": \"pepe\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1001)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'name_property' must have a length between '5' and '10'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("name_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationSize_whenValueIsNotMaximumSize_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-size")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name_property\": \"Jose Francisco\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1001)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'name_property' must have a length between '5' and '10'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("name_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationDigits_whenValueIsSuperiorIntegerDigits_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-digits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": 10.99}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1014)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must be a decimal with '1' integer digits and '2' decimal digits")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationDigits_whenValueIsSuperiorDecimalDigits_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-digits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number_property\": 1.989}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1014)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'number_property' must be a decimal with '1' integer digits and '2' decimal digits")))
                .andExpect(jsonPath("$.result.errors[0].element", is("number_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationDigits_whenValueIsNotPattern_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-pattern")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name_property\": \"pepe\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1006)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'name_property' must follow the regex '[A-Z]+'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("name_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationNotSupported_whenValueIsFalse_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isName_property\": false}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1099)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'isName_property' must be valid")))
                .andExpect(jsonPath("$.result.errors[0].element", is("isName_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenEntityNotFound_whenEntityNotFound_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/entity-not-found")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1106)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Element with id 'id' of type 'String' not found")))
                .andExpect(jsonPath("$.result.errors[0].element", is("id")))
                .andReturn().getResponse();

        // then
        assertEquals(404, response.getStatus());
    }

    @Test
    void givenEntityIdAlreadyExists_whenEntityIdAlreadyExists_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/entity-id-already-exists")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1113)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Element with id 'id' of type 'String' already exists")))
                .andExpect(jsonPath("$.result.errors[0].element", is("id")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenEntityNotFound_whenRelatedEntityNotFound_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/related-entity-not-found")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1107)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Related element with id 'null' of type 'String' not found")))
                .andExpect(jsonPath("$.result.errors[0].element", is("null")))
                .andExpect(jsonPath("$.result.errors[1].code", is(1107)))
                .andExpect(jsonPath("$.result.errors[1].message", is("Related element with id '1' of type 'String' not found")))
                .andExpect(jsonPath("$.result.errors[1].element", is("1")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenMethodNotSupported_whenGetNotSupported_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/method-not-supported")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1200)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Method 'GET' not implemented")))
                .andExpect(jsonPath("$.result.errors[0].element", is("GET")))
                .andReturn().getResponse();

        // then
        assertEquals(501, response.getStatus());
    }

    @Test
    void givenHandlerNotFound_whenPathNotFound_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/not-defined-path")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1201)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Path '/not-defined-path' not implemented")))
                .andExpect(jsonPath("$.result.errors[0].element", is("/not-defined-path")))
                .andReturn().getResponse();

        // then
        assertEquals(404, response.getStatus());
    }

    @Test
    void givenHandler_whenMethodNotImplemented_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/method-not-implemented")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1200)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Method 'GET /method-not-implemented' not implemented")))
                .andExpect(jsonPath("$.result.errors[0].element", is("GET /method-not-implemented")))
                .andReturn().getResponse();

        // then
        assertEquals(501, response.getStatus());
    }

    @Test
    void givenException_whenNotKnowException_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/exception")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1300)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Unexpected error")))
                .andExpect(jsonPath("$.result.errors[0].element").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(500, response.getStatus());
    }

    @Test
    void givenAcceptMediaType_whenBodyIsNotCorrect_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/accept-media-type")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1203)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Accept format not supported, supported formats: '[text/xml]'")))
                .andExpect(jsonPath("$.result.errors[0].element").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenContentMediaType_whenBodyIsNotCorrect_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/content-media-type")
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(jsonPath("$.result.errors[0].code", is(1205)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Content-type format not supported: 'application/xml;charset=UTF-8'")))
                .andExpect(jsonPath("$.result.errors[0].element").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenCustomApigenException_whenCustomApigenException_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/custom-apigen-exception")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(9999)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Parameter not found 'color'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("color")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenIncorrectEnum_whenBody_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/message-not-readable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"levelTemperature_property\": \"MEDIUM\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1204)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Unsupported value 'MEDIUM', accepted values: 'HIGH, LOW'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("MEDIUM")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenEmptyBody_whenBodyIsNull_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1202)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Request body required")))
                .andExpect(jsonPath("$.result.errors[0].element").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenValidationDate_whenDateIsIncorrect_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/error-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"date_property\": \"ErrorDate\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1108)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Error parsing ISO date 'ErrorDate'")))
                .andExpect(jsonPath("$.result.errors[0].element", is("ErrorDate")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenTransalatorsError_whenTranslatorDoError_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/translator-errors")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1101)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Invalid property 'selected' in $select")))
                .andExpect(jsonPath("$.result.errors[0].element", is("selected")))
                .andExpect(jsonPath("$.result.errors[1].code", is(1102)))
                .andExpect(jsonPath("$.result.errors[1].message", is("Invalid property 'exclude' in $exclude")))
                .andExpect(jsonPath("$.result.errors[1].element", is("exclude")))
                .andExpect(jsonPath("$.result.errors[2].code", is(1103)))
                .andExpect(jsonPath("$.result.errors[2].message", is("Invalid property 'expand' in $expand")))
                .andExpect(jsonPath("$.result.errors[2].element", is("expand")))
                .andExpect(jsonPath("$.result.errors[3].code", is(1100)))
                .andExpect(jsonPath("$.result.errors[3].message", is("Invalid property 'filter' in $filter")))
                .andExpect(jsonPath("$.result.errors[3].element", is("filter")))
                .andExpect(jsonPath("$.result.errors[4].code", is(1104)))
                .andExpect(jsonPath("$.result.errors[4].message", is("Invalid property 'orderby' in $orderby")))
                .andExpect(jsonPath("$.result.errors[4].element", is("orderby")))
                .andExpect(jsonPath("$.result.errors[5].code", is(1105)))
                .andExpect(jsonPath("$.result.errors[5].message", is("Invalid property 'OrderByToManyPath' in $orderby")))
                .andExpect(jsonPath("$.result.errors[5].element", is("OrderByToManyPath")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenListValidationNotNull_whenListValueIsNotNull_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/advice/nested-error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nested_property\": [{}]}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1000)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Property 'nested_property[0].name_property' must be not null")))
               // .andExpect(jsonPath("$.result.errors[0].element", is("name_property")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    void givenInvalidRegex_whenEvaluated_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/advice/regex-error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.errors[0].code", is(1111)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Invalid regex expression '*'")))
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }
}
