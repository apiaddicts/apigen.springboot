package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;

import com.squareup.javapoet.*;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.java.AbstractJavaClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

import jakarta.annotation.PostConstruct;
import javax.lang.model.element.Modifier;
import java.util.TimeZone;

public class ApplicationBuilder<C extends JavaContext> extends AbstractJavaClassBuilder<C> {

    public ApplicationBuilder(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    public String getPackage() {
        return cfg.getBasePackage();
    }

    @Override
    protected void initialize() {
        initializeBuilder();
        addMainMethod();
        initializeTimezone();
    }

    protected void initializeBuilder() {
        builder = TypeSpec.classBuilder("Application").addModifiers(Modifier.PUBLIC)
                .addAnnotation(SpringBootApplication.class);
    }

    protected void addMainMethod() {
        MethodSpec methodSpec = MethodSpec.methodBuilder("main").addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(TypeName.get(String[].class), "args").build())
                .returns(TypeName.VOID).addStatement(
                        CodeBlock.of("$T springApplication = new SpringApplication(Application.class)",
                                SpringApplication.class))
                .addStatement(CodeBlock.of("springApplication.addListeners(new $T())", ApplicationPidFileWriter.class))
                .addStatement(CodeBlock.of("$T.run(Application.class, args)", SpringApplication.class)).build();
        builder.addMethod(methodSpec);
    }

    protected void initializeTimezone() {
        MethodSpec methodSpec =
                MethodSpec.methodBuilder("init").addModifiers(Modifier.PUBLIC).addAnnotation(PostConstruct.class)
                        .returns(TypeName.VOID)
                        .addStatement("$1T.setDefault($1T.getTimeZone($2S))", TimeZone.class, "UTC").build();
        builder.addMethod(methodSpec);
    }

}
