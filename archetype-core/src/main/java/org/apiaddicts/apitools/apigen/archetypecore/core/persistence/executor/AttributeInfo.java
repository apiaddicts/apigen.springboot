package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.executor;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;

import java.util.List;
import java.util.Set;

public class AttributeInfo {

	private Class type;
	private Class itemType;
	private boolean isBasic;
	private boolean isPrimaryKey;
	private boolean isEmbedded;
	private boolean isEnum;

	AttributeInfo(Class type, Class itemType, boolean isPrimaryKey, boolean isEmbedded) {
		this.type = type;
		this.itemType = itemType;
		isBasic = itemType.equals(type) && !ApigenAbstractPersistable.class.isAssignableFrom(type);
		this.isPrimaryKey = isPrimaryKey;
		this.isEmbedded = isEmbedded;
		this.isEnum = itemType.equals(type) && Enum.class.isAssignableFrom(type);
	}

	public boolean isBasic() {
		return isBasic;
	}

	public boolean isEnum() {
		return isEnum;
	}

	public boolean isList() {
		return List.class.equals(type);
	}

	public boolean isSet() {
		return Set.class.equals(type);
	}

	public boolean isCollection() {
		return isList() || isSet();
	}

	public Class getSimpleType() {
		return isCollection() ? itemType : type;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public boolean isEmbedded() {
		return isEmbedded;
	}
}
