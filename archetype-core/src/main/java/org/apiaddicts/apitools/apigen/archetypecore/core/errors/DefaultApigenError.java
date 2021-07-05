package org.apiaddicts.apitools.apigen.archetypecore.core.errors;

import lombok.Getter;

@Getter
public enum DefaultApigenError {

	// 10xx : Validation error
	VALIDATION_NOT_NULL(1000, "Property ''{0}'' must be not null"),
	VALIDATION_BAD_SIZE(1001, "Property ''{0}'' must have a length between ''{1}'' and ''{2}''"),
	VALIDATION_MIN_VALUE(1002, "Property ''{0}'' must have a value greater than ''{1}''"),
	VALIDATION_MIN_EQ_VALUE(1003, "Property ''{0}'' must have a value greater or equal to ''{1}''"),
	VALIDATION_MAX_VALUE(1004, "Property ''{0}'' must have a value less than ''{1}''"),
	VALIDATION_MAX_EQ_VALUE(1005, "Property ''{0}'' must have a value less or equal to ''{1}''"),
	VALIDATION_REGEX(1006, "Property ''{0}'' must follow the regex ''{1}''"),
	VALIDATION_NOT_EMPTY(1007, "Property ''{0}'' must not be empty"),
	VALIDATION_NOT_BLANK(1008, "Property ''{0}'' must not be blank"),
	VALIDATION_EMAIL(1009, "Property ''{0}'' must be an email"),
	VALIDATION_PAST_DATE(1010, "Property ''{0}'' must be a past date"),
	VALIDATION_PAST_NOW_DATE(1011, "Property ''{0}'' must be a past or present date"),
	VALIDATION_FUTURE_DATE(1012, "Property ''{0}'' must be a future"),
	VALIDATION_FUTURE_NOW_DATE(1013, "Property ''{0}'' must be a future or present"),
	VALIDATION_DECIMAL(1014, "Property ''{0}'' must be a decimal with ''{1}'' integer digits and ''{2}'' decimal digits"),
	VALIDATION_NULL(1015, "Property ''{0}'' must be null"),
	VALIDATION_ERROR(1099, "Property ''{0}'' must be valid"),

	// 110x : Property path error
	BAD_PROPERTY_IN_FILTER(1100, "Invalid property ''{0}'' in $filter"),
	BAD_PROPERTY_IN_SELECT(1101, "Invalid property ''{0}'' in $select"),
	BAD_PROPERTY_IN_EXCLUDE(1102, "Invalid property ''{0}'' in $exclude"),
	BAD_PROPERTY_IN_EXPAND(1103, "Invalid property ''{0}'' in $expand"),
	BAD_PROPERTY_IN_ORDER_BY(1104, "Invalid property ''{0}'' in $orderby"),
	PROPERTY_NOT_ALLOWED_IN_ORDER_BY(1105, "Invalid property ''{0}'' in $orderby"),
	ELEMENT_NOT_FOUND(1106, "Element with id ''{0}'' of type ''{1}'' not found"),
	RELATED_ELEMENT_NOT_FOUND(1107, "Related element with id ''{0}'' of type ''{1}'' not found"),
	ERROR_PARSING_ISO_DATE(1108, "Error parsing ISO date ''{0}''"),
	PATH_VARIABLE_ERROR(1109, "Error parsing path variable ''{0}''"),
	QUERY_PARAM_REQUIRED(1110, "Query parameter ''{0}'' is required"),
	INVALID_REGEX_EXPRESSION(1111, "Invalid regex expression ''{0}''"),
	ILLEGAL_ARGUMENT(1112, "Illegal argument: {0}"),
	EXISTING_ID_ERROR(1113,"Element with id ''{0}'' of type ''{1}'' already exists"),


	// 12xx : General errors
	METHOD_NOT_IMPLEMENTED(1200, "Method ''{0}'' not implemented"),
	PATH_NOT_IMPLEMENTED(1201, "Path ''{0}'' not implemented"),
	EMPTY_REQUEST_BODY(1202, "Request body required"),
	UNSUPPORTED_ACCEPT_FORMAT(1203, "Accept format not supported, supported formats: ''{1}''"),
	UNSUPPORTED_VALUE(1204, "Unsupported value ''{0}'', accepted values: ''{1}''"),
	UNSUPPORTED_CONTENT_TYPE_FORMAT(1205, "Content-type format not supported: ''{1}''"),

	// 13xx : Unexpected errors
	UNEXPECTED_ERROR(1300, "Unexpected error");

	private final Integer defaultCode;
	private final String defaultMessage;

	private DefaultApigenError(Integer defaultCode, String defaultMessage) {
		this.defaultCode = defaultCode;
		this.defaultMessage = defaultMessage;
	}
}
