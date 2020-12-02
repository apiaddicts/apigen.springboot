package net.cloudappi.apigen.generatorcore.config.controller;

import net.cloudappi.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;

import java.util.ArrayList;
import java.util.List;

public class ControllerObjectMother {

    public static Controller createControllerWithStandardEndpoints(String entityName, String requestMapping) {
        Controller controller = new Controller();
        controller.setEntity(entityName);
        controller.setMapping(requestMapping);

        List<Endpoint> endpointList = new ArrayList<>();
        endpointList.add(EndpointObjectMother.standardPost("postEndpoint", entityName));
        endpointList.add(EndpointObjectMother.standardSearch("postSearchEndpoint", entityName));
        endpointList.add(EndpointObjectMother.standardGetAll("getAllEndpoint", entityName));
        endpointList.add(EndpointObjectMother.standardGetById("getByIdEndpoint", entityName));
        endpointList.add(EndpointObjectMother.standardPut("putEndpoint", entityName));
        endpointList.add(EndpointObjectMother.standardDelete("deleteEndpoint", entityName));
        controller.setEndpoints(endpointList);
        return controller;
    }

    public static Controller createControllerWithSimpleResponse(String entityName) {
        Controller controller = new Controller();
        controller.setEntity(entityName);
        controller.setMapping(entityName + "s");

        Endpoint endpointSimple = EndpointObjectMother.standardGetById("postSearch", "EntityName");

        List<Endpoint> listEndpoints = new ArrayList<>();
        listEndpoints.add(endpointSimple);

        controller.setEndpoints(listEndpoints);
        return controller;
    }

    public static Controller createControllerWithListResponse(String entityName) {
        Controller controller = new Controller();
        controller.setEntity(entityName);
        controller.setMapping(entityName + "s");

        Endpoint endpointList = EndpointObjectMother.standardGetAll("getAll", "EntityName");

        List<Endpoint> listEndpoints = new ArrayList<>();
        listEndpoints.add(endpointList);

        controller.setEndpoints(listEndpoints);
        return controller;
    }

    public static Controller createControllerWithSimpleAndListResponse(String entityName) {
        Controller controller = new Controller();
        controller.setEntity(entityName);
        controller.setMapping(entityName + "s");

        Endpoint endpointSimple = EndpointObjectMother.standardGetById("postSearch", "EntityName");
        Endpoint endPointList = EndpointObjectMother.standardGetAll("getAll", "EntityName");

        List<Endpoint> listEndpoints = new ArrayList<>();
        listEndpoints.add(endpointSimple);
        listEndpoints.add(endPointList);

        controller.setEndpoints(listEndpoints);
        return controller;
    }

}
