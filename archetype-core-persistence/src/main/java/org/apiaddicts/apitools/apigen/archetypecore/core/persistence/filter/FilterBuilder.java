package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public interface FilterBuilder {
    Filter and(Filter... values);
    Filter or(Filter... values);
    Filter greater(String field, String value);
    Filter less(String field, String value);
    Filter greaterOrEqual(String field, String value);
    Filter lessThanOrEqualTo(String field, String value);
    Filter eq(String field, String value);
    Filter neq(String field, String value);
    Filter substring(String field, String value);
    Filter like(String field, String value);
    Filter ilike(String field, String value);
    Filter nlike(String field, String value);
    Filter regexp(String field, Pattern pattern);
    Filter in(String field, String... values);
    Filter between(String field, String... values);
}