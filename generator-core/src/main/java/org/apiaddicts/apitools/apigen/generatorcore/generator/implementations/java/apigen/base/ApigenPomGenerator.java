package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base;


import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.AbstractPomGenerator;

import java.util.Properties;

public class ApigenPomGenerator<C extends JavaContext> extends AbstractPomGenerator<C> {


    public ApigenPomGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    protected void addProperties(Model model) {
        super.addProperties(model);
        model.addProperty("h2.scope", "test");
    }

    @Override
    protected void addDependencies(Model model) {
        Dependency h2Dependency = new Dependency();
        h2Dependency.setGroupId("com.h2database");
        h2Dependency.setArtifactId("h2");
        h2Dependency.setScope("${h2.scope}");
        model.addDependency(h2Dependency);
    }

    @Override
    protected void addProfiles(Model model) {
        Profile h2Profile = new Profile();
        h2Profile.setId("h2");
        Properties properties = new Properties();
        properties.setProperty("h2.scope", "runtime");
        h2Profile.setProperties(properties);
        model.addProfile(h2Profile);
    }
}
