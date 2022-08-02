package org.apiaddicts.apitools.apigen.generatorcore.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "org.apiaddicts.apitools.apigen.generatorcore")
public class ArchitectureTest {

    @ArchTest
    public static final ArchRule layersSeparation = layeredArchitecture().consideringAllDependencies()
            .layer("DefinitionCommon")
            .definedBy("..generator.components.generic..")
            .layer("DefinitionJava")
            .definedBy("..generator.components.java..")
            .layer("ImplementationCommon")
            .definedBy("..generator.implementations.common..")
            .layer("ImplementationJavaCommon")
            .definedBy("..generator.implementations.java.common..")
            .layer("ImplementationJavaApigen")
            .definedBy("..generator.implementations.java.apigen..")
            .whereLayer("DefinitionJava")
            .mayOnlyBeAccessedByLayers("ImplementationJavaCommon", "ImplementationJavaApigen")
            .whereLayer("ImplementationJavaApigen")
            .mayNotBeAccessedByAnyLayer()
            .whereLayer("ImplementationJavaCommon")
            .mayOnlyBeAccessedByLayers("ImplementationJavaApigen")
            .whereLayer("ImplementationCommon")
            .mayOnlyBeAccessedByLayers("ImplementationJavaCommon", "ImplementationJavaApigen");
}
