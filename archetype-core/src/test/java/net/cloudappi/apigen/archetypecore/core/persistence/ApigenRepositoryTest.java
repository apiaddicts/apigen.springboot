package net.cloudappi.apigen.archetypecore.core.persistence;

import net.cloudappi.apigen.archetypecore.core.persistence.filter.Filter;
import net.cloudappi.apigen.archetypecore.core.persistence.filter.FilterOperation;
import net.cloudappi.apigen.archetypecore.core.persistence.filter.PropType;
import net.cloudappi.apigen.archetypecore.core.persistence.filter.Value;
import net.cloudappi.apigen.archetypecore.core.persistence.pagination.Pagination;
import net.cloudappi.apigen.archetypecore.core.persistence.stubs.FakeEntityDates;
import net.cloudappi.apigen.archetypecore.core.persistence.stubs.FakeEntityDatesRepository;
import net.cloudappi.apigen.archetypecore.core.persistence.stubs.FakeEntityNode;
import net.cloudappi.apigen.archetypecore.core.persistence.stubs.FakeEntityNodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {"spring.datasource.initialization-mode=always"})
class ApigenRepositoryTest {

	private static final List<String> EMPTY = Collections.emptyList();

	private static final Long TOTAL_DATES_ENTITIES = 2L;
	private static final Long PAST_DATE_ENTITY_ID = 1L;
	private static final Long FUTURE_DATE_ENTITY_ID = 2L;

	private static final Long CENTER_NODE_ENTITY_ID = 1L;
	private static final Long NORTH_NODE_ENTITY_ID = 2L;

	@Autowired FakeEntityDatesRepository datesRepository;
	@Autowired FakeEntityNodeRepository nodeRepository;

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
	void givenPersistedRecords_whenSearchedDatetimeFilter_thenSuccess() {
		Pagination pagination = new Pagination(0, 20);
		Filter filter = new Filter();
		filter.setOperation(FilterOperation.GT);
		Value value = new Value();
		value.setType(PropType.DATETIME);
		value.setProperty("dateTime");
		value.setValue("2020-02-02T12:00:00.000+01:00");
		filter.setValues(Collections.singletonList(value));
		ApigenSearch search = new ApigenSearch(EMPTY, EMPTY, EMPTY, filter, EMPTY, pagination, false);
		ApigenSearchResult<FakeEntityDates> result = datesRepository.search(search);
		assertEquals(1, result.getSearchResult().size());
		assertEquals(FUTURE_DATE_ENTITY_ID, result.getSearchResult().get(0).getId());
	}

	@Test
	void givenPersistedRecords_whenSearchedDateFilter_thenSuccess() {
		Pagination pagination = new Pagination(0, 20);
		Filter filter = new Filter();
		filter.setOperation(FilterOperation.LT);
		Value value = new Value();
		value.setType(PropType.DATE);
		value.setProperty("date");
		value.setValue("2020-02-02");
		filter.setValues(Collections.singletonList(value));
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
}