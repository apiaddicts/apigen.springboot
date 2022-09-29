package org.apiaddicts.apitools.apigen.archetypecore.core.controllers;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.FilterOperation;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Value;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MoreLevelsPathController {

    protected Filter getParentFilter(String id, Filter originalFilter, String childParentRelationProperty) {
        Value value = new Value();
        value.setProperty(childParentRelationProperty);
        value.setValue(id);

        Filter sf = filter(FilterOperation.EQ, value);
        Value sfv = value(sf);

        if (originalFilter == null) {
            return filter(FilterOperation.AND, sfv);
        } else {
            Value ov = value(originalFilter);
            return filter(FilterOperation.AND, sfv, ov);
        }
    }

    protected List<String> getparentExpand(List<String> expand, String parentEntity) {
        if (expand == null) expand = new LinkedList<>();
        if (!expand.contains(parentEntity.toLowerCase())) expand.add(parentEntity.toLowerCase());
        return expand;
    }

    protected Value value(Filter f) {
        Value v = new Value();
        v.setFilter(f);
        return v;
    }

    protected Filter filter(FilterOperation op, Value... values) {
        Filter f = new Filter();
        f.setOperation(op);
        f.setValues(Arrays.stream(values).collect(Collectors.toList()));
        return f;
    }
}
