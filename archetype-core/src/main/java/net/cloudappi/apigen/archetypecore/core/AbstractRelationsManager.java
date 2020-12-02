package net.cloudappi.apigen.archetypecore.core;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public class AbstractRelationsManager<E> {

	@Transactional(propagation = Propagation.MANDATORY)
	public void createOrRetrieveRelations(E entity) {
		// Override if required
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void updateRelations(E persistedEntity, E entity, Set<String> fields) {
		// Override if required
	}
}
