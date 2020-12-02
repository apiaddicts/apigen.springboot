package net.cloudappi.apigen.generatorcore.generator.web.controller.endpoints;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import net.cloudappi.apigen.generatorcore.config.controller.Endpoint;
import net.cloudappi.apigen.generatorcore.utils.Mapping;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntityBuilder;
import net.cloudappi.apigen.generatorcore.generator.web.resource.input.AllInputResourceBuilder;
import net.cloudappi.apigen.generatorcore.generator.web.resource.output.EntityOutputResourceBuilder;
import net.cloudappi.apigen.generatorcore.generator.web.response.SimpleResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public class PostEndpointBuilder extends EndpointBuilder {

    public PostEndpointBuilder(Mapping rootMapping, Endpoint endpoint, String basePackage) {
        super(rootMapping, endpoint, basePackage);
    }

    @Override
    protected Class getMappingClass() {
        return PostMapping.class;
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.CREATED;
    }

    @Override
    protected TypeName getReturnTypeName() {
        return SimpleResponseBuilder.getTypeName(entityName, basePackage);
    }

    @Override
    protected void addRequestBody() {
        TypeName bodyType = AllInputResourceBuilder.getTypeName(endpoint, basePackage);
        builder.addParameter(ParameterSpec.builder(bodyType, "body").addAnnotation(RequestBody.class).addAnnotation(Valid.class).build());
    }

    @Override
    protected void addStatements() {
        TypeName entityType = EntityBuilder.getTypeName(entityName, basePackage);
        TypeName resourceType = EntityOutputResourceBuilder.getTypeName(entityName, basePackage);
        TypeName responseType = SimpleResponseBuilder.getTypeName(entityName, basePackage);
        builder.addStatement("$T createRequest = $L.toEntity(body)", entityType, MAPPER_NAME);
        builder.addStatement("$L.create(createRequest)", SERVICE_NAME);
        builder.addStatement("$T createResult = $L.search(createRequest.getId(), null, null, null)", entityType, SERVICE_NAME);
        builder.addStatement("$T result = $L.toResource(createResult)", resourceType, MAPPER_NAME);
        builder.addStatement("return new $T(result)", responseType);
    }
}
