package org.apiaddicts.apitools.apigen.archetypecore.core.controllers;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.FilterOperation;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Value;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class NestedParentChildController {

    protected Filter getParentFilter(Object parentId, Filter originalFilter, String childParentIdField) {
        return getParentFilter(parentId.toString(), originalFilter, childParentIdField);
    }

    protected Filter getParentFilter(String parentId, Filter originalFilter, String childParentIdField) {
        Value value = new Value();
        value.setProperty(childParentIdField);
        value.setValue(parentId);

        Filter sf = filter(FilterOperation.EQ, value);
        Value sfv = value(sf);

        if (originalFilter == null) {
            return filter(FilterOperation.AND, sfv);
        } else {
            Value ov = value(originalFilter);
            return filter(FilterOperation.AND, sfv, ov);
        }
    }

    protected List<String> getParentExpand(List<String> expand, String parentExpandProperty) {
        if (expand == null) expand = new LinkedList<>();
        if (!expand.contains(parentExpandProperty)) expand.add(parentExpandProperty);
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
