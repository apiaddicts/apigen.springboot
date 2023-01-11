package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class Filter {

	public Filter(){}

	public Filter(FilterOperation operation, List<Value> values) {
		this.operation = operation;
		this.values = values;
	}

	@NotNull
	private FilterOperation operation;
	@Valid
	@NotNull
	@NotEmpty
	private List<Value> values;
}
