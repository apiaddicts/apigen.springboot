package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
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

    private final String ENTITY_NAME = "EntityName";
    private final Mapping ROOT_MAPPING = new Mapping("/entities");
    private final Mapping CHILD_ROOT_MAPPING = new Mapping("/entities/{id}/subentities");
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
    void givenGetAllParentChildEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardParentChildGetAll("getAllParentChildEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(CHILD_ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof GetAllParentChildEndpointBuilder, "Expected GetAllParentChildEndpointBuilder");
    }

    @Test
    void givenGetByIdEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardGetById("getByIdEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof GetByIdEndpointBuilder, "Expected GetByIdEndpointBuilder");
    }

    @Test
    void givenGetByIdParentChildEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardParentChildGetAll("getByIdParentChildEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(CHILD_ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof GetAllParentChildEndpointBuilder, "Expected GetByIdParentChildEndpointBuilder");
    }

    @Test
    void givenPutEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardPut("putEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof PutEndpointBuilder, "Expected PutEndpointBuilder");
    }

    @Test
    void givenPatchEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardPatch("patchEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof PatchEndpointBuilder, "Expected PatchEndpointBuilder");
    }

    @Test
    void givenPutParentChildEndpoint_whenAskForBuilder_thenCorrectBuilderIsReturned() {
        Endpoint endpoint = EndpointObjectMother.standardParentChildPut("putParentChildEndpoint", ENTITY_NAME);
        EndpointBuilder<ApigenContext> endpointBuilder = FACTORY.create(CHILD_ROOT_MAPPING, endpoint, CTX, CFG);

        assertTrue(endpointBuilder instanceof PutParentChildEndpointBuilder, "Expected PutParentChildEndpointBuilder");
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
