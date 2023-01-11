package org.apiaddicts.apitools.apigen.archetypecore.interceptors.trace;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.archetypecore.interceptors.ApigenContext;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
public class TraceIdInterceptor implements HandlerInterceptor {

	private final String traceHeader;

	public TraceIdInterceptor(String traceHeader) {
		this.traceHeader = traceHeader;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String traceId = request.getHeader(traceHeader);
		log.debug("Intercepted [{} {}] with traceId={}", request.getMethod(), request.getRequestURI(), traceId);
		if (traceId != null) ApigenContext.setTraceId(traceId);
		return true;
	}
}
