package org.apiaddicts.apitools.apigen.archetypecore.core.persistence;

import lombok.Data;

import java.util.List;

@Data
public class ApigenSearchResult<E> {

	private List<E> searchResult;
	private Long total;

	public ApigenSearchResult(List<E> searchResult, Long total) {
		this.searchResult = searchResult;
		this.total = total;
	}

	public ApigenSearchResult(List<E> searchResult) {
		this.searchResult = searchResult;
	}
}
