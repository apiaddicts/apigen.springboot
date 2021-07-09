package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.executor;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EntityLookup {

	private Map<String, Map<Serializable, ApigenAbstractPersistable>> entitiesByPath = new HashMap<>();

	public ApigenAbstractPersistable register(ApigenAbstractPersistable entity, String path) {
		entitiesByPath.putIfAbsent(path, new HashMap<>());
		entitiesByPath.get(path).putIfAbsent(entity.getId(), entity);
		return entitiesByPath.get(path).get(entity.getId());
	}

	public ApigenAbstractPersistable get(ApigenAbstractPersistable entity, String path) {
		return get(path, entity.getId());
	}

	public ApigenAbstractPersistable get(String path, Serializable id) {
		return entitiesByPath.get(path).get(id);
	}
}
