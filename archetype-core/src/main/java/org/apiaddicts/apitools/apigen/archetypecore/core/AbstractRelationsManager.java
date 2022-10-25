package org.apiaddicts.apitools.apigen.archetypecore.core;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrors;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrorsException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AbstractRelationsManager<E> {

    @Transactional(propagation = Propagation.MANDATORY)
    public void createOrRetrieveRelations(E entity) {
        // Override if required
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateRelations(E persistedEntity, E entity, Set<String> fields) {
        // Override if required
    }

    protected <T extends ApigenAbstractPersistable<K>, K extends Serializable> T retrieve(T entity, AbstractReadService<T, K, ?> service, RelationalErrors errors) {
        if (entity == null) return null;
        K id = entity.getId();
        T retrieved = (id == null) ? null : service.getOne(id).orElse(null);
        if (retrieved == null) errors.register(entity.getClass(), id);
        return retrieved;
    }

    protected <T extends ApigenAbstractPersistable<K>, K extends Serializable> Set<T> retrieve(Set<T> entities, AbstractReadService<T, K, ?> service, RelationalErrors errors) {
        if (entities == null) return new HashSet<>();
        return entities.stream().map(e -> retrieve(e, service, errors)).collect(Collectors.toSet());
    }

    protected <T extends ApigenAbstractPersistable<?>> T create(T entity, AbstractCrudService<T, ?, ?> service) {
        if (entity == null) return null;
        return service.create(entity);
    }

	protected <T extends ApigenAbstractPersistable<?>> void delete(T entity, AbstractCrudService<T, ?, ?> service) {
        if (entity == null) return;
		service.delete(entity);
	}

    protected <T extends ApigenAbstractPersistable<K>, K extends Serializable> T createOrRetrieve(T entity, AbstractCrudService<T, K, ?> service, RelationalErrors errors) {
        if (entity == null) return null;
        if (entity.isReference()) {
            return retrieve(entity, service, errors);
        } else {
            try {
                return create(entity, service);
            } catch (RelationalErrorsException e) {
                errors.merge(e.getRelationalErrors());
                return null;
            }
        }
    }

    protected <T extends ApigenAbstractPersistable<K>, K extends Serializable> Set<T> createOrRetrieve(Set<T> entities, AbstractCrudService<T, K, ?> service, RelationalErrors errors) {
        if (entities == null) return new HashSet<>();
        return entities.stream().map(e -> createOrRetrieve(e, service, errors)).collect(Collectors.toSet());
    }

    protected <T> void replace(Set<T> oldValues, Set<T> newValues) {
        oldValues.clear();
        oldValues.addAll(newValues);
    }
}
