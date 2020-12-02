package net.cloudappi.apigen.archetypecore.core.responses.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private Integer code;
    private String message;
    private String element;
}
