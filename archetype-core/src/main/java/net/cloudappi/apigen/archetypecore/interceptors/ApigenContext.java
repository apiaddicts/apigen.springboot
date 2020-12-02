package net.cloudappi.apigen.archetypecore.interceptors;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class ApigenContext {

	private static final String TRACE_ID = "TRACE_ID";

	protected ApigenContext() {
		// Intentional blank
	}

	public static Object getRequestAttribute(String name) {
		return RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_REQUEST);
	}

	public static void setRequestAttribute(String name, Object value) {
		RequestContextHolder.getRequestAttributes().setAttribute(name, value, RequestAttributes.SCOPE_REQUEST);
	}

	public static String getTraceId() {
		return (String) getRequestAttribute(TRACE_ID);
	}

	public static void setTraceId(String traceId) {
		setRequestAttribute(TRACE_ID, traceId);
	}
}
