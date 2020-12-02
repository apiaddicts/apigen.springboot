package net.cloudappi.apigen.archetypecore.core.responses.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiResult {

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("http_code")
    private Integer httpCode;

    @JsonProperty("info")
    private String info;

    @JsonProperty("errors")
    private List<ApiError> errors;

    @JsonProperty("trace_id")
    private String traceId;

    @JsonProperty("updated_elements")
    private Integer updatedElements;
}
