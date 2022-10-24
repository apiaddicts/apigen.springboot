package org.apiaddicts.apitools.apigen.archetypecore.core.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public abstract class ApigenAbstractAudited<K extends Serializable, U extends ApigenAbstractPersistable> extends ApigenAbstractTimeAudited<K> { // NOSONAR

	@CreatedBy
	@ManyToOne
	@JoinColumn(name = "created_by")
	private U createdBy;

	@ManyToOne
	@JoinColumn(name = "last_updated_by")
	@LastModifiedBy
	private U lastUpdatedBy;
}
