package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Data
public class Value {

	public Value(){}

	public Value(Filter filter) {
		this.filter = filter;
	}

	public Value(Filter filter, String value) {
		this.filter = filter;
		this.value = value;
	}

	public Value(Filter filter, List<String> values) {
		this.filter = filter;
		this.values = values;
	}

	public Value(String property, List<String> values) {
		this.property = property;
		this.values = values;
	}

	public Value(String property, String value) {
		this.property = property;
		this.value = value;
	}

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
