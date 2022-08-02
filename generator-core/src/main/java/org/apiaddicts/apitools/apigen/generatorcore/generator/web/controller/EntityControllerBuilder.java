package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.archetypecore.core.resource.ResourceNamingTranslator;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.generator.mapper.MapperBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.service.ServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.lang.model.element.Modifier;

@Deprecated
public class EntityControllerBuilder extends ControllerBuilder {

    private String entityName;

    public EntityControllerBuilder(Controller controller, EntitiesData entitiesData, String basePackage) {
        super(controller, entitiesData, basePackage);
        this.entityName = controller.getEntity();
    }

    private static String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName, "web");
    }

    @Override
    protected String getName() {
        return entityName + "Controller";
    }

    @Override
    protected String getTag() {
        return entityName;
    }

    @Override
    public String getPackage() {
        return getPackage(entityName, basePackage);
    }

    @Override
    public void initialize() {
        initializeBuilder();
        autowireService();
        autowireMapper();
        autowireNamingTranslator();
        generateEndpoints();
    }

    private void autowireService() {
        TypeName serviceType = ServiceBuilder.getTypeName(entityName, basePackage);
        FieldSpec field = FieldSpec.builder(serviceType, "service", Modifier.PROTECTED)
                .addAnnotation(Autowired.class)
                .build();
        builder.addField(field);
    }

    private void autowireMapper() {
        TypeName mapperType = MapperBuilder.getTypeName(entityName, basePackage);
        FieldSpec field = FieldSpec.builder(mapperType, "mapper", Modifier.PROTECTED)
                .addAnnotation(Autowired.class)
                .build();
        builder.addField(field);
    }

    private void autowireNamingTranslator() {
        TypeName translatorType = ClassName.get(ResourceNamingTranslator.class);
        FieldSpec field = FieldSpec.builder(translatorType, "namingTranslator", Modifier.PROTECTED)
                .addAnnotation(Autowired.class)
                .build();
        builder.addField(field);
    }
}
