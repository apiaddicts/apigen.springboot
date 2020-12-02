package net.cloudappi.apigen.archetypecore.core.persistence.pagination;

import lombok.Data;

@Data
public class Pagination {

	private int init = -1;
	private int limit = -1;

	public Pagination(int init, int limit) {
		this.init = init;
		this.limit = limit;
	}
}
