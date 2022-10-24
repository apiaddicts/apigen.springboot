package org.apiaddicts.apitools.apigen.archetypecore.core.persistence;

import lombok.Data;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.pagination.Pagination;

import java.util.List;

@Data
public class ApigenSearch {
	private List<String> select;
	private List<String> exclude;
	private List<String> expand;
	private Filter filter;
	private List<String> orderBy;
	private Pagination pagination;
	private Boolean total = false;

	public ApigenSearch(List<String> select, List<String> exclude, List<String> expand) {
		this.select = select;
		this.exclude = exclude;
		this.expand = expand;
	}

	public ApigenSearch(List<String> select, List<String> exclude, List<String> expand, Filter filter, List<String> orderBy, Pagination pagination, Boolean total) {
		this.select = select;
		this.exclude = exclude;
		this.expand = expand;
		this.filter = filter;
		this.orderBy = orderBy;
		this.pagination = pagination;
		this.total = total;
	}
}
