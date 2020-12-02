package net.cloudappi.apigen.archetypecore.core.persistence;

import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;
import org.springframework.lang.Nullable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

@MappedSuperclass
public abstract class ApigenAbstractPersistable<K extends Serializable> implements Persistable<K> {
	private static final long serialVersionUID = -5554308939380869754L;

	public ApigenAbstractPersistable() {
	}

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
			return null != this.getId() && this.getId().equals(that.getId());
		}
	}

	public int hashCode() {
		int hashCode = 17;
		if (this.getId() != null) hashCode += this.getId().hashCode() * 31;
		return hashCode;
	}
}
