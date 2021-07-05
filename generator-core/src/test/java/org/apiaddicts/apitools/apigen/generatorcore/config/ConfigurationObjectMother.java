package org.apiaddicts.apitools.apigen.generatorcore.config;

public class ConfigurationObjectMother {

    private ConfigurationObjectMother() {
        // Intentional blank
    }

    public static Configuration createCompleteConfigurationWithoutEntitiesAndControllers() {
        Configuration c = new Configuration();
        c.setName("name");
        c.setDescription("description");
        c.setGroup("the.group");
        c.setArtifact(("artifact"));
        c.setVersion(("1.0.0"));
        c.setPartial(false);
        return c;
    }
}
