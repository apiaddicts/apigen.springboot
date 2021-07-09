package org.apiaddicts.apitools.apigen.archetypecore.core.responses.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class ApiListResponseContent<T> {
	@JsonIgnore
	protected List<T> content;

	public ApiListResponseContent() {
	}

	public ApiListResponseContent(List<T> content) {
		this.content = content;
	}
}
