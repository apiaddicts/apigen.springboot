package org.apiaddicts.apitools.apigen.archetypecore.core;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenRepository;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenSearch;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenSearchResult;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AbstractReadServiceTest {

	private static final List<String> SELECT = new ArrayList<>();
	private static final List<String> EXCLUDE = new ArrayList<>();
	private static final List<String> EXPAND = new ArrayList<>();
	private static final Filter FILTER = new Filter();
	private static final List<String> ORDER_BY = new ArrayList<>();
	private static final Integer INIT = 10;
	private static final Integer LIMIT = 20;
	private static final Boolean TOTAL = true;

	private EntityRepository repository;
	private ReadService service;

	@BeforeEach
	void beforeEach() {
		repository = mock(EntityRepository.class);
		service = new ReadService(repository);
	}

	@Test
	void givenMaybePersistedEntity_whenGetOneOptional_thenSuccess() {
		Optional<Entity> optional = Optional.empty();
		when(repository.findById(anyLong())).thenReturn(optional);
		Optional<Entity> result = service.getOne(1L);
		assertSame(optional, result);
	}

	@Test
	void givenPersistedEntities_whenGetAll_thenSuccess() {
		List<Entity> entities = new ArrayList<>();
		when(repository.findAll()).thenReturn(entities);
		List<Entity> result = service.getAll();
		assertSame(entities, result);
	}

	@Test
	void givenPersistedEntities_whenGetAllPaged_thenSuccess() {
		PageImpl<Entity> page = new PageImpl<>(new ArrayList<>());
		when(repository.findAll(any(Pageable.class))).thenReturn(page);
		Page<Entity> result = service.getAll(mock(Pageable.class));
		assertSame(page, result);
	}

	@Test
	void givenPersistedEntities_whenGetAllSorted_thenSuccess() {
		List<Entity> entities = new ArrayList<>();
		when(repository.findAll(any(Sort.class))).thenReturn(entities);
		List<Entity> result = service.getAll(mock(Sort.class));
		assertSame(entities, result);
	}

	@Test
	void givenPersistedEntities_whenGetAllById_thenSuccess() {
		List<Entity> entities = new ArrayList<>();
		when(repository.findAllById(any(Iterable.class))).thenReturn(entities);
		List<Entity> result = service.getAll(new ArrayList<>());
		assertSame(entities, result);
	}

	@Test
	void givenPersistedEntities_whenSearchAll_thenSuccess() {
		ApigenSearchResult searchResult = mock(ApigenSearchResult.class);
		when(repository.search(any(ApigenSearch.class))).thenReturn(searchResult);
		ArgumentCaptor<ApigenSearch> captor = ArgumentCaptor.forClass(ApigenSearch.class);
		ApigenSearchResult result = service.search(SELECT, EXCLUDE, EXPAND, FILTER, ORDER_BY, INIT, LIMIT, TOTAL);
		verify(repository).search(captor.capture());
		assertSame(searchResult, result);
		assertApigenSearch(SELECT, EXCLUDE, EXPAND, FILTER, ORDER_BY, INIT, LIMIT, TOTAL, captor.getValue());
	}

	@Test
	void givenPersistedEntity_whenSearchOne_thenSuccess() {
		Entity entity = new Entity(1L);
		when(repository.searchById(anyLong(), any(ApigenSearch.class))).thenReturn(Optional.of(entity));
		ArgumentCaptor<ApigenSearch> captor = ArgumentCaptor.forClass(ApigenSearch.class);
		Entity result = service.search(1L, SELECT, EXCLUDE, EXPAND);
		verify(repository).searchById(anyLong(), captor.capture());
		assertSame(entity, result);
		assertApigenSearch(SELECT, EXCLUDE, EXPAND, null, null, null, null, null, captor.getValue());
	}

	@Test
	void givenPersistedEntity_whenSearchOne_thenException() {
		when(repository.searchById(anyLong(), any(ApigenSearch.class))).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> service.search(1L, SELECT, EXCLUDE, EXPAND));
	}

	@Test
	void givenPersistedEntity_whenSafeGetOne_thenSuccess() {
		Entity entity = new Entity(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(entity));

		Entity entityReturned = service.safeGetOne(1L);
		verify(repository).findById(1L);
		assertEquals(entityReturned, entity);
	}

	@Test
	void givenNonPersistedEntity_whenSafeGetOne_thenError() {
		assertThrows(EntityNotFoundException.class, () -> service.safeGetOne(1L));
	}

	@Test
	void givenNonPersistedEntity_whenSafeGetOne_thenInvalidId() {
		assertThrows(IllegalArgumentException.class, () -> service.safeGetOne(null));
	}

	private void assertApigenSearch(List<String> select, List<String> exclude, List<String> expand, Filter filter, List<String> orderBy, Integer init, Integer limit, Boolean total, ApigenSearch search) {
		assertSame(select, search.getSelect());
		assertSame(exclude, search.getExclude());
		assertSame(expand, search.getExpand());
		assertSame(filter, search.getFilter());
		assertSame(orderBy, search.getOrderBy());
		if (init != null && limit != null) {
			assertSame(init, search.getPagination().getInit());
			assertSame(limit, search.getPagination().getLimit());
		}
		if (total != null) {
			assertSame(total, search.getTotal());
		}
	}

	private static class Entity extends ApigenAbstractPersistable<Long> {

		public Entity(Long id) {
			this.id = id;
		}

		Long id;

		@Override
		public Long getId() {
			return id;
		}

		@Override
		public void setId(Long id) {
			this.id = id;
		}

		@Override
		public boolean isReference() {
			return getId() != null;
		}
	}

	private interface EntityRepository extends ApigenRepository<Entity, Long> {
	}

	private static class ReadService extends AbstractReadService<Entity, Long, EntityRepository> {
		public ReadService(EntityRepository repository) {
			super(repository);
		}
	}
}