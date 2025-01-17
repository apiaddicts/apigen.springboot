package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.executor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import jakarta.persistence.Tuple;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class TupleMapper {

	private static final String ROOT_PATH = null;

	private final Class<?> clazz;
	private final EntityInfo entityData;
	private final EntityLookup lookup;
	private List<EntityAttributesGroup> groups;
	private static final Map<Class<?>, Map<String, Field>> fieldCache = new ConcurrentHashMap<>();

	public TupleMapper(List<String> fields, EntityInfo entityData, Class<?> clazz) {
		this.clazz = clazz;
		this.entityData = entityData;
		this.lookup = new EntityLookup();
		initializeGroups(fields);
	}

	private void initializeGroups(List<String> fields) {
		List<OrderedField> orderedFields = getOrderedFields(fields);
		groups = getGroups(orderedFields);
	}

	private List<OrderedField> getOrderedFields(List<String> fields) {
		return IntStream.range(0, fields.size())
				.mapToObj(i -> new OrderedField(fields.get(i), i))
				.sorted(Comparator.comparing(OrderedField::getLevel).thenComparing((OrderedField::compareToField)))
				.collect(Collectors.toList());
	}

	private List<EntityAttributesGroup> getGroups(List<OrderedField> orderedFields) {
		List<EntityAttributesGroup> foundGroups = new ArrayList<>();
		HashMap<String, EntityAttributesGroup> attributesGroupByPath = new HashMap<>();

		orderedFields.forEach(f -> {
			String fieldName = f.getField();
			boolean isNested = fieldName.contains(".");
			String path = isNested ? fieldName.substring(0, fieldName.lastIndexOf('.')) : ROOT_PATH;
			if (!attributesGroupByPath.containsKey(path)) {
				String primaryKey = this.entityData.getIdAttribute(clazz, path);
				EntityAttributesGroup attributesGroup = new EntityAttributesGroup(path, primaryKey, new HashSet<>());
				foundGroups.add(attributesGroup);
				attributesGroupByPath.put(path, attributesGroup);
			}
			if (isNested) {
				String field = fieldName.substring(fieldName.lastIndexOf('.') + 1);
				attributesGroupByPath.get(path).getSimpleFields().add(new OrderedField(field, f.getIndex()));
			} else {
				attributesGroupByPath.get(ROOT_PATH).getSimpleFields().add(f);
			}
		});
		return foundGroups;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> map(List<Tuple> result) {
		List<Serializable> rootIds = new ArrayList<>();
		for (Tuple tuple : result) {
			Map<String, ApigenAbstractPersistable<?>> tupleEntities = new HashMap<>();
			for (EntityAttributesGroup group : groups) {
				String fieldId = group.getFieldId();
				ApigenAbstractPersistable<?> entity = createInstance(group.path, clazz);
				tupleEntities.put(group.path, entity);
				for (OrderedField orderedField : group.simpleFields) {
					if (isRootPath(group.path) && orderedField.field.equals(fieldId)) {
						rootIds.add((Serializable) tuple.get(orderedField.index));
					}
					setFieldValue(entity, orderedField.field, tuple.get(orderedField.index));
				}
				if (entity.getId() == null) continue;
				entity = lookup.register(entity, group.path);
				addToParent(entity, group.path, tupleEntities, lookup);
			}
		}
		return (List<T>) rootIds.stream().distinct().map(id -> lookup.get(ROOT_PATH, id)).collect(Collectors.toList());
	}

	/**
	 * Given a attribute path and a root class, returns an empty instance (all params to null) of the attribute class
	 *
	 * @param path  path of the attribute
	 * @param clazz class of the root class
	 * @return instance of the entity class of the attribute
	 */
	@SuppressWarnings("unchecked")
	private ApigenAbstractPersistable<? extends Serializable> createInstance(String path, Class<?> clazz) {
		try {
			return (ApigenAbstractPersistable<?>) entityData.getClass(path, clazz).getConstructor().newInstance();
		} catch (Exception e) {
			log.error("Attribute '{}' of entity {} can not be instanced", path, clazz, e);
			throw new RuntimeException(e);
		}
	}

	private void addToParent(ApigenAbstractPersistable<?> entity, String path, Map<String, ApigenAbstractPersistable<?>> instanced, EntityLookup lookup) {
		if (isRootPath(path)) return;
		boolean isNested = path.contains(".");
		String key = isNested ? path.substring(0, path.lastIndexOf('.')) : null;
		String attributeName = isNested ? path.substring(path.lastIndexOf('.') + 1) : path;
		ApigenAbstractPersistable<?> parent = instanced.get(key);
		ApigenAbstractPersistable<?> trueParent = lookup.get(parent, key);
		addIfAbsent(trueParent, attributeName, entity);
	}

	private void addIfAbsent(ApigenAbstractPersistable<?> entity, String attribute, Object value) {
		try {
			Object currentValue = PropertyUtils.getSimpleProperty(entity, attribute);
			AttributeInfo attributeInfo = entityData.getAttributeInfo(entity, attribute);
			if (attributeInfo.isCollection()) {
				addIfAbsentCollection(entity, attribute, value, currentValue, attributeInfo.isSet());
			} else {
				addIfAbsentObject(entity, attribute, value, currentValue);
			}
		} catch (Exception e) {
			log.error("Error adding entity to parent", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void addIfAbsentCollection(ApigenAbstractPersistable<?> entity, String attribute, Object value, Object currentValue, boolean isSet) {
		Collection<Object> collection = (Collection<Object>) currentValue;
		if (collection == null) {
			collection = isSet ? new HashSet<>() : new ArrayList<>();
			setFieldValue(entity, attribute, collection);
		}
		if (!collection.contains(value)) {
			collection.add(value);
		}
	}

	private void addIfAbsentObject(ApigenAbstractPersistable<?> entity, String attribute, Object value, Object currentValue) {
		if (currentValue == null) {
			setFieldValue(entity, attribute, value);
		}
	}

	private void setFieldValue(ApigenAbstractPersistable<?> instance, String field, Object value) {
		try {
			Class<?> clazz = instance.getClass();
			Map<String, Field> fields = fieldCache.computeIfAbsent(clazz, cls -> {
				Map<String, Field> map = new ConcurrentHashMap<>();
				for (Field f : cls.getDeclaredFields()) {
					f.setAccessible(true);
					map.put(f.getName(), f);
				}
				return map;
			});
			Field f = fields.get(field);
			if (f != null) {
				f.set(instance, value);
			} else {
				throw new NoSuchFieldException("Field " + field + " not found in " + clazz.getName());
			}
		} catch (Exception e) {
			log.warn("Attribute '{}' of entity {} can not be set", instance.getClass(), e);
		}
	}

	private boolean isRootPath(String path) {
		return path == null;
	}

	@Getter
	@AllArgsConstructor
	private static class EntityAttributesGroup {
		String path;
		String fieldId;
		Set<OrderedField> simpleFields;
	}

	@Getter
	@AllArgsConstructor
	private static class OrderedField {
		String field;
		int index;

		int compareToField(OrderedField object) {
			return field.compareTo(object.field);
		}

		int getLevel() {
			return StringUtils.countOccurrencesOf(field, ".");
		}
	}
}
