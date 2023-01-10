package org.apiaddicts.apitools.apigen.archetypecore.core.persistence;

import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;
import org.springframework.lang.Nullable;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.io.Serializable;

@MappedSuperclass
public abstract class ApigenAbstractPersistable<K extends Serializable> implements Persistable<K> {
	private static final long serialVersionUID = -5554308939380869754L;

	@Nullable
	@Transient
	public abstract K getId();

	@Transient
	public abstract void setId(@Nullable K id);

	@Transient
	public boolean isNew() {
		return null == this.getId();
	}

	@Transient
	public abstract boolean isReference();

	public String toString() {
		return String.format("Entity of type %s with id: %s", this.getClass().getName(), this.getId());
	}

	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (!this.getClass().equals(ProxyUtils.getUserClass(obj))) {
			return false;
		} else {
			ApigenAbstractPersistable<?> that = (ApigenAbstractPersistable) obj;
			K id = this.getId();
			return null != id && id.equals(that.getId());
		}
	}

	public int hashCode() {
		int hashCode = 17;
		K id = this.getId();
		if (id != null) hashCode += id.hashCode() * 31;
		return hashCode;
	}
}
