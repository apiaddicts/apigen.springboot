package net.cloudappi.apigen.archetypecore.core.errors;

import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.archetypecore.autoconfigure.ApigenProperties;
import net.cloudappi.apigen.archetypecore.core.responses.result.ApiError;

import java.text.MessageFormat;
import java.util.*;

@Slf4j
public class DefaultApigenErrorManager implements ApigenErrorManager {

	private Map<String, ApigenError> errorTemplates = new HashMap<>();

	public DefaultApigenErrorManager(ApigenProperties properties) {
		for (DefaultApigenError defaultErrorCodes : DefaultApigenError.values()) {
			registerErrorTemplate(defaultErrorCodes.name(), defaultErrorCodes.getDefaultCode(), defaultErrorCodes.getDefaultMessage());
		}
		if (properties.getErrors() != null) {
			properties.getErrors().forEach(this::registerErrorTemplate);
		}
	}

	public ApigenError registerErrorTemplate(String key, Integer code, String errorTemplate) {
		ApigenError errorCode = new ApigenError(code, errorTemplate);
		return registerErrorTemplate(key, errorCode);
	}

	public ApigenError registerErrorTemplate(String key, ApigenError errorCode) {
		return errorTemplates.put(key, errorCode);
	}

	public ApiError getError(DefaultApigenError key) {
		return getError(key.name());
	}

	public ApiError getError(String key) {
		return getError(key, null);
	}

	public ApiError getError(DefaultApigenError key, String element, Object... otherElements) {
		return getError(key.name(), element, otherElements);
	}

	public ApiError getError(String key, String element, Object... otherElements) {
		ApigenError errorCode = errorTemplates.get(key);
		if (errorCode == null) {
			log.error("Error code not configured for {}, using unexpected error message", key);
			errorCode = errorTemplates.get(DefaultApigenError.UNEXPECTED_ERROR.name());
		}
		List<Object> params = new ArrayList<>();
		params.add(element);
		params.addAll(Arrays.asList(otherElements));
		String message = MessageFormat.format(errorCode.getMessageTemplate(), params.toArray());
		return new ApiError(errorCode.getCode(), message, element);
	}
}
