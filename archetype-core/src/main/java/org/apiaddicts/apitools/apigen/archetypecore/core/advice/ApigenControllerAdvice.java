package org.apiaddicts.apitools.apigen.archetypecore.core.advice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.archetypecore.core.errors.ApigenErrorManager;
import org.apiaddicts.apitools.apigen.archetypecore.core.errors.DefaultApigenError;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.result.ApiError;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.*;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@ControllerAdvice
public class ApigenControllerAdvice {

	private final ApigenErrorManager errorManager;

	public ApigenControllerAdvice(ApigenErrorManager errorManager) {
		this.errorManager = errorManager;
	}

	@ResponseBody
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse requiredQueryParamNotFound(MissingServletRequestParameterException ex) {
		String paramName = ex.getParameterName();
		ApiError error = errorManager.getError(DefaultApigenError.QUERY_PARAM_REQUIRED, paramName);
		return new ApiResponse().withResultErrors(errors(error));
	}

	@ResponseBody
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse queryParamNotValid(ConstraintViolationException ex) {
		List<ApiError> errors = new ArrayList<>();
		for (ConstraintViolation violation : ex.getConstraintViolations()) {
			String path = violation.getPropertyPath().toString();
			String[] pathParts = path.split("\\.");
			String methodName = pathParts[0];
			String parameterName = pathParts[1];
			Method[] methods = violation.getRootBeanClass().getDeclaredMethods();
			String fieldName = Stream.of(methods)
					.filter(m -> methodName.equals(m.getName()))
					.flatMap(m -> Stream.of(m.getParameters()))
					.filter(p -> parameterName.equals(p.getName()))
					.filter(p -> p.isAnnotationPresent(RequestParam.class))
					.map(p -> p.getDeclaredAnnotation(RequestParam.class))
					.map(RequestParam::value)
					.findFirst().orElse(parameterName);
			String code = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
			Object[] args = violation.getExecutableParameters();
			errors.add(getValidationError(fieldName, code, args));
		}
		return new ApiResponse().withResultErrors(errors);
	}

	@ResponseBody
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse mismatch(MethodArgumentTypeMismatchException ex) {
		Parameter parameter = ex.getParameter().getParameter();
		String variableName = parameter.getName();
		if (parameter.isAnnotationPresent(PathVariable.class)) {
			PathVariable pathVariable = parameter.getDeclaredAnnotation(PathVariable.class);
			if (!pathVariable.value().trim().equals("")) variableName = pathVariable.value();
		}
		ApiError error = errorManager.getError(DefaultApigenError.PATH_VARIABLE_ERROR, variableName);
		return new ApiResponse().withResultErrors(errors(error));
	}

	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse argumentNotValid(MethodArgumentNotValidException ex) {
		List<ApiError> errors = new ArrayList<>();
		Object target = ex.getBindingResult().getTarget();
		Class clazz = target.getClass();
		FieldNameTranslator fieldNamesTranslator = new FieldNameTranslator(clazz);
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			String field = fieldNamesTranslator.translate(fieldError.getField());
			String code = fieldError.getCode();
			ApiError error = getValidationError(field, code, fieldError.getArguments());
			errors.add(error);
		}
		return new ApiResponse().withResultErrors(errors);
	}

	private ApiError getValidationError(String field, String code, Object[] args) {
		DefaultApigenError error = getError(code, args);
		Object[] errorArgs = getArgs(code, args);
		return errorManager.getError(error, field, errorArgs);
	}

	private DefaultApigenError getError(String code, Object[] args) {
		boolean inclusive;
		switch (code) {
			case "NotNull":
				return DefaultApigenError.VALIDATION_NOT_NULL;
			case "Null":
				return DefaultApigenError.VALIDATION_NULL;
			case "Email":
				return DefaultApigenError.VALIDATION_EMAIL;
			case "NotEmpty":
				return DefaultApigenError.VALIDATION_NOT_EMPTY;
			case "NotBlank":
				return DefaultApigenError.VALIDATION_NOT_BLANK;
			case "Positive":
				return DefaultApigenError.VALIDATION_MIN_VALUE;
			case "Min":
			case "PositiveOrZero":
				return DefaultApigenError.VALIDATION_MIN_EQ_VALUE;
			case "Negative":
				return DefaultApigenError.VALIDATION_MAX_VALUE;
			case "Max":
			case "NegativeOrZero":
				return DefaultApigenError.VALIDATION_MAX_EQ_VALUE;
			case "Past":
				return DefaultApigenError.VALIDATION_PAST_DATE;
			case "PastOrPresent":
				return DefaultApigenError.VALIDATION_PAST_NOW_DATE;
			case "Future":
				return DefaultApigenError.VALIDATION_FUTURE_DATE;
			case "FutureOrPresent":
				return DefaultApigenError.VALIDATION_FUTURE_NOW_DATE;
			case "DecimalMin":
				inclusive = (boolean) args[1];
				return inclusive ? DefaultApigenError.VALIDATION_MIN_EQ_VALUE : DefaultApigenError.VALIDATION_MIN_VALUE;
			case "DecimalMax":
				inclusive = (boolean) args[1];
				return inclusive ? DefaultApigenError.VALIDATION_MAX_EQ_VALUE : DefaultApigenError.VALIDATION_MAX_VALUE;
			case "Size":
				return DefaultApigenError.VALIDATION_BAD_SIZE;
			case "Digits":
				return DefaultApigenError.VALIDATION_DECIMAL;
			case "Pattern":
				return DefaultApigenError.VALIDATION_REGEX;
			default:
				return DefaultApigenError.VALIDATION_ERROR;
		}
	}

	private Object[] getArgs(String code, Object[] allArgs) {
		Object[] args = {};
		switch (code) {
			case "Positive":
			case "PositiveOrZero":
			case "Negative":
			case "NegativeOrZero":
				return toArgs(0);
			case "Min":
			case "Max":
				return toArgs(allArgs[1]);
			case "DecimalMin":
			case "DecimalMax":
			case "Pattern":
				return toArgs(allArgs[2]);
			case "Size":
			case "Digits":
				return toArgs(allArgs[2], allArgs[1]);
			default:
				return args;
		}
	}

	private Object[] toArgs(Object... args) {
		return args;
	}

	@ResponseBody
	@ExceptionHandler(InvalidPropertyPath.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse requiredQueryParamNotFound(InvalidPropertyPath ex) {
		List<ApiError> errors = new ArrayList<>();
		for (String path : ex.getInvalidSelectPath()) {
			errors.add(errorManager.getError(DefaultApigenError.BAD_PROPERTY_IN_SELECT, path));
		}
		for (String path : ex.getInvalidExcludePath()) {
			errors.add(errorManager.getError(DefaultApigenError.BAD_PROPERTY_IN_EXCLUDE, path));
		}
		for (String path : ex.getInvalidExpandPath()) {
			errors.add(errorManager.getError(DefaultApigenError.BAD_PROPERTY_IN_EXPAND, path));
		}
		for (String path : ex.getInvalidFilterPath()) {
			errors.add(errorManager.getError(DefaultApigenError.BAD_PROPERTY_IN_FILTER, path));
		}
		for (String path : ex.getInvalidOrderByPath()) {
			errors.add(errorManager.getError(DefaultApigenError.BAD_PROPERTY_IN_ORDER_BY, path));
		}
		for (String path : ex.getInvalidOrderByToManyPath()) {
			errors.add(errorManager.getError(DefaultApigenError.PROPERTY_NOT_ALLOWED_IN_ORDER_BY, path));
		}
		return new ApiResponse().withResultErrors(errors);
	}

	@ResponseBody
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse entityNotFound(EntityNotFoundException ex) {
		ApiError error = errorManager.getError(DefaultApigenError.ELEMENT_NOT_FOUND, ex.getId().toString(), ex.getClazz().getSimpleName());
		return new ApiResponse().withResultErrors(errors(error));
	}
	
	@ResponseBody
	@ExceptionHandler(EntityIdAlreadyInUseException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse existingId(EntityIdAlreadyInUseException ex) {
		ApiError error = errorManager.getError(DefaultApigenError.EXISTING_ID_ERROR, ex.getId().toString(), ex.getClazz().getSimpleName());
		return new ApiResponse().withResultErrors(errors(error));
	}

	@ResponseBody
	@ExceptionHandler(RelationalErrorsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse relatedEntitiesNotFound(RelationalErrorsException ex) {
		List<ApiError> errors = ex.getRelationalErrors().getErrors().stream()
				.map(e -> errorManager.getError(
						DefaultApigenError.RELATED_ELEMENT_NOT_FOUND,
						e.getId() == null ? "null" : e.getId().toString(),
						e.getClazz().getSimpleName()
				))
				.collect(Collectors.toList());
		return new ApiResponse().withResultErrors(errors);
	}

	@ResponseBody
	@ExceptionHandler(CustomApigenException.class)
	public ResponseEntity<ApiResponse> customException(CustomApigenException ex) {
		List<ApiError> errors = ex.getErrors().stream().
				map(error -> errorManager.getError(error.getKey(), error.getElement(), error.getOtherElements()))
				.collect(Collectors.toList());
		ApiResponse response = new ApiResponse().withResultErrors(errors);
		return new ResponseEntity<>(response, ex.getHttpStatus());
	}

	@ResponseBody
	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse acceptMediaTypeNotSupported(HttpMediaTypeNotAcceptableException ex) {
		ApiError error = errorManager.getError(DefaultApigenError.UNSUPPORTED_ACCEPT_FORMAT, null, ex.getSupportedMediaTypes());
		return new ApiResponse().withResultErrors(errors(error));
	}

	@ResponseBody
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse contentTypeMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
		ApiError error = errorManager.getError(DefaultApigenError.UNSUPPORTED_CONTENT_TYPE_FORMAT, null, ex.getContentType().toString());
		return new ApiResponse().withResultErrors(errors(error));
	}

	@ResponseBody
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse requiredBodyNotFound(HttpMessageNotReadableException ex) {
		Throwable cause = ex.getCause();
		if (cause instanceof InvalidFormatException) {
			ApiResponse response = getResponseFromInvalidFormatException((InvalidFormatException) cause);
			if (response != null) return response;
		}
		log.error("Body error", ex);
		ApiError error = errorManager.getError(DefaultApigenError.EMPTY_REQUEST_BODY);
		return new ApiResponse().withResultErrors(errors(error));
	}

	private ApiResponse getResponseFromInvalidFormatException(InvalidFormatException exception) {
		Throwable cause = exception.getCause();
		if (cause instanceof DateTimeParseException) {
			DateTimeParseException explicit = (DateTimeParseException) cause;
			ApiError error = errorManager.getError(DefaultApigenError.ERROR_PARSING_ISO_DATE, explicit.getParsedString());
			return new ApiResponse().withResultErrors(errors(error));
		}
		String message = exception.getMessage();
		if (message.contains("Enum class")) {
			int s = message.indexOf('[');
			int e = message.indexOf(']');
			if (s > -1 && e > -1) {
				String values = message.substring(s + 1, e);
				Object currentValue = exception.getValue();
				ApiError error = errorManager.getError(DefaultApigenError.UNSUPPORTED_VALUE, currentValue.toString(), values);
				return new ApiResponse().withResultErrors(errors(error));
			}
		}
		return null;
	}

	@ResponseBody
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	public ApiResponse methodNotSupported(HttpRequestMethodNotSupportedException ex) {
		ApiError error = errorManager.getError(DefaultApigenError.METHOD_NOT_IMPLEMENTED, ex.getMethod());
		return new ApiResponse().withResultErrors(errors(error));
	}

	@ResponseBody
	@ExceptionHandler(NotImplementedException.class)
	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	public ApiResponse methodNotImplemented(NotImplementedException ex) {
		ApiError error = errorManager.getError(DefaultApigenError.METHOD_NOT_IMPLEMENTED, ex.getMessage());
		return new ApiResponse().withResultErrors(errors(error));
	}

	@ResponseBody
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse handlerNotFound(NoHandlerFoundException ex) {
		ApiError error = errorManager.getError(DefaultApigenError.PATH_NOT_IMPLEMENTED, ex.getRequestURL());
		return new ApiResponse().withResultErrors(errors(error));
	}

	@ResponseBody
	@ExceptionHandler(PatternSyntaxException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse handleRegexException(PatternSyntaxException ex) {
		ApiError error = errorManager.getError(DefaultApigenError.INVALID_REGEX_EXPRESSION, ex.getPattern());
		return new ApiResponse().withResultErrors(errors(error));
	}

	@ResponseBody
	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	public ResponseEntity handleDataAccessApiUsage(InvalidDataAccessApiUsageException ex) {
		ApiError error;
		if (ex.getCause() instanceof PatternSyntaxException) {
			error = errorManager.getError(DefaultApigenError.INVALID_REGEX_EXPRESSION, ((PatternSyntaxException)ex.getCause()).getPattern());
		} else if (ex.getCause() instanceof IllegalArgumentException) {
			error = errorManager.getError(DefaultApigenError.ILLEGAL_ARGUMENT, ex.getCause().getMessage());
		} else {
			return new ResponseEntity<>(exception(ex), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new ApiResponse().withResultErrors(errors(error)), HttpStatus.BAD_REQUEST);
	}

	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse exception(Exception ex) {
		log.error("Unexpected error", ex);
		ApiError error = errorManager.getError(DefaultApigenError.UNEXPECTED_ERROR);
		return new ApiResponse().withResultErrors(errors(error));
	}

	private List<ApiError> errors(ApiError... apiErrors) {
		return Arrays.asList(apiErrors);
	}

	private static class FieldNameTranslator {
		Map<String, String> dictionary;

		FieldNameTranslator(Class clazz) {
			dictionary = getDictionary(clazz);
		}

		private Map<String, String> getDictionary(Class clazz) {
			Map<String, String> levelDictionary = new HashMap<>();
			for (Field field : clazz.getDeclaredFields()) {
				String javaName = field.getName();
				String resourceName = javaName;
				if (field.isAnnotationPresent(JsonProperty.class)) {
					JsonProperty annotation = field.getDeclaredAnnotation(JsonProperty.class);
					resourceName = annotation.value();
				}
				levelDictionary.put(javaName, resourceName);
				Class fieldClazz = field.getType();
				if (field.getGenericType() instanceof ParameterizedType) {
					fieldClazz = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
				}
				Package pkg = fieldClazz.getPackage();
				if (pkg != null && !pkg.getName().startsWith("java") && !fieldClazz.equals(Filter.class) && !fieldClazz.isEnum()) {
					Map<String, String> nestedNames = getDictionary(fieldClazz);
					String resName = resourceName;
					nestedNames.forEach((key, value) -> levelDictionary.put(javaName + "." + key, resName + "." + value));
				}
			}
			return levelDictionary;
		}

		public String translate(String name) {
			String[] originalParts = name.split("\\.");
			String[] arrayParts = new String[originalParts.length];
			for (int i = 0; i < originalParts.length; i++) {
				int idx = originalParts[i].indexOf('[');
				if (idx > -1) {
					arrayParts[i] = originalParts[i].substring(idx);
					originalParts[i] = originalParts[i].substring(0, idx);
				} else {
					arrayParts[i] = "";
				}
			}
			String key = String.join(".", originalParts);
			String value = dictionary.get(key);
			if (value == null) return name;
			String[] newParts = value.split("\\.");
			for (int i = 0; i < originalParts.length; i++) {
				newParts[i] = newParts[i] + arrayParts[i];
			}
			return String.join(".", newParts);
		}
	}
}
