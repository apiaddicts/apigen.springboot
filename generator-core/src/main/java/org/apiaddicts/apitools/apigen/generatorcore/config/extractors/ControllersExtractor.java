package org.apiaddicts.apitools.apigen.generatorcore.config.extractors;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.stream.Stream;

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
        List<Controller> entityControllers = getEntityControllers(paths, bindings, rawEntities);
        List<Controller> nestedParentChildEntityControllers = getNestedParentChildEntityControllers(paths, bindings, rawEntities);
        List<Controller> otherControllers = getOtherControllers(paths, bindings);
        return Stream.of(entityControllers, nestedParentChildEntityControllers, otherControllers)
                .flatMap(List::stream).collect(Collectors.toList());
    }

    private List<Controller> getEntityControllers(Map<String, PathItem> paths, Map<PathItem, ApigenBinding> bindings, List<Entity> rawEntities) {
        Map<String, Entity> entities = rawEntities.stream().collect(Collectors.toMap(Entity::getName, e -> e));
        Map<String, Map<String, PathItem>> pathsByEntity = new HashMap<>();
        for (Map.Entry<String, PathItem> path : paths.entrySet()) {
            ApigenBinding binding = bindings.get(path.getValue());
            if (binding == null || binding.getChildModel() != null) continue;
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

    private List<Controller> getNestedParentChildEntityControllers(Map<String, PathItem> paths, Map<PathItem, ApigenBinding> bindings, List<Entity> rawEntities) {
        Map<String, Entity> entities = rawEntities.stream().collect(Collectors.toMap(Entity::getName, e -> e));
        Map<Pair<String, String>, Map<String, PathItem>> pathsByParentChildEntities = new HashMap<>();
        for (Map.Entry<String, PathItem> path : paths.entrySet()) {
            ApigenBinding binding = bindings.get(path.getValue());
            if (binding == null || binding.getChildModel() == null) continue;
            Entity parentEntity = entities.get(binding.getModel());
            Entity childEntity = entities.get(binding.getChildModel());
            if (parentEntity == null) {
                log.warn("Model not found {} , path {} ignored", binding.getModel(), path.getKey());
            } else if (childEntity == null) {
                log.warn("Model not found {} , path {} ignored", binding.getChildModel(), path.getKey());
            } else {
                Pair<String, String> childParentEntity = Pair.of(childEntity.getName(), parentEntity.getName());
                pathsByParentChildEntities.putIfAbsent(childParentEntity, new HashMap<>());
                pathsByParentChildEntities.get(childParentEntity).put(path.getKey(), path.getValue());
            }
        }
        return pathsByParentChildEntities.entrySet().stream()
                .map(entry -> {
                    String childEntity = entry.getKey().getLeft();
                    String parentEntity = entry.getKey().getRight();
                    return createController(entry.getValue(), entities.get(childEntity), entities.get(parentEntity), bindings);
                })
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
        return createController(paths, entity, null, null);
    }

    private Controller createController(Map<String, PathItem> paths, Entity entity, Entity parentEntity, Map<PathItem, ApigenBinding> bindings) {
        String entityName = entity == null ? null : entity.getName();
        Controller controller = new Controller();
        controller.setEntity(entityName);
        String requestMapping = paths.keySet().iterator().next();
        String[] mappingParts = requestMapping.split("/");
        requestMapping = StringUtils.join(mappingParts, "/", 0, (parentEntity != null) ? 4 : 2);
        controller.setMapping(requestMapping);
        List<Endpoint> endpoints = getEndpoints(paths, requestMapping, bindings);
        endpoints.forEach(e -> e.setRelatedEntity(entityName));
        String parentEntityName = parentEntity == null ? null : parentEntity.getName();
        endpoints.forEach(e -> e.setParentEntity(parentEntityName));
        controller.setEndpoints(endpoints);
        return controller;
    }

    private List<Endpoint> getEndpoints(Map<String, PathItem> paths, String controllerMapping, Map<PathItem, ApigenBinding> bindings) {
        List<Endpoint> endpoints = new ArrayList<>();
        for (Map.Entry<String, PathItem> pathData : paths.entrySet()) {
            String endpointMapping = pathData.getKey().replace(controllerMapping, "");
            if (endpointMapping.isEmpty()) endpointMapping = null;
            String childParentRelationProperty = (bindings == null) ? null : bindings.get(pathData.getValue()).getChildParentRelationProperty();
            endpoints.addAll(getEndpoints(endpointMapping, pathData.getValue(), childParentRelationProperty));
        }
        return endpoints;
    }

    private List<Endpoint> getEndpoints(String endpointMapping, PathItem pathItem, String childParentRelationProperty) {
        List<Endpoint> endpoints = new ArrayList<>();
        if (pathItem.getGet() != null)
            endpoints.add(getEndpoint(pathItem.getGet(), Endpoint.Method.GET, endpointMapping));
        if (pathItem.getPost() != null)
            endpoints.add(getEndpoint(pathItem.getPost(), Endpoint.Method.POST, endpointMapping));
        if (pathItem.getPut() != null)
            endpoints.add(getEndpoint(pathItem.getPut(), Endpoint.Method.PUT, endpointMapping));
        if (pathItem.getDelete() != null)
            endpoints.add(getEndpoint(pathItem.getDelete(), Endpoint.Method.DELETE, endpointMapping));
        if (pathItem.getPatch() != null)
            endpoints.add(getEndpoint(pathItem.getPatch(), Endpoint.Method.PATCH, endpointMapping));
        endpoints.forEach(e -> e.setChildParentRelationProperty(childParentRelationProperty));
        return endpoints;
    }

    private Endpoint getEndpoint(
            Operation operation,
            Endpoint.Method method,
            String endpointMapping
    ) {
        Endpoint endpoint = new Endpoint();
        endpoint.setMethod(method);
        endpoint.setName(operation.getOperationId());
        endpoint.setMapping(endpointMapping);
        Mapping mapping = new Mapping(endpointMapping);
        endpoint.setParameters(parametersExtractor.readParameters(operation.getParameters()));
        if (!mapping.isSearch()) {
            endpoint.setRequest(requestExtractor.getRequest(operation));
        }
        endpoint.setResponse(responseExtractor.getResponse(operation));
        return endpoint;
    }


}
