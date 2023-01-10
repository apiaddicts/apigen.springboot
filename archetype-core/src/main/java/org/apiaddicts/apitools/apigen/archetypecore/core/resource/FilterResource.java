package org.apiaddicts.apitools.apigen.archetypecore.core.resource;

import lombok.Data;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
public class FilterResource {
	@NotNull
	@Valid
	Filter filter;
}
