package net.cloudappi.apigen.generatorcore.generator.web.controller.endpoints;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import net.cloudappi.apigen.generatorcore.config.controller.Endpoint;
import net.cloudappi.apigen.generatorcore.utils.Mapping;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntityBuilder;
import net.cloudappi.apigen.generatorcore.generator.web.resource.input.AllInputResourceBuilder;
import net.cloudappi.apigen.generatorcore.generator.web.resource.output.EntityOutputResourceBuilder;
import net.cloudappi.apigen.generatorcore.generator.web.response.SimpleResponseBuilder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.Set;

public class PutEndpointBuilder extends EndpointBuilder {

    private TypeName composedIdType;

    public PutEndpointBuilder(Mapping rootMapping, Endpoint endpoint, TypeName composedIdType, String basePackage) {
        super(rootMapping, endpoint, basePackage);
        this.composedIdType = composedIdType;
    }

    @Override
    protected void generate() {
        super.generate();
        addUpdatedFieldsParam();
    }

    @Override
    protected Class getMappingClass() {
        return PutMapping.class;
    }

    @Override
    protected String getMapping() {
        return mapping.getValue();
    }

    @Override
    protected TypeName getReturnTypeName() {
        return SimpleResponseBuilder.getTypeName(entityName, basePackage);
    }

    @Override
    protected TypeName getBodyTypeName() {
        return AllInputResourceBuilder.getTypeName(endpoint, basePackage);
    }

    private void addUpdatedFieldsParam() {
        TypeName updatedFieldsType = ParameterizedTypeName.get(ClassName.get(Set.class), ClassName.get(String.class));
        builder.addParameter(ParameterSpec.builder(updatedFieldsType, "updatedFields").addAnnotation(RequestAttribute.class).build());
    }

    @Override
    protected void addStatements() {
        TypeName entityType = EntityBuilder.getTypeName(entityName, basePackage);
        TypeName resourceType = EntityOutputResourceBuilder.getTypeName(entityName, basePackage);
        TypeName responseType = SimpleResponseBuilder.getTypeName(entityName, basePackage);
        builder.addStatement("$T updateRequest = $L.toEntity(body)", entityType, MAPPER_NAME);
        builder.addStatement("$L.update($L, updateRequest, updatedFields)", SERVICE_NAME, identifierParam);
        builder.addStatement("$T createResult = $L.search($L, null, null, null)", entityType, SERVICE_NAME, identifierParam);
        builder.addStatement("$T result = $L.toResource(createResult)", resourceType, MAPPER_NAME);
        builder.addStatement("return new $T(result)", responseType);
    }

    @Override
    protected TypeName getIdentifierPathParamType() {
        return composedIdType;
    }
}
