package org.apiaddicts.apitools.apigen.generatorcore.config.extractors;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.spec.components.ApigenBinding;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ControllersExtractor {

    private final ParametersExtractor parametersExtractor;
    private final RequestExtractor requestExtractor;
    private final ResponseExtractor responseExtractor;

    public ControllersExtractor(Map<String, Schema<?>> schemas) {
        this.parametersExtractor = new ParametersExtractor();
        this.requestExtractor = new RequestExtractor(schemas);
        this.responseExtractor = new ResponseExtractor(schemas);
    }

    public List<Controller> getControllers(Map<String, PathItem> paths, Map<PathItem, ApigenBinding> bindings, List<Entity> rawEntities) {
        List<Controller> controllers = getEntityControllers(paths, bindings, rawEntities);
        List<Controller> otherControllers = getOtherControllers(paths, bindings);
        controllers.addAll(otherControllers);
        return controllers;
    }

    private List<Controller> getEntityControllers(Map<String, PathItem> paths, Map<PathItem, ApigenBinding> bindings, List<Entity> rawEntities) {
        Map<String, Entity> entities = rawEntities.stream().collect(Collectors.toMap(Entity::getName, e -> e));
        Map<String, Map<String, PathItem>> pathsByEntity = new HashMap<>();
        for (Map.Entry<String, PathItem> path : paths.entrySet()) {
            ApigenBinding binding = bindings.get(path.getValue());
            if (binding == null) continue;
            Entity entity = entities.get(binding.getModel());
            if (entity == null) {
                log.warn("Model not found {} , path {} ignored", binding.getModel(), path.getKey());
            } else {
                String entityName = entity.getName();
                pathsByEntity.putIfAbsent(entityName, new HashMap<>());
                pathsByEntity.get(entityName).put(path.getKey(), path.getValue());
            }
        }
        return pathsByEntity.entrySet().stream()
                .map(entry -> createController(entry.getValue(), entities.get(entry.getKey())))
                .collect(Collectors.toList());
    }

    private List<Controller> getOtherControllers(Map<String, PathItem> paths, Map<PathItem, ApigenBinding> bindings) {
        Map<String, PathItem> otherPaths = new HashMap<>();
        for (Map.Entry<String, PathItem> path : paths.entrySet()) {
            ApigenBinding binding = bindings.get(path.getValue());
            if (binding == null) {
                otherPaths.put(path.getKey(), path.getValue());
            }
        }
        return group(otherPaths).stream()
                .map(this::createController)
                .collect(Collectors.toList());
    }

    private List<Map<String, PathItem>> group(Map<String, PathItem> paths) {
        Map<String, Map<String, PathItem>> groups = new HashMap<>();
        paths.forEach((k, v) -> {
            String key = k.split("/")[1];
            if (!groups.containsKey(key)) {
                groups.put(key, new HashMap<>());
            }
            groups.get(key).put(k, v);
        });
        return new ArrayList<>(groups.values());
    }

    private Controller createController(Map<String, PathItem> paths) {
        return createController(paths, null);
    }

    private Controller createController(Map<String, PathItem> paths, Entity entity) {
        String entityName = entity == null ? null : entity.getName();
        Controller controller = new Controller();
        controller.setEntity(entityName);
        String requestMapping = paths.keySet().iterator().next();
        requestMapping = requestMapping.split("/")[1];
        controller.setMapping(requestMapping);
        List<Endpoint> endpoints = getEndpoints(paths);
        endpoints.forEach(e -> e.setRelatedEntity(entityName));
        controller.setEndpoints(endpoints);
        return controller;
    }

    private List<Endpoint> getEndpoints(Map<String, PathItem> paths) {
        List<Endpoint> endpoints = new ArrayList<>();
        for (Map.Entry<String, PathItem> pathData : paths.entrySet()) {
            endpoints.addAll(getEndpoints(pathData.getKey(), pathData.getValue()));
        }
        return endpoints;
    }

    private List<Endpoint> getEndpoints(String pathMapping, PathItem pathItem) {
        List<Endpoint> endpoints = new ArrayList<>();
        String[] pathParts = pathMapping.split("/", 3);
        String idPathPart = isCollectionPath(pathParts) ? null : pathParts[2];
        if (pathItem.getGet() != null)
            endpoints.add(getEndpoint(pathItem.getGet(), Endpoint.Method.GET, idPathPart));
        if (pathItem.getPost() != null)
            endpoints.add(getEndpoint(pathItem.getPost(), Endpoint.Method.POST, idPathPart));
        if (pathItem.getPut() != null)
            endpoints.add(getEndpoint(pathItem.getPut(), Endpoint.Method.PUT, idPathPart));
        if (pathItem.getDelete() != null)
            endpoints.add(getEndpoint(pathItem.getDelete(), Endpoint.Method.DELETE, idPathPart));
        return endpoints;
    }

    private boolean isCollectionPath(String[] pathParts) {
        return pathParts.length == 2;
    }

    private Endpoint getEndpoint(
            Operation operation,
            Endpoint.Method method,
            String pathPart
    ) {
        Endpoint endpoint = new Endpoint();
        endpoint.setMethod(method);
        endpoint.setName(operation.getOperationId());
        endpoint.setMapping(pathPart);
        Mapping mapping = new Mapping(pathPart);
        endpoint.setParameters(parametersExtractor.readParameters(operation.getParameters()));
        if (!mapping.isSearch()) {
            endpoint.setRequest(requestExtractor.getRequest(operation));
        }
        endpoint.setResponse(responseExtractor.getResponse(operation));
        return endpoint;
    }


}
