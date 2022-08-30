package org.apiaddicts.apitools.apigen.archetypecore.autoconfigure;

import lombok.Data;
import org.apiaddicts.apitools.apigen.archetypecore.core.errors.ApigenError;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

@Data
@ConfigurationProperties(prefix = "apigen")
public class ApigenProperties {

	private static final int DEFAULT_EXPAND_LEVEL = 1;

	private String traceHeader = "x-trace-id";
	private Map<String, ApigenError> errors;
	private Api api = new Api();
	private StandardResponse standardResponse = new StandardResponse();

	@Data
	public static class Api {

		private Expand expand = new Expand();
		private Map<String, PathConfig> paths = new HashMap<>();

		@Data
		public static class Expand {
			private Integer level = DEFAULT_EXPAND_LEVEL;
		}

		@Data
		public static class PathConfig {

			private ExpandConfig expand;

			@Data
			public static class ExpandConfig {
				private Integer level;
				private Set<String> allowed;
				private Set<String> excluded;
			}
		}
	}

	@Data
	public static class StandardResponse {
		private List<String> operations = new ArrayList<>();
	}
}
