package org.apiaddicts.apitools.apigen.archetypecore.interceptors.controller;

import org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponseObjectMother;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagination")
public class FakePaginationController {

    @GetMapping(value = "/with-total")
    public @ResponseBody ApiResponse getPaginationTotal(@RequestParam(value = "$init", required = true, defaultValue = "0") Integer init,
                                                        @RequestParam(value = "$limit", required = true, defaultValue = "25") Integer limit) {
        return ApiResponseObjectMother.createApiResponseOnlyWithPaginationWithTotal(init, limit);
    }

    @GetMapping(value = "/without-total")
    public @ResponseBody ApiResponse getPagination(@RequestParam(value = "$init", required = true, defaultValue = "0") Integer init,
                                                   @RequestParam(value = "$limit", required = true, defaultValue = "25") Integer limit) {
        return ApiResponseObjectMother.createApiResponseOnlyWithPaginationWithoutTotal(init, limit);
    }

    @GetMapping(value = "/without-pagination")
    public @ResponseBody ApiResponse get() {
        return ApiResponseObjectMother.createEmptyApiResponse();
    }

    @GetMapping(value = "")
    public @ResponseBody ApiResponse getDefault(@RequestParam(value = "$init", required = true, defaultValue = "0") Integer init,
                                                @RequestParam(value = "$limit", required = true, defaultValue = "25") Integer limit) {
        return ApiResponseObjectMother.createApiResponseOnlyWithPaginationWithoutTotal(init, limit);
    }

    @GetMapping(value = "/no-response")
    public @ResponseBody String getNoApiResponse(@RequestParam(value = "$init", required = true, defaultValue = "0") Integer init,
                                                 @RequestParam(value = "$limit", required = true, defaultValue = "25") Integer limit) {
        return "hola";
    }
}
