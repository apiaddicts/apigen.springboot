package org.apiaddicts.apitools.apigen.generatorrest.core.web;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.result.ApiError;
import org.apiaddicts.apitools.apigen.generatorcore.exceptions.DefinitionException;
import org.apiaddicts.apitools.apigen.generatorcore.exceptions.ExtractorException;
import org.apiaddicts.apitools.apigen.generatorcore.exceptions.InvalidValuesException;
import org.apiaddicts.apitools.apigen.generatorrest.core.exceptions.GeneratorRestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apiaddicts.apitools.apigen.generatorcore.exceptions.GeneratorErrors.CONFIGURATION_NOT_VALID;
import static org.apiaddicts.apitools.apigen.generatorcore.exceptions.GeneratorErrors.EXTRACTOR_ERROR;
import static org.apiaddicts.apitools.apigen.generatorrest.core.exceptions.GeneratorRestErrors.UNEXPECTED_ERROR;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(GeneratorRestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handle(GeneratorRestException e) {
        List<ApiError> errors = e.getErrors().stream()
                .map(ec -> new ApiError(ec.code, ec.message, null))
                .collect(Collectors.toList());
        return new ApiResponse().withResultErrors(errors);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExtractorException.class)
    public ApiResponse handle(ExtractorException e) {
        List<ApiError> errors = e.getErrors().stream()
                .map(ec -> new ApiError(ec.getCode(), ec.getMessage(), ec.getReference()))
                .collect(Collectors.toList());
        return new ApiResponse().withResultErrors(errors);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DefinitionException.class)
    public ApiResponse handle(DefinitionException e) {
        ApiError error = new ApiError(EXTRACTOR_ERROR.code, EXTRACTOR_ERROR.message, null);
        return new ApiResponse().withResultErrors(errors(error));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidValuesException.class)
    public ApiResponse handle(InvalidValuesException e) {
        List<ApiError> errors = e.getViolations().stream()
        .map(v -> new ApiError(CONFIGURATION_NOT_VALID.code, CONFIGURATION_NOT_VALID.message, v.getPropertyPath().toString()))
        .collect(Collectors.toList());
        return new ApiResponse().withResultErrors(errors);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handle(Exception e) {
        log.error("Unexpected error", e);
        ApiError error = new ApiError(UNEXPECTED_ERROR.code, UNEXPECTED_ERROR.message, null);
        return new ApiResponse().withResultErrors(errors(error));
    }

    private List<ApiError> errors(ApiError... apiErrors) {
        return Arrays.asList(apiErrors);
    }
}
