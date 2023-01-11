package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.FilterOperation.*;

public class FilterBuilderUtils {

    private FilterBuilderUtils(){}

    public static Filter and(Filter... values) {
        return new Filter(AND, Arrays.stream(values).map(Filter::getValues).flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    public static Filter or(Filter... values) {
        return new Filter(OR, Arrays.stream(values).map(Filter::getValues).flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    public static Filter greater(String field, String value) {
        return new Filter(GT, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    public static Filter less(String field, String value) {
        return new Filter(LT, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    public static Filter greaterOrEqual(String field, String value) {
        return new Filter(GTEQ, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    public static Filter lessThanOrEqualTo(String field, String value) {
        return new Filter(LTEQ, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    public static Filter eq(String field, String value) {
        return new Filter(EQ, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    public static Filter neq(String field, String value) {
        return new Filter(NEQ, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    public static Filter substring(String field, String value) {
        return new Filter(SUBSTRING, new ArrayList<>(Collections.singletonList(new Value(field, "%" + value + "%"))));
    }

    public static Filter like(String field, String value) {
        return new Filter(LIKE, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    public static Filter ilike(String field, String value) {
        return new Filter(ILIKE, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    public static Filter nlike(String field, String value) {
        return new Filter(NLIKE, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    public static Filter regexp(String field, Pattern pattern) {
        return new Filter(REGEXP, new ArrayList<>(Collections.singletonList(new Value(field, pattern.toString()))));
    }

    public static Filter in(String field, String... values) {
        return new Filter(IN, new ArrayList<>(Collections.singletonList(new Value(field, Arrays.stream(values).collect(Collectors.toList())))));
    }

    public static Filter between(String field, String... values) {
        return new Filter(BETWEEN, new ArrayList<>(Collections.singletonList(new Value(field, Arrays.stream(values).collect(Collectors.toList())))));
    }
}