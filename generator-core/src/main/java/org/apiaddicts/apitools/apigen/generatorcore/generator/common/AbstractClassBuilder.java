package org.apiaddicts.apitools.apigen.generatorcore.generator.common;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

@Deprecated
public abstract class AbstractClassBuilder {

    protected TypeSpec.Builder builder;

    protected static String concatPackage(String basePackage, String newPackage) {
        return String.format("%s.%s", basePackage, newPackage.toLowerCase());
    }

    protected static String concatPackage(String basePackage, String newPackage, String otherPackage) {
        return String.format("%s.%s.%s", basePackage, newPackage.toLowerCase(), otherPackage);
    }

    protected static TypeSpec.Builder getClass(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }

    protected static TypeSpec.Builder getInnerClass(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC);
    }

    protected static TypeSpec.Builder getPublicInnerClass(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
    }

    protected static TypeSpec.Builder getInterface(String name) {
        return TypeSpec.interfaceBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }

    protected static AnnotationSpec.Builder getAnnotation(Class clazz) {
        return AnnotationSpec.builder(clazz);
    }

    protected static FieldSpec.Builder getField(TypeName type, String name) {
        return FieldSpec.builder(type, name, Modifier.PRIVATE);
    }

    public abstract String getPackage();

    protected abstract void initialize();

    public TypeSpec build() {
        if (this.builder == null) initialize();
        return builder.build();
    }
}
