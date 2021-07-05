package org.apiaddicts.apitools.apigen.archetypecore.core.responses;

public class ApiResponseObjectMother {

    private ApiResponseObjectMother() {
        // Intentional blank
    }

    public static ApiResponse createApiResponseOnlyWithPaginationWithTotal(int init, int limit) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.withMetadataPagination(init, limit, 100L);
        return apiResponse;
    }

    public static ApiResponse createApiResponseOnlyWithPaginationWithoutTotal(int init, int limit) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.withMetadataPagination(init, limit, null);
        return apiResponse;
    }

    public static ApiResponse createEmptyApiResponse() {
        return new ApiResponse();
    }
}
