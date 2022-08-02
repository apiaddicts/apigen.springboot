package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints;

import com.squareup.javapoet.TypeName;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Endpoint;
import org.apiaddicts.apitools.apigen.generatorcore.utils.Mapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;

@Deprecated
@Slf4j
public class DeleteEndpointBuilder extends EndpointBuilder {

    private TypeName composedIdType;

    public DeleteEndpointBuilder(Mapping rootMapping, Endpoint endpoint, TypeName composedIdType, String basePackage) {
        super(rootMapping, endpoint, basePackage);
        this.composedIdType = composedIdType;
    }

    @Override
    protected Class getMappingClass() {
        return DeleteMapping.class;
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.NO_CONTENT;
    }

    @Override
    protected String getMapping() {
        return mapping.getValue();
    }

    @Override
    protected void addStatements() {
        builder.addStatement("$L.delete($L)", SERVICE_NAME, identifierParam);
    }

    @Override
    protected TypeName getIdentifierPathParamType() {
        return composedIdType;
    }
}
