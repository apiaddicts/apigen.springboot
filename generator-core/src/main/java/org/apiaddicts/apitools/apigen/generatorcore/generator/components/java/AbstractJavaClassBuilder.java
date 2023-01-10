package org.apiaddicts.apitools.apigen.generatorcore.generator.components.java;

import com.squareup.javapoet.*;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.AbstractFileBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Context;

import jakarta.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractJavaClassBuilder<C extends Context> extends AbstractFileBuilder<C> {
    protected TypeSpec.Builder builder;

    public AbstractJavaClassBuilder(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    protected static String concatPackage(String basePackage, String newPackage) {
        return String.format("%s.%s", basePackage, newPackage.toLowerCase());
    }

    protected static String concatPackage(String basePackage, String newPackage, String otherPackage) {
        return String.format("%s.%s.%s", basePackage, newPackage.toLowerCase(), otherPackage);
    }

    protected TypeSpec.Builder getClass(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }

    protected TypeSpec.Builder getInnerClass(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC);
    }

    protected TypeSpec.Builder getPublicInnerClass(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
    }

    protected TypeSpec.Builder getInterface(String name) {
        return TypeSpec.interfaceBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }

    protected AnnotationSpec.Builder getAnnotation(Class clazz) {
        return AnnotationSpec.builder(clazz);
    }

    protected FieldSpec.Builder getField(TypeName type, String name) {
        return FieldSpec.builder(type, name, Modifier.PRIVATE);
    }

    // TODO: Review visibility
    public abstract String getPackage();

    protected abstract void initialize();

    // TODO: Review visibility
    public TypeSpec build() {
        if (this.builder == null) initialize();
        return builder.build();
    }

    protected JavaFile toJavaFile(String basePackage, TypeSpec spec) {
        return JavaFile.builder(basePackage, spec)
                .indent("    ")
                .skipJavaLangImports(true)
                .build();
    }

    @Override
    public void generate(Path projectPath) throws IOException {
        toJavaFile(getPackage(), build()).writeTo(getPath(projectPath));
    }

    protected Path getPath(Path projectPath) {
        return Paths.get(projectPath.toString(), JavaConstants.CLASSES_PATH);
    }

}
