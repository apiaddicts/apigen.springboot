package net.cloudappi.apigen.archetypecore.interceptors.trace;

import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.archetypecore.interceptors.ApigenContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class TraceIdInterceptor extends HandlerInterceptorAdapter {

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
