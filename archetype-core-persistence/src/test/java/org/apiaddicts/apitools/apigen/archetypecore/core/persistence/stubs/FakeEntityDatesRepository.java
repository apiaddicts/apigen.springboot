package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.stubs;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FakeEntityDatesRepository extends ApigenRepository<FakeEntityDates, Long> {
}
