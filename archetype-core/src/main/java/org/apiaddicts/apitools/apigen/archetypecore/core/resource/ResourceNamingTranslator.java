package org.apiaddicts.apitools.apigen.archetypecore.core.resource;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;

import java.util.List;

public interface ResourceNamingTranslator {
	void translate(List<String> select, List<String> exclude, List<String> expand, Class resourceClass);
	void translate(List<String> select, List<String> exclude, List<String> expand, List<String> orderBy, Class resourceClass);
	void translate(List<String> select, List<String> exclude, List<String> expand, Filter filter, List<String> orderBy, Class resourceClass);
	void translate(Filter filter, Class resourceClass);
}
