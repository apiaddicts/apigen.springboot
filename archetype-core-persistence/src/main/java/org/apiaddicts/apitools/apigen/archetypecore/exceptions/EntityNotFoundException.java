package org.apiaddicts.apitools.apigen.archetypecore.exceptions;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class EntityNotFoundException extends RuntimeException {
	private Serializable id;
	private Class<?> clazz;

	public EntityNotFoundException(Serializable id, Class<?> clazz) {
		this.id = id;
		this.clazz = clazz;
	}
}
