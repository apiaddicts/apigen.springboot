package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence;

import com.squareup.javapoet.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Attribute;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Column;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.ApigenExt2JavapoetType;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.CustomStringUtils;

import javax.lang.model.element.Modifier;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.LITERAL;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Formats.STRING;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.NAME;
import static org.apiaddicts.apitools.apigen.generatorcore.generator.common.Members.UNIQUE;

@Slf4j
public class ComposedIdBuilder<C extends JavaContext> extends AbstractJavaClassBuilder<C> {

    protected final String entityName;
    protected final List<Attribute> idAttributes;
    protected final String basePackage;

    protected final TypeName typeName;

    public ComposedIdBuilder(Entity entity, C ctx, Configuration cfg) {
        super(ctx, cfg);
        this.entityName = entity.getName();
        this.idAttributes = getIdAttributes(entity);
        this.basePackage = cfg.getBasePackage();
        typeName = getTypeName(entityName, basePackage);
    }

    protected List<Attribute> getIdAttributes(Entity entity) {
        return entity.getAttributes().stream()
                .filter(a -> ApigenExt2JavapoetType.isComposedID(a.getType()))
                .findFirst().orElseThrow(IllegalArgumentException::new)
                .getAttributes();
    }

    public static TypeName getTypeName(String entityName, String basePackage) {
        return ClassName.get(getPackage(entityName, basePackage), getName(entityName));
    }

    private static String getPackage(String entityName, String basePackage) {
        return concatPackage(basePackage, entityName);
    }

    private static String getName(String entityName) {
        return entityName + "ID";
    }

    @Override
    public String getPackage() {
        return getPackage(entityName, basePackage);
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addIdAttributes();
        addFromMethod();
        addToStringMethod();
        addCompareToMethod();
    }

    protected void initializeBuilder() {
        builder = getClass(getName(entityName))
                .addSuperinterface(Serializable.class)
                .addSuperinterface(getComparableInterfaceClass())
                .addAnnotation(Getter.class).addAnnotation(Setter.class).addAnnotation(Embeddable.class)
                .addAnnotation(NoArgsConstructor.class).addAnnotation(AllArgsConstructor.class)
                .addAnnotation(EqualsAndHashCode.class);
    }

    protected ParameterizedTypeName getComparableInterfaceClass() {
        return ParameterizedTypeName.get(ClassName.get(Comparable.class), typeName);
    }

    protected void addIdAttributes() {
        idAttributes.forEach(this::addAttribute);
    }

    protected void addAttribute(Attribute attribute) {
        log.debug("Parsing attribute '{}' of type '{}' in entity composed ID '{}' | {}", attribute.getName(), attribute.getType(), entityName, attribute);
        FieldSpec.Builder fieldBuilder = getBasicFieldBuilder(attribute.getType(), attribute.getName());
        Column column = attribute.getColumns().isEmpty() ? new Column() : attribute.getColumns().get(0);
        addColumnAnnotation(attribute.getName(), column, fieldBuilder);
        attribute.getValidations().forEach(validation -> validation.apply(fieldBuilder));
        builder.addField(fieldBuilder.build());
    }

    protected FieldSpec.Builder getBasicFieldBuilder(String type, String attributeName) {
        TypeName attributeType = ApigenExt2JavapoetType.transformSimpleType(type);
        return FieldSpec.builder(attributeType, attributeName, Modifier.PRIVATE);
    }

    protected void addColumnAnnotation(String javaName, Column column, FieldSpec.Builder fieldBuilder) {
        if (isNull(column) || isNull(fieldBuilder)) return;
        String columnName = column.getName();
        if (columnName == null) columnName = CustomStringUtils.camelCaseToSnakeCase(javaName);
        Boolean isUnique = column.getUnique();
        AnnotationSpec.Builder annotationBuilder = getAnnotation(jakarta.persistence.Column.class)
                .addMember(NAME, STRING, columnName);

        if (Boolean.TRUE.equals(isUnique)) {
            annotationBuilder.addMember(UNIQUE, LITERAL, true);
        }

        fieldBuilder.addAnnotation(annotationBuilder.build());
    }

    protected void addFromMethod() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("from")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String.class, "str")
                .returns(typeName)
                .addStatement("if (str == null) return null")
                .addStatement("$T[] parts = str.split(\"_\")", String.class)
                .addStatement("if (parts.length != $L) throw new $T()", idAttributes.size(), IllegalArgumentException.class);
        List<Object> args = new ArrayList<>();
        args.add(typeName);
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < idAttributes.size(); i++) {
            TypeName attTypeName = ApigenExt2JavapoetType.transformSimpleType(idAttributes.get(i).getType());
            if (attTypeName.equals(TypeName.get(String.class))) {
                params.append("parts[").append(i).append("]");
            } else if (attTypeName.equals(TypeName.get(LocalDate.class)) || attTypeName.equals(TypeName.get(LocalDateTime.class))) {
                args.add(attTypeName);
                params.append("$T.parse(parts[").append(i).append("])");
            } else {
                args.add(attTypeName);
                params.append("$T.valueOf(parts[").append(i).append("])");
            }
            if (i < idAttributes.size() - 1) {
                params.append(", ");
            }
        }
        methodBuilder.addStatement("return new $T(" + params + ")", args.toArray());
        builder.addMethod(methodBuilder.build());
    }

    protected void addToStringMethod() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(String.class)
                .addStatement("return " + idAttributes.stream().map(Attribute::getName).collect(Collectors.joining(" + \"_\" + ")));
        builder.addMethod(methodBuilder.build());
    }

    protected void addCompareToMethod() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("compareTo")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(typeName, "o")
                .returns(int.class)
                .addStatement("int c");
        for (Attribute a : idAttributes) {
            methodBuilder.addStatement("c = $1L.compareTo(o.$1L)", a.getName());
            methodBuilder.addStatement("if (c != 0) return c");
        }
        methodBuilder.addStatement("return c");
        builder.addMethod(methodBuilder.build());
    }
}
