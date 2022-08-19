package org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;

import java.util.List;
import java.util.Map;

public interface GeneratorsAbstractFactory<C extends Context> {
    List<Generator> createDefault(C ctx, Configuration c);
    List<Generator> createNonPartial(C ctx, Configuration c, Map<String, Object> extensions);
}
