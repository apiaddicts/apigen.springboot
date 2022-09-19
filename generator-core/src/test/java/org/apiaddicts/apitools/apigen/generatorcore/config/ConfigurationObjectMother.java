package org.apiaddicts.apitools.apigen.generatorcore.config;

import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;

import java.util.List;

public class ConfigurationObjectMother {

    private ConfigurationObjectMother() {
        // Intentional blank
    }

    public static Configuration create() {
        return create(null, null);
    }

    public static Configuration create(List<Entity> entities, List<Controller> controllers) {
        Configuration c = new Configuration();
        c.setName("name");
        c.setDescription("description");
        c.setGroup("the.group");
        c.setArtifact(("artifact"));
        c.setBasePackage("the.group.artifact");
        c.setVersion(("1.0.0"));
        c.setPartial(false);
        c.setEntities(entities);
        c.setControllers(controllers);
        return c;
    }
}
