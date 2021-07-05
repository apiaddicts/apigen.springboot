package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
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

	@JsonIgnore
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
