package net.cloudappi.apigen.generatorcore.generator.web.controller.endpoints;

import net.cloudappi.apigen.generatorcore.config.controller.Endpoint;
import net.cloudappi.apigen.generatorcore.config.controller.EndpointBaseResponseObjectMother;
import net.cloudappi.apigen.generatorcore.config.controller.EndpointRequestObjectMother;
import net.cloudappi.apigen.generatorcore.config.controller.Parameter;
import net.cloudappi.apigen.generatorcore.config.parameter.ParameterObjectMother;

import java.util.ArrayList;
import java.util.List;

public class EndpointObjectMother {

    private EndpointObjectMother() {
        // Intentional blank
    }

    public static Endpoint standardPost(String endpointName, String entityName) {
        Endpoint endpoint = createEndpoint(endpointName, Endpoint.Method.POST, null);
        endpoint.setRequest(EndpointRequestObjectMother.requestWithoutAttributes(entityName));
        endpoint.setResponse(EndpointBaseResponseObjectMother.simpleResponseWithoutAttributes(entityName));
        endpoint.setRelatedEntity(entityName);
        return endpoint;
    }

    public static Endpoint standardSearch(String endpointName, String entityName) {
        List<Parameter> parameters = ParameterObjectMother.createGetAllStandardParameters();
        Endpoint endpoint = createEndpoint(endpointName, Endpoint.Method.POST, "/search", parameters);
        endpoint.setResponse(EndpointBaseResponseObjectMother.listResponseWithoutAttributesAndCollectionNameEqualsEntityName(entityName));
        endpoint.setRelatedEntity(entityName);
        return endpoint;
    }

    public static Endpoint standardGetAll(String endpointName, String entityName) {
        List<Parameter> parameters = ParameterObjectMother.createGetAllStandardParameters();
        Endpoint endpoint = createEndpoint(endpointName, Endpoint.Method.GET, null, parameters);
        endpoint.setResponse(EndpointBaseResponseObjectMother.listResponseWithoutAttributesAndCollectionNameEqualsEntityName(entityName));
        endpoint.setRelatedEntity(entityName);
        return endpoint;
    }

    public static Endpoint standardGetById(String endpointName, String entityName) {
        List<Parameter> parameters = ParameterObjectMother.createGetByIdStandardParameters();
        Parameter parameter = ParameterObjectMother.getBasicPathParameter("id", "integer");
        parameters.add(0, parameter);
        Endpoint endpoint = createEndpoint(endpointName, Endpoint.Method.GET, "/{id}", parameters);
        endpoint.setResponse(EndpointBaseResponseObjectMother.simpleResponseWithoutAttributes(entityName));
        endpoint.setRelatedEntity(entityName);
        return endpoint;
    }

    public static Endpoint standardPut(String endpointName, String entityName) {
        List<Parameter> parameters = new ArrayList<>();
        Parameter parameter = ParameterObjectMother.getBasicPathParameter("id", "integer");
        parameters.add(parameter);
        Endpoint endpoint = createEndpoint(endpointName, Endpoint.Method.PUT, "/{id}", parameters);
        endpoint.setRequest(EndpointRequestObjectMother.requestWithoutAttributes(entityName));
        endpoint.setResponse(EndpointBaseResponseObjectMother.simpleResponseWithoutAttributes(entityName));
        endpoint.setRelatedEntity(entityName);
        ;
        return endpoint;
    }

    public static Endpoint standardDelete(String endpointName, String entityName) {
        List<Parameter> parameters = new ArrayList<>();
        Parameter parameter = ParameterObjectMother.getBasicPathParameter("id", "integer");
        parameters.add(parameter);
        Endpoint endpoint = createEndpoint(endpointName, Endpoint.Method.DELETE, "/{id}", parameters);
        endpoint.setResponse(EndpointBaseResponseObjectMother.simpleResponseWithoutAttributes(entityName));
        endpoint.setRelatedEntity(entityName);
        return endpoint;
    }

    public static Endpoint customEndpoint() {
        Endpoint endpoint = createEndpoint("custom", Endpoint.Method.POST, "endpoint");
        endpoint.setRequest(EndpointRequestObjectMother.customRequestWithoutAttributes());
        endpoint.setResponse(EndpointBaseResponseObjectMother.customResponseWithoutAttributes(201));
        return endpoint;
    }

    private static Endpoint createEndpoint(String name, Endpoint.Method operation, String mapping) {
        return createEndpoint(name, operation, mapping, new ArrayList<>());
    }

    private static Endpoint createEndpoint(String name, Endpoint.Method operation, String mapping, List<Parameter> parameters) {
        Endpoint endpoint = new Endpoint();
        endpoint.setMethod(operation);
        endpoint.setMapping(mapping);
        endpoint.setName(name);
        endpoint.setParameters(parameters);
        return endpoint;
    }
}
