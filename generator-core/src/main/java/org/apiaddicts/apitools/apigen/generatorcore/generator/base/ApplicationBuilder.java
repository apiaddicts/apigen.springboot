package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import com.squareup.javapoet.*;
import org.apiaddicts.apitools.apigen.archetypecore.autoconfigure.ApigenApplication;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractClassBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

import javax.annotation.PostConstruct;
import javax.lang.model.element.Modifier;
import java.util.TimeZone;

@Deprecated
public class ApplicationBuilder extends AbstractClassBuilder {

    private String basePackage;

    public ApplicationBuilder(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public String getPackage() {
        return basePackage;
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addMainMethod();
        initializeTimezone();
    }

    private void initializeBuilder() {
        builder = TypeSpec
                .classBuilder("Application")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ApigenApplication.class)
                .addAnnotation(SpringBootApplication.class);
    }

    private void addMainMethod() {
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(TypeName.get(String[].class), "args").build())
                .returns(TypeName.VOID)
                .addStatement(CodeBlock.of("$T springApplication = new SpringApplication(Application.class)", SpringApplication.class))
                .addStatement(CodeBlock.of("springApplication.addListeners(new $T())", ApplicationPidFileWriter.class))
                .addStatement(CodeBlock.of("$T.run(Application.class, args)", SpringApplication.class))
                .build();
        builder.addMethod(methodSpec);
    }

    private void initializeTimezone() {
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("init")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(PostConstruct.class)
                .returns(TypeName.VOID)
                .addStatement("$1T.setDefault($1T.getTimeZone($2S))", TimeZone.class, "UTC")
                .build();
        builder.addMethod(methodSpec);
    }
}
