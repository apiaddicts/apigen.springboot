package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.stubs;

import lombok.Getter;
import lombok.Setter;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "entity_dates")
public class FakeEntityDates extends ApigenAbstractPersistable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long oneId;

	private LocalDate date;
	private OffsetDateTime dateTime;



	@Override
	public Long getId() {
		return oneId;
	}

	@Override
	public void setId(Long id) {
		this.oneId = id;
	}

	@Override
	public boolean isReference() {
		return getId() != null && date == null && dateTime == null;
	}
}
