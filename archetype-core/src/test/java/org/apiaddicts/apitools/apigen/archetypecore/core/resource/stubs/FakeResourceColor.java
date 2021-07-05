package org.apiaddicts.apitools.apigen.archetypecore.core.resource.stubs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apiaddicts.apitools.apigen.archetypecore.core.resource.ApigenEntityOutResource;

import java.util.Set;

@Data
@ApigenEntityOutResource
public class FakeResourceColor {
    @JsonProperty("json_id")
    private Long id;

    @JsonProperty("json_name")
    private String name;

    @JsonProperty("json_forms")
    private Set<FakeResourceForm> forms;

    @JsonProperty("json_form")
    private FakeResourceForm form;
}
