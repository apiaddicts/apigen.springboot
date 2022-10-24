package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.executor;

@FunctionalInterface
public interface ApigenConverter {
    Comparable convert(String value);
}
