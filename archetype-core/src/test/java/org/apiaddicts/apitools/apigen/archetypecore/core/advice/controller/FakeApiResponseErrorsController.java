package org.apiaddicts.apitools.apigen.archetypecore.core.advice.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.*;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Validated
@RestController
@RequestMapping("/advice")
public class FakeApiResponseErrorsController {

    @GetMapping("/path-variable/{id}")
    public void pathVariable(@PathVariable("id") Integer identifier) {
    }

    @Data
    public static class NotNullBody{
        @NotNull
        @JsonProperty("name_property")
        private String name;
    }

    @PostMapping(value = "/error-not-null")
    public @ResponseBody
    ApiResponse postNotNullException(@Valid @RequestBody NotNullBody body) {
        throw new RuntimeException();
    }

    @Data
    public static class NullBody{
        @Null
        @JsonProperty("name_property")
        private String name;
    }

    @PostMapping(value = "/error-null")
    public @ResponseBody
    ApiResponse postNullException(@Valid @RequestBody NullBody body) {
        throw new RuntimeException();
    }

    @Data
    public static class Email{
        @javax.validation.constraints.Email
        @JsonProperty("email_property")
        private String email;
    }

    @PostMapping(value = "/error-email")
    public @ResponseBody
    ApiResponse postEmailException(@Valid @RequestBody Email email) {
        throw new RuntimeException();
    }

    @Data
    public static class NotEmpty{
        @javax.validation.constraints.NotEmpty
        @JsonProperty("name_property")
        private String name;
    }

    @PostMapping(value = "/error-not-empty")
    public @ResponseBody
    ApiResponse postNotEmptyException(@Valid @RequestBody NotEmpty body) {
        throw new RuntimeException();
    }

    @Data
    public static class NotBlank{
        @javax.validation.constraints.NotBlank
        @JsonProperty("name_property")
        private String name;
    }

    @PostMapping(value = "/error-not-blank")
    public @ResponseBody
    ApiResponse postNotBlankException(@Valid @RequestBody NotBlank body) {
        throw new RuntimeException();
    }

    @Data
    public static class Positive{
        @javax.validation.constraints.Positive
        @JsonProperty("number_property")
        private Integer number;
    }

    @PostMapping(value = "/error-positive")
    public @ResponseBody
    ApiResponse postPositiveException(@Valid @RequestBody Positive body) {
        throw new RuntimeException();
    }

    @Data
    public static class PositiveOrZero{
        @javax.validation.constraints.PositiveOrZero
        @JsonProperty("number_property")
        private Integer number;
    }

    @PostMapping(value = "/error-positive-or-zero")
    public @ResponseBody
    ApiResponse postPositiveOrZeroException(@Valid @RequestBody PositiveOrZero body) {
        throw new RuntimeException();
    }

    @Data
    public static class Negative{
        @javax.validation.constraints.Negative
        @JsonProperty("number_property")
        private Integer number;
    }

    @PostMapping(value = "/error-negative")
    public @ResponseBody
    ApiResponse postNegativeException(@Valid @RequestBody Negative body) {
        throw new RuntimeException();
    }

    @Data
    public static class NegativeOrZero{
        @javax.validation.constraints.NegativeOrZero
        @JsonProperty("number_property")
        private Integer number;
    }

    @PostMapping(value = "/error-negative-or-zero")
    public @ResponseBody
    ApiResponse postNegativeOrZeroException(@Valid @RequestBody NegativeOrZero body) {
        throw new RuntimeException();
    }

    @Data
    public static class Past{
        @javax.validation.constraints.Past
        @JsonProperty("date_property")
        private LocalDate date;
    }

    @PostMapping(value = "/error-past")
    public @ResponseBody
    ApiResponse postPastException(@Valid @RequestBody Past body) {
        throw new RuntimeException();
    }

    @Data
    public static class PastOrPresent{
        @javax.validation.constraints.PastOrPresent
        @JsonProperty("date_property")
        private LocalDate date;
    }

    @PostMapping(value = "/error-past-or-present")
    public @ResponseBody
    ApiResponse postPastOrPresentException(@Valid @RequestBody PastOrPresent body) {
        throw new RuntimeException();
    }

    @Data
    public static class Future{
        @javax.validation.constraints.Future
        @JsonProperty("date_property")
        private LocalDate date;
    }

    @PostMapping(value = "/error-future")
    public @ResponseBody
    ApiResponse postFutureException(@Valid @RequestBody Future body) {
        throw new RuntimeException();
    }

    @Data
    public static class FutureOrPresent{
        @javax.validation.constraints.FutureOrPresent
        @JsonProperty("date_property")
        private LocalDate date;
    }

    @PostMapping(value = "/error-future-or-present")
    public @ResponseBody
    ApiResponse postFutureOrPresentException(@Valid @RequestBody FutureOrPresent body) {
        throw new RuntimeException();
    }

    @Data
    public static class Min{
        @javax.validation.constraints.Min(10)
        @JsonProperty("number_property")
        private Long number;
    }

    @PostMapping(value = "/error-min")
    public @ResponseBody
    ApiResponse postMinException(@Valid @RequestBody Min body) {
        throw new RuntimeException();
    }

    @Data
    public static class DecimalMinWithFalse{
        @DecimalMin(value = "10.25", inclusive = false)
        @JsonProperty("number_property")
        private BigDecimal number;
    }

    @PostMapping(value = "/error-decimal-min-with-false")
    public @ResponseBody
    ApiResponse postMinWithFalseException(@Valid @RequestBody DecimalMinWithFalse body) {
        throw new RuntimeException();
    }

    @Data
    public static class DecimalMinWithTrue{
        @DecimalMin(value = "10.25", inclusive = true)
        @JsonProperty("number_property")
        private BigDecimal number;
    }

    @PostMapping(value = "/error-decimal-min-with-true")
    public @ResponseBody
    ApiResponse postMinWithTrueException(@Valid @RequestBody DecimalMinWithTrue body) {
        throw new RuntimeException();
    }

    @Data
    public static class Max{
        @javax.validation.constraints.Max(20)
        @JsonProperty("number_property")
        private Long number;
    }

    @PostMapping(value = "/error-max")
    public @ResponseBody
    ApiResponse postMaxException(@Valid @RequestBody Max body) {
        throw new RuntimeException();
    }

    @Data
    public static class DecimalMaxWithFalse{
        @DecimalMax(value = "20.25", inclusive = false)
        @JsonProperty("number_property")
        private BigDecimal number;
    }

    @PostMapping(value = "/error-decimal-max-with-false")
    public @ResponseBody
    ApiResponse postMaxWithFalseException(@Valid @RequestBody DecimalMaxWithFalse body) {
        throw new RuntimeException();
    }

    @Data
    public static class DecimalMaxWithTrue{
        @DecimalMax(value = "20.25", inclusive = true)
        @JsonProperty("number_property")
        private BigDecimal number;
    }

    @PostMapping(value = "/error-decimal-max-with-true")
    public @ResponseBody
    ApiResponse postMaxWithTrueException(@Valid @RequestBody DecimalMaxWithTrue body) {
        throw new RuntimeException();
    }

    @Data
    public static class Size {
        @javax.validation.constraints.Size( min = 5, max = 10)
        @JsonProperty("name_property")
        private String name;
    }

    @PostMapping(value = "/error-size")
    public @ResponseBody
    ApiResponse postSizeException(@Valid @RequestBody Size body) {
        throw new RuntimeException();
    }

    @Data
    public static class Digits {
        @javax.validation.constraints.Digits(integer=1, fraction=2)
        @JsonProperty("number_property")
        private BigDecimal number;
    }

    @PostMapping(value = "/error-digits")
    public @ResponseBody
    ApiResponse postDigitsException(@Valid @RequestBody Digits body) {
        throw new RuntimeException();
    }

    @Data
    public static class PatternBody {
        @javax.validation.constraints.Pattern(regexp = "[A-Z]+")
        @JsonProperty("name_property")
        private String name;
    }

    @PostMapping(value = "/error-pattern")
    public @ResponseBody
    ApiResponse postPatternException(@Valid @RequestBody PatternBody body) {
        throw new RuntimeException();
    }

    @Data
    public static class Error{
        @AssertTrue
        @JsonProperty("isName_property")
        private Boolean isName;
    }

    @PostMapping(value = "/error")
    public @ResponseBody
    ApiResponse postErrorException(@Valid @RequestBody Error body) {
        throw new RuntimeException();
    }

    @GetMapping(value = "/entity-not-found")
    public @ResponseBody ApiResponse getEntityNotFoundException() {
        throw new EntityNotFoundException("id", String.class);
    }

    @GetMapping(value = "/entity-id-already-exists")
    public @ResponseBody ApiResponse getEntityIdAlreadyExistsException() {
        throw new EntityIdAlreadyInUseException("id", String.class);
    }

    @GetMapping(value = "/related-entity-not-found")
    public @ResponseBody ApiResponse getRelatedEntityNotFoundException() {
        RelationalErrors errors = new RelationalErrors();
        errors.register(String.class, 1);
        errors.register(String.class, null);
        throw new RelationalErrorsException(errors);
    }

    @GetMapping(value = "/missing-request-parameter")
    public @ResponseBody ApiResponse getMissingServletRequestParameterException(@RequestParam(value = "$init", required = true) Integer init,
                                                                                @RequestParam(value = "$limit", required = true) Integer limit){
        throw new RuntimeException();
    }

    @GetMapping(value = "/constrain-violation")
    public @ResponseBody ApiResponse getConstraintViolationException(@javax.validation.constraints.PositiveOrZero @RequestParam(value = "$init", required = true) Integer init,
                                                                                @RequestParam(value = "$limit", required = true) Integer limit){
        throw new RuntimeException();
    }

    @PostMapping(value = "/method-not-supported")
    public @ResponseBody ApiResponse getHttpRequestMethodNotSupportedException(){
        throw new RuntimeException();
    }

    @GetMapping(value = "/exception")
    public @ResponseBody ApiResponse getException() {
        throw new RuntimeException();
    }

    @GetMapping(value = "/accept-media-type", produces = MediaType.TEXT_XML_VALUE)
    public @ResponseBody ApiResponse getContentMediaTypeException() {
        throw new RuntimeException();
    }

    @PostMapping(value = "/content-media-type")
    public @ResponseBody ApiResponse getAcceptMediaTypeException(@RequestBody ContentBody body) {
        throw new RuntimeException();
    }

    @Data
    public static class ContentBody {
        private String name;
    }

    @GetMapping(value = "/custom-apigen-exception")
    public @ResponseBody ApiResponse getCustomApigenException() {
        throw new CustomApigenException("CUSTOM_ERROR", "color");
    }

    @Data
    public static class Temperature {
        @JsonProperty("levelTemperature_property")
        private Level level;

        public enum Level{
            LOW,
            HIGH
        }
    }

    @PostMapping(value = "/message-not-readable")
    public @ResponseBody
    ApiResponse postMessageNotReadableException(@Valid @RequestBody Temperature body) {
        throw new RuntimeException();
    }

    @Data
    public static class ErrorDate{
        @JsonProperty("date_property")
        private LocalDate date;
    }

    @PostMapping(value = "/error-date")
    public @ResponseBody
    ApiResponse postPastException(@Valid @RequestBody ErrorDate body) {
        throw new RuntimeException();
    }

    @Data
    public static class FilterError{
        @JsonProperty("filter_property")
        private Filter filter;
    }

    @GetMapping(value = "/translator-errors")
    public @ResponseBody
    ApiResponse postTranslatorErrors() {
        InvalidPropertyPath translatorErrors = new InvalidPropertyPath();
        translatorErrors.getInvalidExpandPath().add("expand");
        translatorErrors.getInvalidFilterPath().add("filter");
        translatorErrors.getInvalidExcludePath().add("exclude");
        translatorErrors.getInvalidSelectPath().add("selected");
        translatorErrors.getInvalidOrderByPath().add("orderby");
        translatorErrors.getInvalidOrderByToManyPath().add("OrderByToManyPath");
        throw translatorErrors;
    }

    @Data
    public static class Body{
        @Valid
        @JsonProperty("nested_property")
        List<NestedBody> nested;

        @Data
        public static class NestedBody {
            @NotNull
            @JsonProperty("name_property")
            String name;
        }
    }

    @PostMapping(value = "/nested-error")
    public @ResponseBody
    ApiResponse postNestedError(@Valid @RequestBody Body body) {
        throw new RuntimeException();
    }

    @GetMapping("/regex-error")
    public void regexError() {
        Pattern regexPattern = Pattern.compile("*");
    }

    @GetMapping("/method-not-implemented")
    public void methodNotImplemented() {
        throw new NotImplementedException("GET /method-not-implemented");
    }

}
