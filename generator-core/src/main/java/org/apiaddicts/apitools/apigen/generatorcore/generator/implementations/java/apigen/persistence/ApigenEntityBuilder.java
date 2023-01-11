package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.persistence;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Attribute;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Column;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.EntityBuilder;

import javax.lang.model.element.Modifier;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.NAME;

@Slf4j
public class ApigenEntityBuilder<C extends ApigenContext> extends EntityBuilder<C> {

    public ApigenEntityBuilder(Entity entity, C ctx, Configuration cfg) {
        super(entity, ctx, cfg);
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addConstructorById();
        addSetAndGetIdMethods();
        addAttributes();
        addIsReferenceMethod();
    }

    @Override
    protected void initializeBuilder() {
        builder = getClass(entity.getName())
                .superclass(getParentClass())
                .addAnnotation(Getter.class).addAnnotation(Setter.class)
                .addAnnotation(getAnnotation(jakarta.persistence.Entity.class).build())
                .addAnnotation(getAnnotation(Table.class).addMember(NAME, STRING, getTable()).build())
                .addAnnotation(NoArgsConstructor.class);
    }

    protected ParameterizedTypeName getParentClass() {
        return ParameterizedTypeName.get(
                ClassName.get(ApigenAbstractPersistable.class),
                ApigenEntityBuilder.getIDTypeName(entity, basePackage)
        );
    }

    protected void addConstructorById() {
        String attributeName = getIDName(entity);
        if (attributeName == null) return;
        TypeName identifierType = getIDTypeName(entity, basePackage);
 
		builder.addMethod(MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(identifierType, "id")
				.addStatement("this.setId(id)")
				.build());		
	}

    protected void addSetAndGetIdMethods() {
        String attributeName = getIDName(entity);
        if (attributeName == null) return;
        TypeName identifierType = getIDTypeName(entity, basePackage);

        builder.addMethod(MethodSpec.methodBuilder("getId")
                .addAnnotation(AnnotationSpec.builder(Override.class).build())
                .addModifiers(Modifier.PUBLIC)
                .returns(identifierType)
                .addStatement("return this.$N", attributeName)
                .build());

        builder.addMethod(MethodSpec.methodBuilder("setId")
                .addAnnotation(AnnotationSpec.builder(Override.class).build())
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(identifierType, attributeName)
                .addStatement("this.$N = $N", attributeName, attributeName)
                .build());
    }

    protected void addIsReferenceMethod() {

        Column column = getSimpleIDAttribute(entity).flatMap(a -> Optional.of(a.getColumns().get(0))).orElse(null);
        boolean autogenerated = column != null && column.getAutogenerated();

        StringBuilder statement = new StringBuilder();
        statement.append("return getId() != null");

        if (!autogenerated) {
            String idAttributeName = getIDName(entity);
            List<String> otherAttributes = entity.getAttributes().stream()
                    .map(Attribute::getName).filter(n -> !n.equals(idAttributeName)).collect(Collectors.toList());
            otherAttributes.forEach(a -> statement.append(" && ").append(a).append(" == null"));
        }

        builder.addMethod(MethodSpec.methodBuilder("isReference")
                .addAnnotation(AnnotationSpec.builder(Override.class).build())
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addStatement(statement.toString())
                .build());
    }
}
