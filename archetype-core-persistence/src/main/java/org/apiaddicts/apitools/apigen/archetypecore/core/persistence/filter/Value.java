package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Data
public class Value {
	private String property;
	private String value; // NOSONAR
	private List<String> values;
	@Valid
	private Filter filter;

	@AssertTrue
	public boolean isValid() {
		return isFilter() ^ isExpression(); // ^ -> XOR
	}

	private boolean isFilter() {
		return nonNull(filter);
	}

	private boolean isExpression() {
		return nonNull(property) && hasOnlyOneValue();
	}

	private boolean hasOnlyOneValue() {
		return ( nonNull(value) && isNull(values) ) || ( isNull(value) && nonNull(values) );
	}
}
