package org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic;

import java.util.Map;

public interface GenerationStrategy<C extends Context> {
    GeneratorsAbstractFactory<C> getGeneratorsFactory();
    C getContext(Map<String, Object> globalConfig);
}
