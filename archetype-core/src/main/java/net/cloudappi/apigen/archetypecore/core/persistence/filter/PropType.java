package net.cloudappi.apigen.archetypecore.core.persistence.filter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public enum PropType {
	STRING("STRING") {
		@Override
		public String convert(String value) {
			return value;
		}
	},
	INTEGER("INTEGER") {
		@Override
		public Long convert(String value) {
			if (value == null) return null;
			return Long.parseLong(value);
		}
	},
	FLOAT("FLOAT") {
		@Override
		public Double convert(String value) {
			if (value == null) return null;
			return Double.parseDouble(value);
		}
	},
	DATE("DATE") {
		@Override
		public LocalDate convert(String value) {
			if (value == null) return null;
			return LocalDate.parse(value);
		}
	},
	DATETIME("DATETIME") {
		@Override
		public OffsetDateTime convert(String value) {
			if (value == null) return null;
			return OffsetDateTime.parse(value);
		}
	},
	BOOLEAN("BOOLEAN") {
		@Override
		public Boolean convert(String value) {
			if (value == null) return null;
			return Boolean.parseBoolean(value);
		}
	};

	private final String value;

	PropType(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public abstract <T extends Comparable> T convert(String value);

	public <T extends Comparable> List<T> convert(List<String> values) {
		List<T> convertedValues = new ArrayList<>();
		for (String v : values) {
			T cast = this.convert(v);
			convertedValues.add(cast);
		}
		return convertedValues;
	}

}
