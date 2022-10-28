package org.apiaddicts.apitools.apigen.generatorcore.config.extractors;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Request;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.validation.Validation;
import org.apiaddicts.apitools.apigen.generatorcore.config.validation.ValidationType;
import org.apiaddicts.apitools.apigen.generatorcore.spec.OpenAPIExtended;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint.Method.*;
import static org.junit.jupiter.api.Assertions.*;

// TODO #14909 refactor tests to use individualized api fragments
class ConfigurationExtractorTest {

    private static Configuration configuration;

    @BeforeAll
    static void prepareTest() {
        OpenAPIExtended openAPIExtended = load("testApi.yaml");
        configuration = new ConfigurationExtractor(openAPIExtended).extract();
        assertNotNull(configuration);
    }

    private static OpenAPIExtended load(String fileName) {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolveFully(true);
        OpenAPI openAPI = new OpenAPIV3Parser().read("src/test/resources/api_fragments/" + fileName, null, parseOptions);
        return new OpenAPIExtended(openAPI);
    }

    @Test
    void checkExtractedProjectInfoFromYAML() {
        assertEquals(configuration.getName(), "test");
        assertEquals(configuration.getDescription(), "test");
        assertEquals("the.test", configuration.getGroup());
        assertEquals("1.0.0", configuration.getVersion());
        assertEquals(configuration.getArtifact(), "test");
    }

    @Test
    void checkExtractedEntitiesFromYAML() {
        assertNotNull(configuration.getEntities());

        List<Entity> entities = configuration.getEntities();

        assertEquals(2, entities.size());

        assertEquals("Owner", entities.get(0).getName(), "Check Entity Name");
        assertEquals("owners", entities.get(0).getTable(), "Check Entity Table");
        assertEquals(2, entities.get(0).getAttributes().size(), "Check Entity Number of Attributes");

        assertEquals("id", entities.get(0).getAttributes().get(0).getName(), "Check Attribute Name");
        assertEquals("String", entities.get(0).getAttributes().get(0).getType(), "Check Attribute Type");
        assertEquals("id", entities.get(0).getAttributes().get(0).getColumns().get(0).getName(), "Check Attribute ColumnName");
        assertTrue(entities.get(0).getAttributes().get(0).getForeignColumns().isEmpty(), "Check Attribute ForeignColumn");
        assertFalse(entities.get(0).getAttributes().get(0).getIsCollection(), "Check if Attribute is Collection");
        assertNull(entities.get(0).getAttributes().get(0).getRelation(), "Check if Attribute has Relation");

        assertEquals("pets", entities.get(0).getAttributes().get(1).getName(), "Check Attribute Name");
        assertEquals("Pet", entities.get(0).getAttributes().get(1).getType(), "Check Attribute Type");
        assertTrue(entities.get(0).getAttributes().get(1).getColumns().isEmpty(), "Check Attribute Column");
        assertEquals("owner_id", entities.get(0).getAttributes().get(1).getForeignColumns().get(0).getName(), "Check Attribute ForeignColumnName");
        assertTrue(entities.get(0).getAttributes().get(1).getIsCollection(), "Check if Attribute is Collection");
        assertNotNull(entities.get(0).getAttributes().get(1).getRelation(), "Check if Attribute has Relation");
        assertEquals("Pet", entities.get(0).getAttributes().get(1).getRelation().getRelatedEntity(), "Check Attribute's ForeignClassName");


        assertEquals("Pet", entities.get(1).getName());
        assertEquals("pets", entities.get(1).getTable(), "Check Entity Table");
        assertEquals(2, entities.get(1).getAttributes().size(), "Check Entity Number of Attributes");

        assertEquals("owner", entities.get(1).getAttributes().get(0).getName(), "Check Attribute Name");
        assertEquals("Owner", entities.get(1).getAttributes().get(0).getType(), "Check Attribute Type");
        assertFalse(entities.get(1).getAttributes().get(0).getColumns().isEmpty(), "Check Attribute Column");
        assertEquals("owner_id", entities.get(1).getAttributes().get(0).getColumns().get(0).getName(), "Check Attribute ColumnName");
        assertTrue(entities.get(1).getAttributes().get(0).getForeignColumns().isEmpty(), "Check Attribute ForeignColumn");
        assertFalse(entities.get(1).getAttributes().get(0).getIsCollection(), "Check if Attribute is Collection");
        assertNotNull(entities.get(1).getAttributes().get(0).getRelation(), "Check if Attribute has Relation");
        assertEquals("Owner", entities.get(1).getAttributes().get(0).getRelation().getRelatedEntity(), "Check Attribute's ForeignClassName");

        assertEquals("tags", entities.get(1).getAttributes().get(1).getName(), "Check Attribute Name");
        assertEquals("Tag", entities.get(1).getAttributes().get(1).getType(), "Check Attribute Type");
        assertTrue(entities.get(1).getAttributes().get(1).getIsCollection(), "Check if Attribute is Collection");
        assertNotNull(entities.get(1).getAttributes().get(1).getRelation(), "Check if Attribute has Relation");
        assertEquals("Tag", entities.get(1).getAttributes().get(1).getRelation().getRelatedEntity(), "Check Attribute's ForeignClassName");
        assertEquals("pet_tags", entities.get(1).getAttributes().get(1).getRelation().getIntermediateTable(), "Check Attribute's IntermediateTable");
        assertTrue(entities.get(1).getAttributes().get(1).getRelation().getOwner(), "Check if Attribute is Owner part of Relation");
        assertNotNull(entities.get(1).getAttributes().get(1).getRelation().getColumns().get(0).getName(), "Check Attribute Column");
        assertEquals("pet_id", entities.get(1).getAttributes().get(1).getRelation().getColumns().get(0).getName(), "Check Attribute ColumnName");
        assertNotNull(entities.get(1).getAttributes().get(1).getRelation().getReverseColumns().get(0).getName(), "Check Attribute ForeignColumn");
        assertEquals("tag_id", entities.get(1).getAttributes().get(1).getRelation().getReverseColumns().get(0).getName(), "Check Attribute ForeignColumnName");
    }

    @Test
    void checkExtractedValidationsFromYAMLModels() {
        assertNotNull(configuration.getEntities());

        List<Validation> validations = configuration.getEntities().get(0).getAttributes().get(0).getValidations();

        assertEquals(ValidationType.NOT_NULL, validations.get(0).getType(), "Check NotNull Validation");

        assertEquals(ValidationType.SIZE, validations.get(1).getType(), "Check Size Validation");
        assertEquals(1, validations.get(1).getIntegerValueOne(), "Check Size Min Value");
        assertEquals(2, validations.get(1).getIntegerValueTwo(), "Check Size Max Value");

        assertEquals(ValidationType.MIN, validations.get(2).getType(), "Check Min Validation");
        assertEquals(1L, validations.get(2).getLongValue(), "Check Min Value");

        assertEquals(ValidationType.MAX, validations.get(3).getType(), "Check Max Validation");
        assertEquals(2L, validations.get(3).getLongValue(), "Check Max Value");

        assertEquals(ValidationType.EMAIL, validations.get(4).getType(), "Check Email Validation");

        assertEquals(ValidationType.NOT_EMPTY, validations.get(5).getType(), "Check NotEmpty Validation");

        assertEquals(ValidationType.NOT_BLANK, validations.get(6).getType(), "Check NotBlank Validation");

        assertEquals(ValidationType.POSITIVE, validations.get(7).getType(), "Check Positive Validation");

        assertEquals(ValidationType.POSITIVE_OR_ZERO, validations.get(8).getType(), "Check PositiveOrZero Validation");

        assertEquals(ValidationType.NEGATIVE, validations.get(9).getType(), "Check Negative Validation");

        assertEquals(ValidationType.NEGATIVE_OR_ZERO, validations.get(10).getType(), "Check NegativeOrZero Validation");

        assertEquals(ValidationType.PAST, validations.get(11).getType(), "Check Past Validation");

        assertEquals(ValidationType.PAST_OR_PRESENT, validations.get(12).getType(), "Check PastOrPresent Validation");

        assertEquals(ValidationType.FUTURE, validations.get(13).getType(), "Check Future Validation");

        assertEquals(ValidationType.FUTURE_OR_PRESENT, validations.get(14).getType(), "Check FutureOrPresent Validation");

        assertEquals(ValidationType.PATTERN, validations.get(15).getType(), "Check Pattern Validation");
        assertEquals("[^i*&2@]", validations.get(15).getStringValue(), "Check Pattern Regex");

        assertEquals(ValidationType.DIGITS, validations.get(16).getType(), "Check Digits Validation");
        assertEquals(4, validations.get(16).getIntegerValueOne(), "Check Digits Validation");
        assertEquals(2, validations.get(16).getIntegerValueTwo(), "Check Digits Validation");

        assertEquals(ValidationType.DECIMAL_MIN, validations.get(17).getType(), "Check DecimalMin Validation");
        assertEquals(BigDecimal.valueOf(0.1), validations.get(17).getDecimalValue(), "Check DecimalMin Value");

        assertEquals(ValidationType.DECIMAL_MAX, validations.get(18).getType(), "Check DecimalMax Validation");
        assertEquals(BigDecimal.valueOf(0.2), validations.get(18).getDecimalValue(), "Check DecimanMax Value");

    }

    @Test
    void checkExtractedValidationsFromYAMLResources() {
        List<Validation> validations = configuration.getControllers().get(0).getEndpoints().get(0).getRequest().getAttributes().get(0).getValidations();
        assertEquals(ValidationType.NOT_NULL, validations.get(0).getType(), "Check NotNull Validation");
    }

    @Test
    void checkExtractedControllersFromYAML() {
        assertNotNull(configuration.getControllers());

        List<Controller> controllers = configuration.getControllers();

        assertEquals(1, controllers.size());

        Controller controller = controllers.get(0);

        assertEquals("Owner", controller.getEntity(), "Check Controller Entity");

        List<Endpoint> endpoints = controller.getEndpoints();

        assertEquals(4, endpoints.size());

        assertEquals(POST, endpoints.get(0).getMethod(), "Check Controller POST Endpoint");
        assertEquals(null, endpoints.get(0).getMapping(), "Check Controller POST Endpoint");

        assertEquals(GET, endpoints.get(1).getMethod(), "Check Controller GETbyID Endpoint");
        assertEquals("/{id}", endpoints.get(1).getMapping(), "Check Controller GETbyID Endpoint");

        assertEquals(PUT, endpoints.get(2).getMethod(), "Check Controller PUT Endpoint");
        assertEquals("/{id}", endpoints.get(2).getMapping(), "Check Controller PUT Endpoint");

        assertEquals(DELETE, endpoints.get(3).getMethod(), "Check Controller DELETE Endpoint");
        assertEquals("/{id}", endpoints.get(3).getMapping(), "Check Controller DELETE Endpoint");
    }

    @Test
    void checkExtractedEndpointsParametersFromYAML() {
        assertNotNull(configuration.getControllers());
        Controller controller = configuration.getControllers().get(0);

        List<Parameter> parameters = controller.getEndpoints().get(1).getParameters();

        checkIdParameter(parameters.get(0));
        checkIntegerParameter(parameters.get(1));
        checkDoubleParameter(parameters.get(2));
        checkStringParameter(parameters.get(3));
        checkListParameter(parameters.get(4));
        checkBooleanParameter(parameters.get(5));
    }

    @Test
    void givenOAPI3ExtendedWithRequestBody_whenConfigurationExtracted_thenResponseGenerated() {
        OpenAPIExtended extended = load("standard_request.yaml");
        Configuration configuration = new ConfigurationExtractor(extended).extract();
        assertEquals(1, configuration.getControllers().size(), "One controller expected");
        Controller controller = configuration.getControllers().get(0);
        assertEquals(1, controller.getEndpoints().size(), "One endpoint expected");
        Endpoint endpoint = controller.getEndpoints().get(0);
        Request request = endpoint.getRequest();
        assertNotNull(request, "Request expected");
    }

    private void checkIdParameter(Parameter parameter) {
        assertEquals("id", parameter.getName(), "Check Endpoint idParameter name");
        assertEquals("path", parameter.getIn(), "Check Endpoint idParameter in");
        assertEquals("string", parameter.getType(), "Check Endpoint idParameter type");
    }

    private void checkIntegerParameter(Parameter parameter) {
        assertEquals("integer", parameter.getName(), "Check Endpoint IntegerParameter name");
        assertEquals("query", parameter.getIn(), "Check Endpoint IntegerParameter in");
        assertEquals("integer", parameter.getType(), "Check Endpoint IntegerParameter type");
        assertEquals(10, parameter.getDefaultValue(), "Check Endpoint IntegerParameter defaultValue");

        assertEquals(new Validation(ValidationType.MIN, 10L), parameter.getValidations().get(0));
        assertEquals(new Validation(ValidationType.MAX, 20L), parameter.getValidations().get(1));
    }

    private void checkDoubleParameter(Parameter parameter) {
        assertEquals("double", parameter.getName(), "Check Endpoint DoubleParameter name");
        assertEquals("query", parameter.getIn(), "Check Endpoint DoubleParameter in");
        assertEquals("double", parameter.getFormat(), "Check Endpoint DoubleParameter type");
        assertEquals(10.5, Double.parseDouble(parameter.getDefaultValue().toString()), 0.00, "Check Endpoint DoubleParameter defaultValue");
        assertEquals(new Validation(ValidationType.DECIMAL_MIN, BigDecimal.valueOf(10.2), false), parameter.getValidations().get(0));
        assertEquals(new Validation(ValidationType.DECIMAL_MAX, BigDecimal.valueOf(20.3), true), parameter.getValidations().get(1));
    }

    private void checkStringParameter(Parameter parameter) {
        assertEquals("string", parameter.getName(), "Check Endpoint StringParameter name");
        assertEquals("query", parameter.getIn(), "Check Endpoint StringParameter in");
        assertEquals("string", parameter.getType(), "Check Endpoint StringParameter type");
        assertEquals("test", parameter.getDefaultValue(), "Check Endpoint StringParameter defaultValue");
        assertEquals(new Validation(ValidationType.SIZE, 3, 7), parameter.getValidations().get(0));
        assertEquals(new Validation(ValidationType.PATTERN, "\\\\d{9}"), parameter.getValidations().get(1));
    }

    private void checkListParameter(Parameter parameter) {
        assertEquals("list", parameter.getName(), "Check Endpoint ListParameter name");
        assertEquals("query", parameter.getIn(), "Check Endpoint ListParameter in");
        assertTrue(parameter.isCollection(), "Check Endpoint ListParameter type");
        assertEquals("string", parameter.getType(), "Check Endpoint ListParameter itemsType");
        assertEquals(new Validation(ValidationType.SIZE, 2, 5), parameter.getValidations().get(0));
    }

    private void checkBooleanParameter(Parameter parameter) {
        assertEquals("boolean", parameter.getName(), "Check Endpoint BooleanParameter name");
        assertEquals("query", parameter.getIn(), "Check Endpoint BooleanParameter in");
        assertEquals("boolean", parameter.getType(), "Check Endpoint BooleanParameter type");
    }

}
