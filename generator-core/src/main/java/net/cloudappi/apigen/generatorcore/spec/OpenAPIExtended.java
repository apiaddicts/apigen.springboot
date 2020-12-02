package net.cloudappi.apigen.generatorcore.spec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Getter;
import net.cloudappi.apigen.generatorcore.spec.components.ApigenBinding;
import net.cloudappi.apigen.generatorcore.spec.components.ApigenModel;
import net.cloudappi.apigen.generatorcore.spec.components.ApigenProject;

import java.util.HashMap;
import java.util.Map;

import static net.cloudappi.apigen.generatorcore.spec.components.Extensions.*;

@Getter
public class OpenAPIExtended {

    private OpenAPI openAPI;
    private ApigenProject project;
    private Map<String, ApigenModel> models;
    private Map<PathItem, ApigenBinding> pathBindings;
    private Map<String, PathItem> paths;

    private ObjectMapper mapper;

    public OpenAPIExtended(OpenAPI openAPI) {
        initializeMapper();
        this.openAPI = openAPI;
        this.project = getProject(openAPI);
        this.models = getModels(openAPI);
        this.pathBindings = getBindings(openAPI);
        this.paths = openAPI.getPaths();
    }

    public Map<String, Schema> getSchemas() {
        return this.openAPI.getComponents().getSchemas();
    }

    private ApigenProject getProject(OpenAPI openAPI) {
        return mapper.convertValue(openAPI.getExtensions().get(PROJECT), ApigenProject.class);
    }

    private Map<String, ApigenModel> getModels(OpenAPI openAPI) {
        if (openAPI.getComponents() == null
                || openAPI.getComponents().getExtensions() == null
                || openAPI.getComponents().getExtensions().get(MODELS) == null) {
            return new HashMap<>();
        }
        return mapper.convertValue(openAPI.getComponents().getExtensions().get(MODELS), new TypeReference<HashMap<String, ApigenModel>>() {});
    }

    private Map<PathItem, ApigenBinding> getBindings(OpenAPI openAPI) {
        return openAPI.getPaths().values().stream()
                .collect(HashMap::new, (m, v) -> m.put(v, getBinding(v)), HashMap::putAll);
    }

    private ApigenBinding getBinding(PathItem pathItem) {
        if (pathItem.getExtensions() == null || pathItem.getExtensions().get(BINDING) == null) return null;
        return mapper.convertValue(pathItem.getExtensions().get(BINDING), ApigenBinding.class);
    }

    private void initializeMapper() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
    }

}
