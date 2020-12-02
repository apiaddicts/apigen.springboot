package net.cloudappi.apigen.archetypecore.core.persistence.executor;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EntityInfo {

	private static EntityInfo INSTANCE = null;

	private Map<String, Map<String, AttributeInfo>> entityAttributesInfo = new HashMap<>();
	private Map<String, String> entityId = new HashMap<>();
	private Set<Class> embeddedIds = new HashSet<>();

	private EntityInfo(EntityManager em) {
		discoverEntities(em);
	}

	public static synchronized EntityInfo getInstance(EntityManager em) {
		if (INSTANCE == null) {
			INSTANCE = new EntityInfo(em);
		}
		return INSTANCE;
	}

	private void discoverEntities(EntityManager em) {
		for (EntityType<?> entityType : em.getMetamodel().getEntities()) {
			entityAttributesInfo.put(entityType.getName(), new HashMap<>());
			Map<String, AttributeInfo> params = entityAttributesInfo.get(entityType.getName());
			for (Attribute attribute : entityType.getAttributes()) {
				String name = attribute.getName();
				Class type = attribute.getJavaType();
				Class itemType = null;
				boolean isId = false;
				boolean isEmbedded = false;
				if (attribute instanceof Bindable) {
					itemType = ((Bindable) attribute).getBindableJavaType();
				}
				if (attribute instanceof SingularAttribute) {
					isId = ((SingularAttribute) attribute).isId();
				}
				if (isId) {
					entityId.put(entityType.getName(), name);
				}
				if(attribute.getPersistentAttributeType().equals(Attribute.PersistentAttributeType.EMBEDDED)){
					isEmbedded = true;
				}
				params.put(name, new AttributeInfo(type, itemType, isId, isEmbedded));
				if (isId && isEmbedded) embeddedIds.add(itemType);
			}
		}
	}

	/**
	 * Given a attribute path and a root class, returns the entity class of the attribute
	 *
	 * @param path  path of the attribute
	 * @param clazz class of the root class
	 * @return entity class of the attribute
	 */
	public Class getClass(String path, Class clazz) {
		if (path == null) return clazz;
		String[] pathParts = path.split("\\.");
		String className;
		for (String pathPart : pathParts) {
			className = clazz.getSimpleName();
			clazz = entityAttributesInfo.get(className).get(pathPart).getSimpleType();
		}
		return clazz;
	}

	public AttributeInfo getAttributeInfo(Object entity, String attributeName) {
		return getAttributeInfo(entity.getClass(), attributeName);
	}

	public AttributeInfo getAttributeInfo(Class entityClass, String attributeName) {
		return getAttributeInfo(entityClass.getSimpleName(), attributeName);
	}

	public AttributeInfo getAttributeInfo(String entityName, String attributeName) {
		return entityAttributesInfo.get(entityName).get(attributeName);
	}

	public List<String> getAttributes(Object entity) {
		return getAttributes(entity.getClass());
	}

	public List<String> getAttributes(Class entityClass) {
		return getAttributes(entityClass.getSimpleName());
	}

	public List<String> getAttributes(String entityName) {
		return new ArrayList<>(entityAttributesInfo.get(entityName).keySet());
	}

	public String getIdAttribute(Class entityClass) {
		return getIdAttribute(entityClass.getSimpleName());
	}

	public String getIdAttribute(String entityName) {
		String primaryKey = entityId.get(entityName);
		if (primaryKey == null) throw new IllegalArgumentException(String.format("Not found primary key of the entity '%s'", entityName));
		return primaryKey;
	}

	/**
	 * Given a root class and an attribute path, returns the name of the entity class of the attribute
	 *
	 * @param entityClass class of the root class
	 * @param path  path of the attribute
	 * @return entity class of the attribute
	 */
	public String getIdAttribute(Class entityClass, String path) {
		entityClass = getClass(path, entityClass);
		return getIdAttribute(entityClass);
	}

	public List<String> getBasicAttributes(Class entityClass) {
		return getAttributes(entityClass).stream()
				.filter(attribute -> {
					AttributeInfo info = getAttributeInfo(entityClass, attribute);
					return info.isBasic() || info.isEmbedded() || info.isEnum();
				})
				.collect(Collectors.toList());
	}

	/**
	 * Given a root class and an attribute path, returns the basic attributes of the entity class of the attribute
	 *
	 * @param entityClass class of the root class
	 * @param path  path of the attribute
	 * @return list of basic attributes
	 */
	public List<String> getBasicAttributes(Class entityClass, String path) {
		entityClass = getClass(path, entityClass);
		return getBasicAttributes(entityClass).stream()
				.map(field -> path.concat("." + field))
				.collect(Collectors.toList());
	}

	public boolean isEmbeddedId(Class idClass) {
		return embeddedIds.contains(idClass);
	}
}