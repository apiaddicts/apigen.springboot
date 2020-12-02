package net.cloudappi.apigen.archetypecore.core.errors;

import net.cloudappi.apigen.archetypecore.core.responses.result.ApiError;

public interface ApigenErrorManager {
	ApiError getError(DefaultApigenError key);

	ApiError getError(String key);

	ApiError getError(DefaultApigenError key, String element, Object... otherElements);

	ApiError getError(String key, String element, Object... otherElements);
}
