package net.cloudappi.apigen.archetypecore.interceptors.update;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
