package org.apiaddicts.apitools.apigen.archetypecore.core;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenRepository;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenSearch;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenSearchResult;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.pagination.Pagination;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.EntityNotFoundException;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Transactional
@SuppressWarnings("squid:S1192")
public abstract class AbstractReadService<E extends ApigenAbstractPersistable<K>, K extends Serializable, R extends ApigenRepository<E, K>> {

	protected R repository;
	protected Class<E> clazz;

	@SuppressWarnings({"unchecked", "ConstantConditions"})
	public AbstractReadService(R repository) {
		this.repository = repository;
		this.clazz = (Class<E>) GenericTypeResolver.resolveTypeArguments(getClass(), AbstractReadService.class)[0];
	}

	@Transactional(readOnly = true)
	public Optional<E> getOne(K id) {
		Assert.notNull(id, "The argument id cannot be null");
		return repository.findById(id);
	}

	@Transactional(readOnly = true)
	public E safeGetOne(K id) {
		Assert.notNull(id, "The argument id cannot be null");
		return this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, clazz));
	}

	@Transactional(readOnly = true)
	public List<E> getAll(Iterable<K> ids) {
		Assert.notNull(ids, "The argument ids cannot be null");
		return repository.findAllById(ids);
	}

	@Transactional(readOnly = true)
	public List<E> getAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public List<E> getAll(Sort sort) {
		Assert.notNull(sort, "The argument sort cannot be null");
		return repository.findAll(sort);
	}

	@Transactional(readOnly = true)
	public Page<E> getAll(Pageable pageable) {
		Assert.notNull(pageable, "The argument pageable cannot be null");
		return repository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public E search(K id, List<String> select, List<String> exclude, List<String> expand, Filter filter) {
		Assert.notNull(id, "The argument id cannot be null");
		ApigenSearch search = new ApigenSearch(select, exclude, expand, filter);
		return repository.searchById(id, search).orElseThrow(() -> new EntityNotFoundException(id, clazz));
	}

	@Transactional(readOnly = true)
	public ApigenSearchResult<E> search(List<String> select, List<String> exclude, List<String> expand, List<String> orderBy, Integer init, Integer limit, Boolean total, Filter filter) { // NOSONAR
		Pagination pagination = null;
		if (init != null && limit != null) {
			pagination = new Pagination(init, limit);
		}
		if (total == null) total = false;
		ApigenSearch search = new ApigenSearch(select, exclude, expand, orderBy, pagination, total, filter);
		search.setTotal(total);
		return repository.search(search);
	}

}
