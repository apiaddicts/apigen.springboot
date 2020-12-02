package net.cloudappi.apigen.archetypecore.core.persistence.filter;

public enum FilterOperation {
	AND("AND", false),
	OR("OR", false),
	GT("GT", true),
	LT("LT", true),
	GTEQ("GTEQ", true),
	LTEQ("LTEQ", true),
	EQ("EQ", true),
	NEQ("NEQ", true),
	IN("IN", false),
	BETWEEN("BETWEEN", false),
	SUBSTRING("SUBSTRING", true),
	LIKE("LIKE", true),
	ILIKE("ILIKE", true),
	NLIKE("NLIKE", true),
	REGEXP("REGEXP", true);

	private final String operation;
	private final boolean singleValue;

	FilterOperation(final String operation, boolean singleValue) {
		this.operation = operation;
		this.singleValue = singleValue;
	}

	public boolean isSingleValue() {
		return singleValue;
	}

	@Override
	public String toString() {
		return this.operation;
	}
}
