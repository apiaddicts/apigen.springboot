package net.cloudappi.apigen.archetypecore.autoconfigure;

import lombok.Data;
import net.cloudappi.apigen.archetypecore.core.errors.ApigenError;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "apigen")
public class ApigenProperties {

	private static final int DEFAULT_EXPAND_LEVEL = 1;

	private String traceHeader = "x-trace-id";
	private Map<String, ApigenError> errors;
	private Api api = new Api();

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
}
