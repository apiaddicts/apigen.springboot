package net.cloudappi.apigen.archetypecore.core.resource.stubs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.cloudappi.apigen.archetypecore.core.resource.ApigenEntityOutResource;

@Data
@ApigenEntityOutResource
public class FakeResourceForm {
    @JsonProperty("property_id")
    private Long id;

    @JsonProperty("property_name")
    private String name;
}
