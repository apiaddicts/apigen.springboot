package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.FilterOperation.*;

public class FilterUtils {

    private FilterUtils() {
        // Intentional blank
    }

    public static Filter and(Filter... values) {
        return new Filter(AND, Arrays.stream(values).map(Value::new).toList());
    }

    public static Filter or(Filter... values) {
        return new Filter(OR, Arrays.stream(values).map(Value::new).toList());
    }

    public static Filter gt(String property, String value) {
        return new Filter(GT, new ArrayList<>(Collections.singletonList(new Value(property, value))));
    }

    public static Filter lt(String property, String value) {
        return new Filter(LT, new ArrayList<>(Collections.singletonList(new Value(property, value))));
    }

    public static Filter gteq(String property, String value) {
        return new Filter(GTEQ, new ArrayList<>(Collections.singletonList(new Value(property, value))));
    }

    public static Filter lteq(String property, String value) {
        return new Filter(LTEQ, new ArrayList<>(Collections.singletonList(new Value(property, value))));
    }

    public static Filter eq(String property, String value) {
        return new Filter(EQ, new ArrayList<>(Collections.singletonList(new Value(property, value))));
    }

    public static Filter neq(String property, String value) {
        return new Filter(NEQ, new ArrayList<>(Collections.singletonList(new Value(property, value))));
    }

    public static Filter substring(String property, String value) {
        return new Filter(SUBSTRING, new ArrayList<>(Collections.singletonList(new Value(property, "%" + value + "%"))));
    }

    public static Filter like(String property, String value) {
        return new Filter(LIKE, new ArrayList<>(Collections.singletonList(new Value(property, value))));
    }

    public static Filter ilike(String property, String value) {
        return new Filter(ILIKE, new ArrayList<>(Collections.singletonList(new Value(property, value))));
    }

    public static Filter nlike(String property, String value) {
        return new Filter(NLIKE, new ArrayList<>(Collections.singletonList(new Value(property, value))));
    }

    public static Filter regexp(String property, Pattern pattern) {
        return new Filter(REGEXP, new ArrayList<>(Collections.singletonList(new Value(property, pattern.toString()))));
    }

    public static Filter in(String property, String... values) {
        return new Filter(IN, new ArrayList<>(Collections.singletonList(new Value(property, Arrays.stream(values).collect(Collectors.toList())))));
    }

    public static Filter between(String property, String value1, String value2) {
        return new Filter(BETWEEN, new ArrayList<>(Collections.singletonList(new Value(property, Stream.of(value1, value2).collect(Collectors.toList())))));
    }
}
