package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaMethodBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters.ParameterBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters.ParameterBuilderFactory;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters.ParameterBuilderFactoryImpl;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.lang.model.element.Modifier;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Constants.JSON_MIME_TYPE;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.ENUM_VALUE;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.CODE;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.VALUE;

@Slf4j
public abstract class EndpointBuilder<C extends JavaContext> extends AbstractJavaMethodBuilder<C> {

    protected String entityName;
    protected Mapping rootMapping;
    protected Mapping mapping;
    protected Endpoint endpoint;

    protected final ParameterBuilderFactory<C> paramFactory;

    public EndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        this(rootMapping, endpoint, ctx, cfg, new ParameterBuilderFactoryImpl<>());
    }

    public EndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg, ParameterBuilderFactory<C> paramFactory) {
        super(ctx, cfg);
        this.rootMapping = rootMapping;
        this.entityName = endpoint.getRelatedEntity();
        this.endpoint = endpoint;
        this.mapping = new Mapping(endpoint.getMapping());
        this.paramFactory = paramFactory;
    }

    @Override
    protected void initialize() {
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
        if (endpoint.getRequest() != null && !JSON_MIME_TYPE.equals(endpoint.getRequest().getMimeType())) {
            String mimeTypeStr = endpoint.getRequest().getMimeType();
            if (mimeTypeStr != null) mappingAnnotation.addMember("consumes", STRING, mimeTypeStr);
        }
        if (endpoint.getResponse() != null && !JSON_MIME_TYPE.equals(endpoint.getResponse().getMimeType())) {
            String mimeTypeStr = endpoint.getResponse().getMimeType();
            if (mimeTypeStr != null) mappingAnnotation.addMember("produces", STRING, mimeTypeStr);
        }
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

    protected void addParam(Parameter param) {
        ParameterBuilder<C> parameterBuilder = paramFactory.create(param, null, ctx, cfg);
        if (parameterBuilder != null) parameterBuilder.apply(builder);
    }
}
