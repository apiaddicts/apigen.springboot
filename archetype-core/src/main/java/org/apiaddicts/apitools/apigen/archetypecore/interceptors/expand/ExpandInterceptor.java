package org.apiaddicts.apitools.apigen.archetypecore.interceptors.expand;

import org.apiaddicts.apitools.apigen.archetypecore.exceptions.InvalidPropertyPath;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

public class ExpandInterceptor implements HandlerInterceptor {

    protected void validate(HttpServletRequest request, Set<String> allowed, Set<String> excluded, int maxLevel) {
        Set<String> expands = getExpands(request);
        int level = getLevel(expands);
        if (hasValues(excluded)) {
            if (containsExcludedValue(expands, excluded)) {
                throwException(getExcludedValues(expands, excluded));
            }
            if (level > maxLevel) {
                throwException(getIllegalLevelValues(expands, maxLevel));
            }
        } else if (hasValues(allowed)) {
            if (containsNotAllowedValue(expands, allowed)) {
                throwException(getNotAllowedValues(expands, allowed));
            }
        } else {
            if (level > maxLevel) {
                throwException(getIllegalLevelValues(expands, maxLevel));
            }
        }
    }

    private boolean hasValues(Set<String> values) {
        return nonNull(values) && !values.isEmpty();
    }

    private boolean containsExcludedValue(Set<String> values, Set<String> excluded) {
        return !Collections.disjoint(excluded, values);
    }

    private boolean containsNotAllowedValue(Set<String> values, Set<String> allowed) {
        return !allowed.containsAll(values);
    }

    private List<String> getIllegalLevelValues(Set<String> values, int maxLevel) {
        return values.stream().filter(value -> getLevel(value) > maxLevel).collect(Collectors.toList());
    }

    private Set<String> getNotAllowedValues(Set<String> values, Set<String> allowed) {
        values.removeAll(allowed);
        return values;
    }

    private Set<String> getExcludedValues(Set<String> values, Set<String> excluded) {
        values.retainAll(excluded);
        return values;
    }

    private int getLevel(Set<String> expands) {
        return expands.stream().map(this::getLevel).mapToInt(i -> i).max().orElse(0);
    }

    private int getLevel(String expand) {
        return StringUtils.countOccurrencesOf(expand, ".") + 1;
    }

    private Set<String> getExpands(HttpServletRequest request) {
        Set<String> values = new HashSet<>();
        String[] queryValues = request.getParameterValues("$expand");
        if (queryValues == null) return values;
        Stream.of(queryValues).flatMap(queryValue -> Stream.of(queryValue.split(","))).forEach(values::add);
        return values;
    }

    private void throwException(Collection<String> values) {
        InvalidPropertyPath errors = new InvalidPropertyPath();
        errors.getInvalidExpandPath().addAll(values);
        throw errors;
    }
}
