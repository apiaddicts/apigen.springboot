package org.apiaddicts.apitools.apigen.archetypecore.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class RelationalErrors implements Serializable {
    private Map<Class, Set<Serializable>> errors = new HashMap<>();

    public void register(Class clazz, Serializable id) {
        if (!errors.containsKey(clazz)) {
            errors.put(clazz, new HashSet<>());
        }
        errors.get(clazz).add(id);
    }

    public void merge(RelationalErrors unknownRelations) {
        for (Map.Entry<Class, Set<Serializable>> entry : unknownRelations.errors.entrySet()) {
            Class clazz = entry.getKey();
            if (!errors.containsKey(clazz)) {
                errors.put(clazz, new HashSet<>());
            }
            errors.get(clazz).addAll(entry.getValue());
        }
    }

    public boolean isEmpty() {
        return errors.isEmpty();
    }

    public List<Error> getErrors() {
        return errors.entrySet().stream().flatMap(e -> e.getValue().stream().map(v -> new Error(e.getKey(), v))).collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    public static class Error {
        private Class clazz;
        private Serializable id;
    }
}
