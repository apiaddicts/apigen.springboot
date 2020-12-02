package net.cloudappi.apigen.generatorcore.generator.base;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
                .addAnnotation(SpringBootTest.class);
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
