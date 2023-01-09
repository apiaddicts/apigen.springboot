package org.apiaddicts.apitools.apigen.archetypecore.core;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenRepository;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.EntityIdAlreadyInUseException;

import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Transactional
@SuppressWarnings("squid:S1192")
@Deprecated
public abstract class AbstractCrudLegacyService<E extends ApigenAbstractPersistable<K>, K extends Serializable, R extends ApigenRepository<E, K>>
		extends AbstractReadService<E, K, R> {

	protected final AbstractRelationsLegacyManager<E> relationsManager;
	protected final ApigenMapper<E> mapper;

	public AbstractCrudLegacyService(R repository, @Nullable AbstractRelationsLegacyManager<E> relationsManager, @Nullable ApigenMapper<E> mapper) {
		super(repository);
		this.relationsManager = relationsManager;
		this.mapper = mapper;
	}

	@Transactional
	public E create(E entity) {
		Assert.notNull(entity, "The argument entity cannot be null.");
		preCreate(entity);
		preCreateBeforeManageRelations(entity);
		if (nonNull(relationsManager)) relationsManager.createOrRetrieveRelations(entity);
		preCreateAfterManageRelations(entity);
		if(nonNull(entity.getId())) {
			Optional<E> existingId = getOne(entity.getId());
			if(existingId.isPresent()) throw new EntityIdAlreadyInUseException(entity.getId(),clazz);
		}
		entity = save(entity);
		postCreate(entity);
		return entity;
	}

	@Transactional
	public Set<E> create(Set<E> entities) {
		Assert.notNull(entities, "The argument entities cannot be null.");
		return entities.stream().map(this::create).collect(Collectors.toSet());
	}

	@Transactional
	public List<E> create(List<E> entities) {
		Assert.notNull(entities, "The argument entities cannot be null.");
		return entities.stream().map(this::create).collect(Collectors.toList());
	}

	@Transactional
	public E update(K id, E entity) {
		return update(id, entity, null);
	}

	@Transactional
	public E update(K id, E entity, Set<String> fields) {
		Assert.notNull(id, "The argument id cannot be null.");
		Assert.notNull(entity, "The argument entity cannot be null.");
		E persistedEntity = safeGetOne(id);
		return update(persistedEntity, entity, fields);
	}

	@Transactional
	public E update(E persistedEntity, E entity) {
		return update(persistedEntity, entity, null);
	}

	@Transactional
	public E update(E persistedEntity, E entity, Set<String> fields) {
		Assert.notNull(persistedEntity, "The argument persistedEntity cannot be null.");
		Assert.notNull(entity, "The argument entity cannot be null.");
		preUpdate(persistedEntity, entity, fields);
		updateData(persistedEntity, entity, fields);
		persistedEntity = save(persistedEntity);
		postUpdate(persistedEntity, entity, fields);
		return persistedEntity;
	}

	protected E updateData(E persistedEntity, E entity) {
		return updateData(persistedEntity, entity, null);
	}

	protected E updateData(E persistedEntity, E entity, Set<String> fields) {
		updateBasicData(persistedEntity, entity, fields);
		preUpdateBeforeManageRelations(persistedEntity, entity, fields);
		if (nonNull(relationsManager)) relationsManager.updateRelations(persistedEntity, entity, fields);
		preUpdateAfterManageRelations(persistedEntity, entity, fields);
		return persistedEntity;
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public E updateBasicData(E persistedEntity, E entity) {
		return updateBasicData(persistedEntity, entity, null);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public E updateBasicData(E persistedEntity, E entity, Set<String> fields) {
		preUpdateAfterManageBasicData(persistedEntity, entity, fields);
		K id = persistedEntity.getId();
		updateBasicDataPartially(persistedEntity, entity, fields);
		persistedEntity.setId(id);
		preUpdateBeforeManageBasicData(persistedEntity, entity, fields);
		return persistedEntity;
	}

	protected abstract void updateBasicDataPartially(E persistedEntity, E entity, Set<String> fields);

	@Transactional
	public Set<E> update(Set<E> persistedEntities, Collection<E> entities) {
		Assert.notNull(persistedEntities, "The argument persistedEntities cannot be null.");
		Assert.notNull(entities, "The argument entities cannot be null.");

		updateCollection(persistedEntities, entities);

		return persistedEntities;
	}

	@Transactional
	public List<E> update(List<E> persistedEntities, Collection<E> entities) {
		Assert.notNull(persistedEntities, "The argument persistedEntities cannot be null.");
		Assert.notNull(entities, "The argument entities cannot be null.");

		updateCollection(persistedEntities, entities);

		return persistedEntities;
	}

	private void updateCollection(Collection<E> persistedEntities, Collection<E> entities) {
		Map<K, E> entitiesWithId = entities.stream()
				.filter(entity -> entity.getId() != null)
				.collect(Collectors.toMap(E::getId, Function.identity()));

		for (E persistedEntity : persistedEntities) {
			K id = persistedEntity.getId();
			if (entitiesWithId.containsKey(id)) {
				update(persistedEntity, entitiesWithId.get(id));
				entitiesWithId.remove(id);
			}
		}
	}

	protected E save(E entity) {
		Assert.notNull(entity, "The argument entity cannot be null.");
		preSave(entity);
		entity = this.repository.save(entity);
		postSave(entity);
		return entity;
	}

	@Transactional
	public void delete(K id) {
		Assert.notNull(id, "The argument id cannot be null.");
		E persistedEntity = safeGetOne(id);
		delete(persistedEntity);
	}

	@Transactional
	public void delete(E entity) {
		Assert.notNull(entity, "The argument entity cannot be null.");
		preDelete(entity);
		this.repository.delete(entity);
		postDelete(entity);
	}

	@Transactional
	public void delete(Collection<E> entities) {
		Assert.notNull(entities, "The argument entities cannot be null.");
		entities.forEach(this::delete);
	}

	protected void preCreate(E entity) {
		// Override if required
	}

	protected void preCreateBeforeManageRelations(E entity) {
		// Override if required
	}

	protected void preCreateAfterManageRelations(E entity) {
		// Override if required
	}

	protected void postCreate(E entity) {
		// Override if required
	}

	protected void preUpdate(E persistedEntity, E entity, Set<String> fields) {
		// Override if required
	}

	protected void preUpdateAfterManageBasicData(E persistedEntity, E entity, Set<String> fields) {
		// Override if required
	}

	protected void preUpdateBeforeManageBasicData(E persistedEntity, E entity, Set<String> fields) {
		// Override if required
	}

	protected void preUpdateAfterManageRelations(E persistedEntity, E entity, Set<String> fields) {
		// Override if required
	}

	protected void preUpdateBeforeManageRelations(E persistedEntity, E entity, Set<String> fields) {
		// Override if required
	}

	protected void postUpdate(E persistedEntity, E entity, Set<String> fields) {
		// Override if required
	}

	protected void preDelete(E entity) {
		// Override if required
	}

	protected void postDelete(E entity) {
		// Override if required
	}

	protected void preSave(E entity) {
		// Override if required
	}

	protected void postSave(E entity) {
		// Override if required
	}
}