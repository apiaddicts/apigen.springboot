package org.apiaddicts.apitools.apigen.archetypecore.interceptors.update;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class CachingRequestBodyFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest currentRequest = (HttpServletRequest) servletRequest;
        if (currentRequest.getMethod().equals("PUT")) {
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(currentRequest);
            chain.doFilter(wrappedRequest, servletResponse);
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }
}
