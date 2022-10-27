package org.apiaddicts.apitools.apigen.archetypecore.core.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class ApigenAbstractTimeAudited<K extends Serializable> extends ApigenAbstractPersistable<K> { // NOSONAR
    private static final long serialVersionUID = -5554308939380869754L;

    @CreatedDate
    @Column(name = "created_at")
    protected Instant createdAt;

    @LastModifiedDate
    @Column(name = "last_updated_at")
    protected Instant lastUpdatedAt;
}
