package org.apiaddicts.apitools.apigen.archetypecore.interceptors.response;

import org.apiaddicts.apitools.apigen.archetypecore.core.responses.ApiResponse;
import org.apiaddicts.apitools.apigen.archetypecore.core.responses.result.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static java.util.Objects.isNull;

public abstract class ApiResponseEnhancer {

	public ApiResponse enhance(ApiResponse apiResponse, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, String traceHeader) {
		if (apiResponse == null) {
			return null;
		}

		HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
		HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();

		completeResponseResult(apiResponse, httpServletRequest, httpServletResponse, traceHeader);
		completeResponseMetadata(apiResponse, httpServletRequest);
		return apiResponse;
	}

	private void completeResponseResult(ApiResponse apiResponse, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String traceHeader) {
		ApiResult result = apiResponse.getResult();
		if (result == null) {
			result = new ApiResult();
		}

		int status = httpServletResponse.getStatus();
		boolean success = HttpStatus.valueOf(status).is2xxSuccessful() || HttpStatus.valueOf(status).is3xxRedirection();

		if (isNull(result.getStatus())) result.setStatus(success);
		if (isNull(result.getHttpCode())) result.setHttpCode(status);
		if (isNull(result.getInfo())) result.setInfo(success ? "OK" : "ERROR");
		if (isNull(result.getTraceId())) result.setTraceId(httpServletRequest.getHeader(traceHeader));

		apiResponse.setResult(result);
	}

	private void completeResponseMetadata(ApiResponse apiResponse, HttpServletRequest httpServletRequest) {
		if (apiResponse.getMetadata() == null || apiResponse.getMetadata().getPaging() == null) {
			return;
		}
		String path = getCorrectPath(httpServletRequest);
		UriComponentsBuilder url = UriComponentsBuilder.newInstance()
				.scheme(httpServletRequest.getScheme())
				.host(httpServletRequest.getServerName())
				.path(path)
				.query(httpServletRequest.getQueryString());
		apiResponse.getMetadata().getPaging().addLinks(url);
	}

	private String getCorrectPath(HttpServletRequest httpServletRequest) {
		if(httpServletRequest.getPathInfo() != null) {
			return httpServletRequest.getPathInfo();
		} else {
			return httpServletRequest.getServletPath();
		}
	}
}
