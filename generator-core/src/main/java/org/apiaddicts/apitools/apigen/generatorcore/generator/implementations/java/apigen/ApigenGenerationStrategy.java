package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen;

import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.GenerationStrategy;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.GeneratorsAbstractFactory;
import org.springframework.util.Assert;

import java.util.Map;

public class ApigenGenerationStrategy implements GenerationStrategy<ApigenContext> {
    @Override
    public GeneratorsAbstractFactory<ApigenContext> getGeneratorsFactory() {
        return new ApigenGeneratorsFactory();
    }

    @Override
    public ApigenContext getContext(Map<String, Object> globalConfig) {
        String parentGroup = (String) globalConfig.get("parentGroup");
        Assert.notNull(parentGroup, "parentGroup global config property is required");
        String parentArtifact = (String) globalConfig.get("parentArtifact");
        Assert.notNull(parentGroup, "parentArtifact global config property is required");
        String parentVersion = (String) globalConfig.get("parentVersion");
        Assert.notNull(parentGroup, "parentVersion global config property is required");
        return new ApigenContext(parentGroup, parentArtifact, parentVersion);
    }
}
