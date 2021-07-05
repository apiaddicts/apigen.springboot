package org.apiaddicts.apitools.apigen.generatorcore.generator.common;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.Collection;

@Slf4j
public abstract class AbstractGenerator {

    private static final String INDENT = "    ";

    protected abstract Collection<AbstractClassBuilder> getBuilders();

    public boolean generate(Path path) {
        return getBuilders().stream().allMatch(builder -> write(builder.getPackage(), builder.build(), path));
    }

    private boolean write(String packageName, TypeSpec spec, Path path) {
        try {
            JavaFile javaFile = JavaFile.builder(packageName, spec)
                    .indent(INDENT)
                    .skipJavaLangImports(true)
                    .build();
            javaFile.writeTo(path);
            return true;
        } catch (Exception e) {
            log.error("Error writing class", e);
            return false;
        }
    }
}
