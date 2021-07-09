package org.apiaddicts.apitools.apigen.archetypecore.interceptors.expand;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ExpandAnnotationInterceptor extends ExpandInterceptor {

    private final int defaultMaxLevel;

    public ExpandAnnotationInterceptor(Integer defaultMaxLevel) {
        this.defaultMaxLevel = defaultMaxLevel;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!(handler instanceof HandlerMethod)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        ApigenExpand expandAnnotation = method.getAnnotation(ApigenExpand.class);

        if (expandAnnotation == null) return true;

        Set<String> allowed = toSet(expandAnnotation.allowed());
        Set<String> excluded = toSet(expandAnnotation.excluded());
        int maxLevel = expandAnnotation.maxLevel();
        if (maxLevel < 0) maxLevel = defaultMaxLevel;

        validate(request, allowed, excluded, maxLevel);

        return true;
    }

    private Set<String> toSet(String[] values) {
        return Stream.of(values).collect(Collectors.toSet());
    }
}
