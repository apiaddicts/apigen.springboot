package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.JavaConstants;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationTestBuilder<C extends JavaContext> extends AbstractJavaClassBuilder<C> {

    public ApplicationTestBuilder(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    public String getPackage() {
        return cfg.getBasePackage();
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        buildTestMethod();
    }

    protected void initializeBuilder() {

        builder = TypeSpec.classBuilder("ApplicationTests").addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("org.springframework.boot.test.context", "SpringBootTest"));
    }

    protected void buildTestMethod() {
        MethodSpec methodSpec =
                MethodSpec.methodBuilder("thatContextLoads").returns(TypeName.VOID).addAnnotation(Test.class)
                        .addComment("Intentional blank").build();
        builder.addMethod(methodSpec);
    }

    @Override
    protected Path getPath(Path projectPath) {
        return Paths.get(projectPath.toString(), JavaConstants.TEST_CLASSES_PATH);
    }

}

