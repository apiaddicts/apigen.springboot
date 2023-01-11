package org.apiaddicts.apitools.apigen.archetypecore.core.controllers;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;

import java.util.LinkedList;
import java.util.List;

import static org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.FilterUtils.and;
import static org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.FilterUtils.eq;

public abstract class NestedParentChildController {

    protected Filter getParentFilter(Object parentId, Filter originalFilter, String childParentIdField) {
        return getParentFilter(parentId.toString(), originalFilter, childParentIdField);
    }

    protected Filter getParentFilter(String parentId, Filter originalFilter, String childParentIdField) {
        if (originalFilter == null) {
            return eq(childParentIdField, parentId);
        } else {
            return and(originalFilter, eq(childParentIdField, parentId));
        }
    }

    protected List<String> getParentExpand(List<String> expand, String parentExpandProperty) {
        if (expand == null) expand = new LinkedList<>();
        if (!expand.contains(parentExpandProperty)) expand.add(parentExpandProperty);
        return expand;
    }
}
