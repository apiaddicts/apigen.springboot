package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints.*;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints.EndpointBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints.EndpointBuilderFactory;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints.EndpointObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApigenEndpointBuilderFactoryImplTests {

    private final String PACKAGE = "the.base.package";
    private final String ENTITY_NAME = "EntityName";
    private final Mapping ROOT_MAPPING = new Mapping("/entities");
    private final JavaEntitiesData ENTITIES_DATA = Mockito.mock(JavaEntitiesData.class);
    
    private final EndpointBuilderFactory<ApigenContext> FACTORY = new ApigenEndpointBuilderFactoryImpl<>();
    
    private final Configuration CFG = ConfigurationObjectMother.create();
    
    private final ApigenContext CTX = ApigenContextObjectMother.create();
    
    @BeforeEach
    public void beforeEach() {
        CTX.setEntitiesData(ENTITIES_DATA);
    }

    @Test
    void givenPostEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardPost("postEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof PostEndpointBuilder, "Expected PostEndpointBuilder");
    }

    @Test
    void givenPostSearchEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardSearch("postSearchEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof PostSearchEndpointBuilder, "Expected PostSearchEndpointBuilder");
    }

    @Test
    void givenGetAllEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardGetAll("getAllEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof GetAllEndpointBuilder, "Expected GetAllEndpointBuilder");
    }

    @Test
    void givenGetAllMoreLevelsEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardGetAlMoreLevels("getAllMoreLevelsEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof GetAllMoreLevelsEndpointBuilder, "Expected GetAllMoreLevelsEndpointBuilder");
    }

    @Test
    void givenGetByIdEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardGetById("getByIdEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof GetByIdEndpointBuilder, "Expected GetByIdEndpointBuilder");
    }

    @Test
    void givenGetByIdMoreLevelsEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardGetAlMoreLevels("getByIdMoreLevelsEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof GetAllMoreLevelsEndpointBuilder, "Expected GetByIdMoreLevelsEndpointBuilder");
    }

    @Test
    void givenPutEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardPut("putEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof PutEndpointBuilder, "Expected PutEndpointBuilder");
    }

    @Test
    void givenDeleteEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardDelete("deleteEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof DeleteEndpointBuilder, "Expected DeleteEndpointBuilder");
    }

    @Test
    void givenNonStandardEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.customEndpoint();
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof ApigenGenericEndpointBuilder, "Expected CustomEndpointBuilder");
    }
}
