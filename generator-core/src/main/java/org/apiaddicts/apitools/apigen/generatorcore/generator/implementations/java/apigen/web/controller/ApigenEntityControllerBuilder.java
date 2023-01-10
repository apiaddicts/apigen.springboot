package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.archetypecore.core.controllers.NestedParentChildController;
import org.apiaddicts.apitools.apigen.archetypecore.core.resource.ResourceNamingTranslator;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.mapper.ApigenMapperBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.service.ApigenServiceBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.web.controller.endpoints.ApigenEndpointBuilderFactoryImpl;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.controller.ControllerBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.lang.model.element.Modifier;

public class ApigenEntityControllerBuilder<C extends ApigenContext> extends ControllerBuilder<C> {

    protected final String entityName;

    public ApigenEntityControllerBuilder(Controller controller, C ctx, Configuration cfg) {
        super(controller, ctx, cfg, new ApigenEndpointBuilderFactoryImpl<>());
        this.entityName = controller.getEntity();
    }

    private static String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName, "web");
    }

    @Override
    protected String getName() {
        return this.endpoints.get(0).getParentEntity() == null
            ? entityName + "Controller"
            : this.endpoints.get(0).getParentEntity() + entityName + "Controller";
    }

    @Override
    protected String getTag() {
        return entityName;
    }

    @Override
    public String getPackage() {
        return getPackage(entityName, cfg.getBasePackage());
    }

    @Override
    public void initialize() {
        initializeBuilder();
        autowireService();
        autowireMapper();
        autowireNamingTranslator();
        generateEndpoints();
    }

    @Override
    protected void initializeBuilder(){
        super.initializeBuilder();
        if(this.endpoints.get(0).getParentEntity() != null) {
            this.builder.superclass(NestedParentChildController.class);
        }
    }

    private void autowireService() {
        TypeName serviceType = ApigenServiceBuilder.getTypeName(entityName, cfg.getBasePackage());
        FieldSpec field = FieldSpec.builder(serviceType, "service", Modifier.PROTECTED)
                .addAnnotation(Autowired.class)
                .build();
        builder.addField(field);
    }

    private void autowireMapper() {
        TypeName mapperType = ApigenMapperBuilder.getTypeName(entityName, cfg.getBasePackage());
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
