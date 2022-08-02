package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttributeData<T> {
    public boolean isCollection;
    public String relatedEntity;
    public boolean isOwned;
    public boolean isId;
    public T type;
}
