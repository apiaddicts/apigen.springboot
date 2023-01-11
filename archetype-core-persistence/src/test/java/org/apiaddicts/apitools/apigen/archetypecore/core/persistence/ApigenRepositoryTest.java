package org.apiaddicts.apitools.apigen.archetypecore.core.persistence;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.Filter;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.pagination.Pagination;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.stubs.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.FilterUtils.gt;
import static org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.FilterUtils.lt;
import static org.apiaddicts.apitools.apigen.archetypecore.core.persistence.filter.FilterUtils.regexp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {"spring.sql.init.mode=always"})
class ApigenRepositoryTest {

	private static final List<String> EMPTY = Collections.emptyList();

	private static final Long TOTAL_DATES_ENTITIES = 2L;
	private static final Long PAST_DATE_ENTITY_ID = 1L;
	private static final Long FUTURE_DATE_ENTITY_ID = 2L;

	private static final Long CENTER_NODE_ENTITY_ID = 1L;
	private static final Long NORTH_NODE_ENTITY_ID = 2L;

	@Autowired FakeEntityDatesRepository datesRepository;
	@Autowired FakeEntityNodeRepository nodeRepository;
	@Autowired FakeEntityBigEntityRepository bigEntityRepository;

	@Test
	void givenPersistedRecords_whenCount_thenCount() {
		ApigenSearch search = new ApigenSearch(EMPTY, EMPTY, EMPTY, null, EMPTY, null, false);
		long result = datesRepository.count(search);
		assertEquals(TOTAL_DATES_ENTITIES, result);
	}

	@Test
	void givenPersistedRecords_whenSearchedPaginated_thenSearchAll() {
		Pagination pagination = new Pagination(0, 10);
		ApigenSearch search = new ApigenSearch(EMPTY, EMPTY, EMPTY, null, EMPTY, pagination, true);
		ApigenSearchResult<FakeEntityDates> result = datesRepository.search(search);
		assertEquals(TOTAL_DATES_ENTITIES, result.getSearchResult().size());
		assertEquals(TOTAL_DATES_ENTITIES, result.getTotal());
	}

	@Test
	void givenPersistedRecords_whenSearchedPaginatedSmall_thenSearchPage() {
		Pagination pagination = new Pagination(0, 1);
		List<String> orderBy = Collections.singletonList("+oneId");
		ApigenSearch search = new ApigenSearch(EMPTY, EMPTY, EMPTY, null, orderBy, pagination, true);
		ApigenSearchResult<FakeEntityDates> result = datesRepository.search(search);
		assertEquals(1, result.getSearchResult().size());
		assertEquals(PAST_DATE_ENTITY_ID, result.getSearchResult().get(0).getId());
		assertEquals(TOTAL_DATES_ENTITIES, result.getTotal());
	}

	@Test
	void givenPersistedRecords_whenSearchedPaginatedSmallWithoutOrder_thenSearchPageOrderById() {
		Pagination pagination = new Pagination(0, 1);
		ApigenSearch search = new ApigenSearch(EMPTY, EMPTY, EMPTY, null, null, pagination, true);
		ApigenSearchResult<FakeEntityDates> result = datesRepository.search(search);
		assertEquals(1, result.getSearchResult().size());
		assertEquals(PAST_DATE_ENTITY_ID, result.getSearchResult().get(0).getId());
		assertEquals(TOTAL_DATES_ENTITIES, result.getTotal());
	}

	@Test
	void givenPersistedRecords_whenSearchedPaginatedSmallWithEmptyOrder_thenSearchPageOrderById() {
		Pagination pagination = new Pagination(0, 1);
		ApigenSearch search = new ApigenSearch(EMPTY, EMPTY, EMPTY, null, EMPTY, pagination, true);
		ApigenSearchResult<FakeEntityDates> result = datesRepository.search(search);
		assertEquals(1, result.getSearchResult().size());
		assertEquals(PAST_DATE_ENTITY_ID, result.getSearchResult().get(0).getId());
		assertEquals(TOTAL_DATES_ENTITIES, result.getTotal());
	}

	@Test
	void givenPersistedRecords_whenSearchedDatetimeFilter_thenSuccess() {
		Pagination pagination = new Pagination(0, 20);
		Filter filter = gt("dateTime", "2020-02-02T12:00:00.000+01:00");
		ApigenSearch search = new ApigenSearch(EMPTY, EMPTY, EMPTY, filter, EMPTY, pagination, false);
		ApigenSearchResult<FakeEntityDates> result = datesRepository.search(search);
		assertEquals(1, result.getSearchResult().size());
		assertEquals(FUTURE_DATE_ENTITY_ID, result.getSearchResult().get(0).getId());
	}

	@Test
	void givenPersistedRecords_whenSearchedDateFilter_thenSuccess() {
		Pagination pagination = new Pagination(0, 20);
		Filter filter = lt("date", "2020-02-02");
		ApigenSearch search = new ApigenSearch(EMPTY, EMPTY, EMPTY, filter, EMPTY, pagination, false);
		ApigenSearchResult<FakeEntityDates> result = datesRepository.search(search);
		assertEquals(1, result.getSearchResult().size());
		assertEquals(PAST_DATE_ENTITY_ID, result.getSearchResult().get(0).getId());
	}

	@Test
	void givenPersistedRecords_whenExpand_thenSuccess() {
		List<String> expand = Arrays.asList("children", "children.children");
		ApigenSearch search = new ApigenSearch(EMPTY, EMPTY, expand);
		FakeEntityNode center = nodeRepository.searchById(CENTER_NODE_ENTITY_ID, search).orElse(null);
		assertNotNull(center);
		assertNotNull(center.getChildren());
		assertEquals(3, center.getChildren().size());
		FakeEntityNode north = center.getChildren().stream().filter(n -> NORTH_NODE_ENTITY_ID.equals(n.getId())).findFirst().orElse(null);
		assertNotNull(north);
		assertNotNull(north.getChildren());
		assertEquals(1, north.getChildren().size());
	}

	@Test
	void givenPersistedRecords_whenExpandAndSelect_thenSuccess() {
		List<String> expand = Arrays.asList("children", "children.children");
		List<String> select = Arrays.asList("id", "children.id", "children.children.id");
		ApigenSearch search = new ApigenSearch(select, EMPTY, expand);
		FakeEntityNode center = nodeRepository.searchById(CENTER_NODE_ENTITY_ID, search).orElse(null);
		assertNotNull(center);
		assertNotNull(center.getChildren());
		assertEquals(3, center.getChildren().size());
		FakeEntityNode north = center.getChildren().stream().filter(n -> NORTH_NODE_ENTITY_ID.equals(n.getId())).findFirst().orElse(null);
		assertNotNull(north);
		assertNotNull(north.getChildren());
		assertEquals(1, north.getChildren().size());
	}

	@Test
	void givenPersistedRecords_whenRegexSearch_thenSuccess() {
		Pagination pagination = new Pagination(0, 20);
		Filter filter = regexp("name", Pattern.compile("[N*]"));
		ApigenSearch search = new ApigenSearch(EMPTY, EMPTY, EMPTY, filter, EMPTY, pagination, false);
		List<FakeEntityNode> result = nodeRepository.search(search).getSearchResult();

		assertEquals(2, result.size());
		assertEquals("N", result.get(0).getName().substring(0, 1));
		assertEquals("N", result.get(1).getName().substring(0, 1));
	}

	@Test
	void givenBigAttributes_whenPersist_thenSuccess() {
		FakeEntityBigEntity e = new FakeEntityBigEntity();
		e.setBDec(BigDecimal.valueOf(.333333));
		e.setBInt(BigInteger.valueOf(1000));
		bigEntityRepository.save(e);

		assertNotNull(e.getId());

		bigEntityRepository.delete(e);
	}

	@Test
	void givenBigAttributes_whenSearch_thenSuccess() {
		Filter filter = lt("bDec", "0.3333");
		ApigenSearch search = new ApigenSearch(EMPTY, EMPTY, EMPTY, filter, EMPTY, null, false);
		ApigenSearchResult<FakeEntityBigEntity> result = bigEntityRepository.search(search);
		assertEquals(2, result.getSearchResult().size());
	}
}
