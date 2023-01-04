package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.FilterOperation.*;

@Service
public class FilterBuilderImpl implements FilterBuilder {

    @Override
    public Filter and(Filter... values) {
        return new Filter(AND, Arrays.stream(values).map(Filter::getValues).flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    @Override
    public Filter or(Filter... values) {
        return new Filter(OR, Arrays.stream(values).map(Filter::getValues).flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    @Override
    public Filter greater(String field, String value) {
        return new Filter(GT, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    @Override
    public Filter less(String field, String value) {
        return new Filter(LT, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    @Override
    public Filter greaterOrEqual(String field, String value) {
        return new Filter(GTEQ, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    @Override
    public Filter lessThanOrEqualTo(String field, String value) {
        return new Filter(LTEQ, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    @Override
    public Filter eq(String field, String value) {
        return new Filter(EQ, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    @Override
    public Filter neq(String field, String value) {
        return new Filter(NEQ, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    @Override
    public Filter substring(String field, String value) {
        return new Filter(SUBSTRING, new ArrayList<>(Collections.singletonList(new Value(field, "%" + value + "%"))));
    }

    @Override
    public Filter like(String field, String value) {
        return new Filter(LIKE, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    @Override
    public Filter ilike(String field, String value) {
        return new Filter(ILIKE, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    @Override
    public Filter nlike(String field, String value) {
        return new Filter(NLIKE, new ArrayList<>(Collections.singletonList(new Value(field, value))));
    }

    @Override
    public Filter regexp(String field, Pattern pattern) {
        return new Filter(REGEXP, new ArrayList<>(Collections.singletonList(new Value(field, pattern.toString()))));
    }

    @Override
    public Filter in(String field, String... values) {
        return new Filter(IN, new ArrayList<>(Collections.singletonList(new Value(field, Arrays.stream(values).collect(Collectors.toList())))));
    }

    @Override
    public Filter between(String field, String... values) {
        return new Filter(BETWEEN, new ArrayList<>(Collections.singletonList(new Value(field, Arrays.stream(values).collect(Collectors.toList())))));
    }
}