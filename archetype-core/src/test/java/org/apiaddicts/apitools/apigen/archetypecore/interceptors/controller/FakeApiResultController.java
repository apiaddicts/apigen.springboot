package org.apiaddicts.apitools.apigen.archetypecore.interceptors.controller;

import org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponseObjectMother;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiresult")
public class FakeApiResultController {

    @GetMapping(value = "/with-200")
    public @ResponseBody ApiResponse getNewApiResultWith200() {
        return ApiResponseObjectMother.createEmptyApiResponse();
    }

    @GetMapping(value = "/with-301")
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    public @ResponseBody ApiResponse getNewApiResultWith301() {
        return ApiResponseObjectMother.createEmptyApiResponse();
    }

    @GetMapping(value = "/with-404")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ApiResponse getNewApiResultWith404() {
        return ApiResponseObjectMother.createEmptyApiResponse();
    }

    @GetMapping(value = "/with-503")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public @ResponseBody ApiResponse getNewApiResultWith503() {
        return ApiResponseObjectMother.createEmptyApiResponse();
    }

    @GetMapping(value = "/null-apiresult")
    public @ResponseBody ApiResponse getNullApiResult() {
        return null;
    }

    @GetMapping(value = "/missing-parameter-exception")
    public @ResponseBody ApiResponse getMissingParameterException() throws MissingServletRequestParameterException {
        throw new MissingServletRequestParameterException("$init", "Integer");
    }

    @GetMapping(value = "/entity-notfound-exception-apiresult")
    public @ResponseBody ApiResponse getEntityNotFoundException() {
        throw new EntityNotFoundException("id", String.class);
    }
}
