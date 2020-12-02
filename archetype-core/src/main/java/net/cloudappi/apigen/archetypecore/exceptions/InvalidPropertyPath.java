package net.cloudappi.apigen.archetypecore.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InvalidPropertyPath extends RuntimeException {
	private List<String> invalidSelectPath = new ArrayList<>();
	private List<String> invalidExcludePath = new ArrayList<>();
	private List<String> invalidExpandPath = new ArrayList<>();
	private List<String> invalidOrderByPath = new ArrayList<>();
	private List<String> invalidOrderByToManyPath = new ArrayList<>();
	private List<String> invalidFilterPath = new ArrayList<>();

	public boolean errorsDetected() {
		return !invalidSelectPath.isEmpty()
				|| !invalidExcludePath.isEmpty()
				|| !invalidExpandPath.isEmpty()
				|| !invalidOrderByPath.isEmpty()
				|| !invalidOrderByToManyPath.isEmpty()
				|| !invalidFilterPath.isEmpty();
	}
}
