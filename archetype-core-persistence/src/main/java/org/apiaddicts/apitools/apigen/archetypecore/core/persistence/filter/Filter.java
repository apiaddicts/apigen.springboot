package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class Filter {
	@NotNull
	private FilterOperation operation;
	@Valid
	@NotNull
	@NotEmpty
	private List<Value> values;
}
