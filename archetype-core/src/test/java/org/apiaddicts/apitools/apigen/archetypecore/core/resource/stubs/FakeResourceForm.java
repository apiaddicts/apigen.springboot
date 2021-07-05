package org.apiaddicts.apitools.apigen.archetypecore.core.resource.stubs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apiaddicts.apitools.apigen.archetypecore.core.resource.ApigenEntityOutResource;

@Data
@ApigenEntityOutResource
public class FakeResourceForm {
    @JsonProperty("property_id")
    private Long id;

    @JsonProperty("property_name")
    private String name;
}
