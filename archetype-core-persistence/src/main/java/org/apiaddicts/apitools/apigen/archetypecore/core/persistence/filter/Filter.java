package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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