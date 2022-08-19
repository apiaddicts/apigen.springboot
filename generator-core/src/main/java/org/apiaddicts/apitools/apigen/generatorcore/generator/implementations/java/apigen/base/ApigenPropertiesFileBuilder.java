package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base.JavaPropertiesFileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.spec.components.ApigenProject;
import org.junit.jupiter.api.function.Executable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ApigenPropertiesFileBuilder<C extends ApigenContext> extends JavaPropertiesFileBuilder<C> {

    public ApigenPropertiesFileBuilder(C ctx, Configuration cfg, Map<String, Object> extensions) {
        super("application.properties", ctx, cfg, extensions);
    }

    @Override
    protected void init() {
        addProperty("spring.application.name", "@name@");
        addProperty("spring.profiles.active", "dev");
        addProperty("logging.level.root", "info");
        addProperty("spring.jackson.serialization.fail_on_empty_beans", "false");
        addProperty("spring.jackson.default-property-inclusion", "NON_NULL");
        addProperty("spring.main.allow-circular-references", "true");
        addProperty("spring.mvc.throw-exception-if-no-handler-found", "true");
        addProperty("spring.web.resources.add-mappings", "false");
        addProperty("management.endpoints.enabled-by-default", "false");
        addProperty("management.endpoint.health.enabled", "true");
        if(null != this.extensions && this.extensions.containsKey("x-apigen-project"))
            checkStandardResponseOperations();
    }

    private void checkStandardResponseOperations(){
        HashMap<String, Object> apigenProject = (HashMap<String, Object>) this.extensions.get("x-apigen-project");
        if(apigenProject.containsKey("standard-response-operations")){
            List<ApigenProject.StandardResponseOperation> standardResponseOperations =
                    (List<ApigenProject.StandardResponseOperation>) apigenProject.get("standard-response-operations");
            if(null != standardResponseOperations){
                for(int i = 0; i < standardResponseOperations.size(); i++){
                    String json = "";
                    ObjectWriter ow = new ObjectMapper().writer();
                    try {
                        json = ow.writeValueAsString(standardResponseOperations.get(i));
                        addProperty("apigen.standard-response.operations[" + i + "]", json);
                    } catch (JsonProcessingException e) {
                        log.error("ERROR loading standard-response-operations");
                    }
                }
            }
        }
    }
}