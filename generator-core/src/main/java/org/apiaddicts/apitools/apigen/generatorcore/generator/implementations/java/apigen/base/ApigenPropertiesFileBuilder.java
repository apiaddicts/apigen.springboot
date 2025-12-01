package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.JavaPropertiesFileBuilder;

@Slf4j
public class ApigenPropertiesFileBuilder<C extends ApigenContext> extends JavaPropertiesFileBuilder<C> {

    public ApigenPropertiesFileBuilder(C ctx, Configuration cfg) {
        super("application.properties", ctx, cfg);
    }

    @Override
    protected void init() {
        addProperty("spring.application.name", "@name@");
        addProperty("spring.profiles.active", "dev");
        addProperty("logging.level.root", "info");
        addProperty("spring.jackson2.serialization.fail_on_empty_beans", "false");
        addProperty("spring.jackson2.default-property-inclusion", "NON_NULL");
        addProperty("spring.main.allow-circular-references", "true");
        addProperty("spring.mvc.throw-exception-if-no-handler-found", "true");
        addProperty("spring.web.resources.add-mappings", "false");
        addProperty("management.endpoints.access.default", "none");
        addProperty("management.endpoint.health.access", "read-only");
        addStandardResponseProperties();
    }

    protected void addStandardResponseProperties() {
        ArrayNode operations = cfg.getStandardResponseOperations();
        if (operations == null) return;
        for (int i = 0; i < operations.size(); i++) {
            addProperty("apigen.standard-response.operations["+i+"]", operations.get(i).toString());
        }
    }
}
