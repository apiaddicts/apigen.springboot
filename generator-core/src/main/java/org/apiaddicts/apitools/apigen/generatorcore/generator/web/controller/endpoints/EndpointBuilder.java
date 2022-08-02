package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints;

import com.squareup.javapoet.*;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.parameters.ParameterBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.parameters.PathParameterBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.parameters.QueryParameterBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.CustomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.lang.model.element.Modifier;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.ENUM_VALUE;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.CODE;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

@Deprecated
@Slf4j
public abstract class EndpointBuilder {

    protected static final String SERVICE_NAME = "service";
    protected static final String MAPPER_NAME = "mapper";
    protected static final String NAMING_TRANSLATOR_NAME = "namingTranslator";


    protected String basePackage;
    protected String entityName;
    protected Mapping rootMapping;
    protected Mapping mapping;
    protected Endpoint endpoint;

    protected Set<String> pathParams = new HashSet<>();
    protected Set<String> queryParams = new HashSet<>();
    protected String identifierParam;

    protected MethodSpec.Builder builder;

    public EndpointBuilder(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        this.basePackage = basePackage;
        this.rootMapping = rootMapping;
        this.entityName = endpoint.getRelatedEntity();
        this.endpoint = endpoint;
        this.mapping = new Mapping(endpoint.getMapping());
    }

    public void apply(TypeSpec.Builder builder) {
        if (this.builder == null) {
            generate();
        }
        builder.addMethod(this.builder.build());
    }

    protected void generate() {
        generateBuilder();
        addMapping();
        addResponseStatus();
        addReturnType();
        addParams();
        addRequestBody();
        addStatements();
    }

    protected void generateBuilder() {
        builder = MethodSpec.methodBuilder(endpoint.getName())
                .addModifiers(Modifier.PUBLIC);
    }

    protected void addMapping() {
        AnnotationSpec.Builder mappingAnnotation = AnnotationSpec.builder(getMappingClass());
        String mappingValue = getMapping();
        if (mappingValue != null) mappingAnnotation.addMember(VALUE, STRING, mappingValue);
        builder.addAnnotation(mappingAnnotation.build());
    }

    protected abstract Class getMappingClass();

    protected String getMapping() {
        return null;
    }

    protected void addResponseStatus() {
        builder.addAnnotation(
                AnnotationSpec.builder(ResponseStatus.class)
                        .addMember(CODE, ENUM_VALUE, HttpStatus.class, getResponseStatus().name())
                        .build()
        );
    }

    protected HttpStatus getResponseStatus() {
        return HttpStatus.OK;
    }

    protected void addReturnType() {
        TypeName typeName = getReturnTypeName();
        if (typeName != null) builder.returns(typeName);
    }

    protected TypeName getReturnTypeName() {
        return null;
    }

    protected void addRequestBody() {
        TypeName bodyType = getBodyTypeName();
        if (bodyType == null) return;
        builder.addParameter(ParameterSpec.builder(bodyType, "body").addAnnotation(RequestBody.class).addAnnotation(Valid.class).build());
    }

    protected TypeName getBodyTypeName() {
        return null;
    }

    protected abstract void addStatements();

    protected void addParams() {
        endpoint.getParameters().forEach(this::addParam);
    }

    private void addParam(Parameter param) {
        if ("path".equalsIgnoreCase(param.getIn())) {
            addPathParam(param);
        } else if ("query".equalsIgnoreCase(param.getIn())) {
            addQueryParam(param);
        } else {
            log.error("Param not supported in {}", param.getIn());
        }
    }

    private void addPathParam(Parameter param) {
        String javaName = getJavaParamName(param.getName());
        TypeName typeName = null;
        if (identifierParam == null) {
            identifierParam = javaName;
            typeName = getIdentifierPathParamType();
        }
        pathParams.add(javaName);

        ParameterBuilder parameterBuilder =
                typeName == null ? new PathParameterBuilder(param, javaName) : new PathParameterBuilder(param, typeName, javaName);
        builder.addParameter(parameterBuilder.build());
    }

    protected TypeName getIdentifierPathParamType() {
        return null;
    }

    private void addQueryParam(Parameter param) {
        String javaName = getJavaParamName(param.getName());
        queryParams.add(javaName);

        ParameterBuilder parameterBuilder = new QueryParameterBuilder(param, javaName);
        builder.addParameter(parameterBuilder.build());
    }

    private String getJavaParamName(String name) {
        return CustomStringUtils.snakeCaseToCamelCase(name).replaceAll("\\$", "");
    }
}
