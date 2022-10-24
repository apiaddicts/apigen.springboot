package org.apiaddicts.apitools.apigen.archetypecore.exceptions;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class EntityIdAlreadyInUseException extends RuntimeException {
	private Serializable id;
	private Class<?> clazz;

	public EntityIdAlreadyInUseException(Serializable id, Class<?> clazz) {
		this.id = id;
		this.clazz = clazz;
	}
}
