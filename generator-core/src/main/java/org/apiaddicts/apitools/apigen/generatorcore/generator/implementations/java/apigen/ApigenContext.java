package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen;

import lombok.Getter;
import lombok.Setter;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

@Setter
@Getter
public class ApigenContext extends JavaContext {

    public ApigenContext(String parentGroup, String parentArtifact, String parentVersion) {
        super(parentGroup, parentArtifact, parentVersion);
    }
}
