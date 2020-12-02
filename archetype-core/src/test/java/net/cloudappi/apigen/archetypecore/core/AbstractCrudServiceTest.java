package net.cloudappi.apigen.archetypecore.core;

import net.cloudappi.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;
import net.cloudappi.apigen.archetypecore.core.persistence.ApigenRepository;
import net.cloudappi.apigen.archetypecore.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AbstractCrudServiceTest {

	private EntityRepository repository;
	private RelationsManager relationsManager;
	private Mapper mapper;
	private CrudService service;

	@BeforeEach
	void beforeEach() {
		repository = mock(EntityRepository.class);
		relationsManager = mock(RelationsManager.class);
		mapper = mock(Mapper.class);
		service = new CrudService(repository, relationsManager, mapper);
	}

	@Test
	void givenNonPersistedEntity_whenSave_thenSuccess() {
		Entity entity = new Entity();
		when(repository.save(any(Entity.class))).then(returnsFirstArg());
		Entity result = service.create(entity);
		verify(relationsManager).createOrRetrieveRelations(any(Entity.class));
		assertSame(entity, result);
	}

	@Test
	void givenNonPersistedEntitySet_whenSave_thenSuccess() {
		Set<Entity> set = new HashSet<>(Arrays.asList(new Entity(), new Entity()));
		when(repository.save(any(Entity.class))).then(returnsFirstArg());
		Set<Entity> result = service.create(set);
		verify(relationsManager, times(2)).createOrRetrieveRelations(any(Entity.class));
		assertEquals(set, result);
	}

	@Test
	void givenNonPersistedEntityList_whenSave_thenSuccess() {
		List<Entity> list = new ArrayList<>(Arrays.asList(new Entity(), new Entity()));
		when(repository.save(any(Entity.class))).then(returnsFirstArg());
		List<Entity> result = service.create(list);
		verify(relationsManager, times(2)).createOrRetrieveRelations(any(Entity.class));
		assertEquals(list, result);
	}

	@Test
	void givenPersistedEntity_whenUpdateById_thenSuccess() {
		Entity entity = new Entity(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(any(Entity.class))).then(returnsFirstArg());
		Entity result = service.update(1L, entity);
		verify(relationsManager).updateRelations(any(Entity.class), any(Entity.class), any());
		verify(mapper).updateBasicData(any(Entity.class), any(Entity.class));
		verify(repository).save(entity);
		assertEquals(entity, result);
	}

	@Test
	void givenPersistedEntities_whenUpdateList_thenSuccess() {
		Entity one = new Entity(1L);
		Entity two = new Entity(2L);
		when(repository.findById(1L)).thenReturn(Optional.of(one));
		when(repository.findById(2L)).thenReturn(Optional.of(two));
		List<Entity> list = Arrays.asList(one, two);
		List<Entity> result = service.update(list, list);
		verify(relationsManager, times(2)).updateRelations(any(Entity.class), any(Entity.class), any());
		verify(mapper, times(2)).updateBasicData(any(Entity.class), any(Entity.class));
		verify(repository).save(one);
		verify(repository).save(two);
		assertEquals(list, result);
	}

	@Test
	void givenPersistedEntities_whenUpdateSet_thenSuccess() {
		Entity one = new Entity(1L);
		Entity two = new Entity(2L);
		when(repository.findById(1L)).thenReturn(Optional.of(one));
		when(repository.findById(2L)).thenReturn(Optional.of(two));
		Set<Entity> set = new HashSet<>(Arrays.asList(one, two));
		Set<Entity> result = service.update(set, set);
		verify(relationsManager, times(2)).updateRelations(any(Entity.class), any(Entity.class), any());
		verify(mapper, times(2)).updateBasicData(any(Entity.class), any(Entity.class));
		verify(repository).save(one);
		verify(repository).save(two);
		assertEquals(set, result);
	}

	@Test
	void givenPersistedEntity_whenDeleteById_thenSuccess() {
		Entity entity = new Entity(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		service.delete(1L);
		verify(repository).delete(entity);
	}

	@Test
	void givenNonPersistedEntity_whenDeleteById_thenError() {
		when(repository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> service.delete(1L));
	}

	@Test
	void givenPersistedEntity_whenDelete_thenSuccess() {
		Entity entity = new Entity(1L);
		service.delete(entity);
		verify(repository).delete(entity);
	}

	@Test
	void givenPersistedEntities_whenDeleteCollection_thenSuccess() {
		Entity one = new Entity(1L);
		Entity two = new Entity(2L);
		Collection<Entity> collection = Arrays.asList(one, two);
		service.delete(collection);
		verify(repository).delete(one);
		verify(repository).delete(two);
	}

	private static class Entity extends ApigenAbstractPersistable<Long> {

		public Entity() {
		}

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

	private abstract class RelationsManager extends AbstractRelationsManager<Entity> {

	}

	private abstract class Mapper implements ApigenMapper<Entity> {

	}

	private static class CrudService extends AbstractCrudService<Entity, Long, EntityRepository> {
		public CrudService(EntityRepository repository, AbstractRelationsManager<Entity> relationsManager, ApigenMapper<Entity> mapper) {
			super(repository, relationsManager, mapper);
		}

		@Override
		protected void updateBasicDataPartially(Entity persistedEntity, Entity entity, Set<String> fields) {
			if (fields == null) mapper.updateBasicData(entity, persistedEntity);
			else {
				if (fields.contains("id")) persistedEntity.setId(entity.getId());
			}
		}
	}
}