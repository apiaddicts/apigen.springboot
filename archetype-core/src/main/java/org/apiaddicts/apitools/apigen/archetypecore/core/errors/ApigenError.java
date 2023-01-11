package org.apiaddicts.apitools.apigen.archetypecore.core.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApigenError {
	@NotNull
	private Integer code;
	@NotNull
	private String messageTemplate;
}
