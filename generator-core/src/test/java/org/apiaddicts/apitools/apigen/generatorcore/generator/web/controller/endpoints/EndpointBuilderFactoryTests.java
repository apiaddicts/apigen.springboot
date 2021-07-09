package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints;

import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EndpointBuilderFactoryTests {

    private final String PACKAGE = "the.base.package";
    private final String ENTITY_NAME = "EntityName";
    private final Mapping ROOT_MAPPING = new Mapping("/entities");
    private final EntitiesData ENTITIES_DATA = Mockito.mock(EntitiesData.class);

    @Test
    void givenPostEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardPost("postEndpoint", ENTITY_NAME);
        EndpointBuilder endpointBuilder = EndpointBuilderFactory.create(ROOT_MAPPING, endpoint, ENTITIES_DATA, PACKAGE);

        assertTrue(endpointBuilder instanceof PostEndpointBuilder, "Expected PostEndpointBuilder");
    }

    @Test
    void givenPostSearchEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardSearch("postSearchEndpoint", ENTITY_NAME);
        EndpointBuilder endpointBuilder = EndpointBuilderFactory.create(ROOT_MAPPING, endpoint, ENTITIES_DATA, PACKAGE);

        assertTrue(endpointBuilder instanceof PostSearchEndpointBuilder, "Expected PostSearchEndpointBuilder");
    }

    @Test
    void givenGetAllEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardGetAll("getAllEndpoint", ENTITY_NAME);
        EndpointBuilder endpointBuilder = EndpointBuilderFactory.create(ROOT_MAPPING, endpoint, ENTITIES_DATA, PACKAGE);

        assertTrue(endpointBuilder instanceof GetAllEndpointBuilder, "Expected GetAllEndpointBuilder");
    }

    @Test
    void givenGetByIdEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardGetById("getByIdEndpoint", ENTITY_NAME);
        EndpointBuilder endpointBuilder = EndpointBuilderFactory.create(ROOT_MAPPING, endpoint, ENTITIES_DATA, PACKAGE);

        assertTrue(endpointBuilder instanceof GetByIdEndpointBuilder, "Expected GetByIdEndpointBuilder");
    }

    @Test
    void givenPutEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardPut("putEndpoint", ENTITY_NAME);
        EndpointBuilder endpointBuilder = EndpointBuilderFactory.create(ROOT_MAPPING, endpoint, ENTITIES_DATA, PACKAGE);

        assertTrue(endpointBuilder instanceof PutEndpointBuilder, "Expected PutEndpointBuilder");
    }

    @Test
    void givenDeleteEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardDelete("deleteEndpoint", ENTITY_NAME);
        EndpointBuilder endpointBuilder = EndpointBuilderFactory.create(ROOT_MAPPING, endpoint, ENTITIES_DATA, PACKAGE);

        assertTrue(endpointBuilder instanceof DeleteEndpointBuilder, "Expected DeleteEndpointBuilder");
    }

    @Test
    void givenNonStandardEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.customEndpoint();
        EndpointBuilder endpointBuilder = EndpointBuilderFactory.create(ROOT_MAPPING, endpoint, ENTITIES_DATA, PACKAGE);

        assertTrue(endpointBuilder instanceof CustomEndpointBuilder, "Expected CustomEndpointBuilder");
    }
}
