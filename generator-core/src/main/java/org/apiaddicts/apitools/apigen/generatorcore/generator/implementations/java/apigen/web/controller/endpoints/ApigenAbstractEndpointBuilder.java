package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Parameter;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.endpoints.EndpointBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.parameters.ParameterBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;

public abstract class ApigenAbstractEndpointBuilder<C extends ApigenContext> extends EndpointBuilder<C> {

    protected static final String SERVICE_NAME = "service";
    protected static final String MAPPER_NAME = "mapper";
    protected static final String NAMING_TRANSLATOR_NAME = "namingTranslator";

    protected final TypeName composedIdType;
    protected String firstPathParam = null;

    public ApigenAbstractEndpointBuilder(Mapping rootMapping, Endpoint endpoint, C ctx, Configuration cfg) {
        super(rootMapping, endpoint, ctx, cfg);
        composedIdType = ctx.getEntitiesData().getComposedIDType(endpoint.getRelatedEntity());
    }

    @Override
    protected String getMapping() {
        return mapping.getValue();
    }

    @Override
    protected void addParam(Parameter parameter) {
        // We consider the first path parameter as ID
        if ("path".equalsIgnoreCase(parameter.getIn()) && firstPathParam == null) {
            ParameterBuilder<C> paramBuilder = paramFactory.create(parameter, composedIdType, ctx, cfg);
            paramBuilder.apply(builder);
            firstPathParam = paramBuilder.javaName;
        } else {
            super.addParam(parameter);
        }
    }
}
