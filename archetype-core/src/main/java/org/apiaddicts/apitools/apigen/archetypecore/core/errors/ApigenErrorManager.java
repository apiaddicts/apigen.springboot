package org.apiaddicts.apitools.apigen.archetypecore.core.errors;

import org.apiaddicts.apitools.apigen.archetypecore.core.responses.result.ApiError;

public interface ApigenErrorManager {
	ApiError getError(DefaultApigenError key);

	ApiError getError(String key);

	ApiError getError(DefaultApigenError key, String element, Object... otherElements);

	ApiError getError(String key, String element, Object... otherElements);
}
