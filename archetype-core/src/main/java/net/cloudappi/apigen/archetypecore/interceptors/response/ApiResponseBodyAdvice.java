package net.cloudappi.apigen.archetypecore.interceptors.response;

import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.archetypecore.autoconfigure.ApigenProperties;
import net.cloudappi.apigen.archetypecore.core.responses.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@Slf4j
public class ApiResponseBodyAdvice extends ApiResponseEnhancer implements ResponseBodyAdvice<Object> {

	private final String traceHeader;

	public ApiResponseBodyAdvice(ApigenProperties apigenProperties) {
		this.traceHeader = apigenProperties.getTraceHeader();
	}

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
		return ApiResponse.class.isAssignableFrom(methodParameter.getParameterType())
				|| ResponseEntity.class.isAssignableFrom(methodParameter.getParameterType());
	}

	@Override
	public Object beforeBodyWrite(Object maybeApiResponse, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
		if (ApiResponse.class.isAssignableFrom(maybeApiResponse.getClass())) {
			ApiResponse apiResponse = (ApiResponse) maybeApiResponse;
			return enhance(apiResponse, serverHttpRequest, serverHttpResponse, traceHeader);
		}
		return maybeApiResponse;
	}
}
