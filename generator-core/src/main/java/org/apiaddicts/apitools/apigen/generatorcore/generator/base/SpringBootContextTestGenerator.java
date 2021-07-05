package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import com.squareup.javapoet.*;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

public class SpringBootContextTestGenerator {

    private SpringBootContextTestGenerator() {
        // Intentional blank
    }

    public static JavaFile generate(String basePackage) {
        TypeSpec.Builder applicationBuilder = getTestBuilder();
        applicationBuilder.addMethod(buildTestMethod());
        return toJavaFile(basePackage, applicationBuilder);
    }

    private static TypeSpec.Builder getTestBuilder() {
        return TypeSpec
                .classBuilder("ApplicationTests")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("org.springframework.boot.test.context", "SpringBootTest"));
    }

    private static MethodSpec buildTestMethod() {
        return MethodSpec
                .methodBuilder("thatContextLoads")
                .returns(TypeName.VOID)
                .addAnnotation(Test.class)
                .addComment("Intentional blank")
                .build();
    }

    private static JavaFile toJavaFile(String basePackage, TypeSpec.Builder builder) {
        return JavaFile.builder(basePackage, builder.build())
                .indent("    ")
                .build();
    }

}
