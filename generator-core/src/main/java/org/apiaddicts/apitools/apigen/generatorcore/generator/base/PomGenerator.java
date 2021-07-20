package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import org.apache.maven.model.*;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PomGenerator {

    private PomGenerator() {
        // Intentional blank
    }

    public static void generate(Configuration configuration, String parentGroup, String parentArtifact, String parentVersion, File projectFolder) throws IOException {
        Model model = buildModel(configuration);
        addParent(model, parentGroup, parentArtifact, parentVersion);
        addProperties(model);
        addDependencies(model);
        addProfiles(model);
        addPlugins(model);
        writeFile(model, projectFolder);
    }

    private static Model buildModel(Configuration configuration) {
        Model model = new Model();
        model.setModelVersion("4.0.0");
        model.setArtifactId(configuration.getArtifact());
        model.setGroupId(configuration.getGroup());
        model.setVersion(configuration.getVersion());
        model.setName(configuration.getName());
        model.setDescription(configuration.getDescription());
        return model;
    }

    private static void addParent(Model model, String group, String artifact, String version) {
        Parent parent = new Parent();
        parent.setGroupId(group);
        parent.setArtifactId(artifact);
        parent.setVersion(version);
        model.setParent(parent);
    }

    private static void addProperties(Model model) {
        model.addProperty("java.version", "1.8");
        model.addProperty("maven.compiler.source", "1.8");
        model.addProperty("maven.compiler.target", "1.8");
        model.addProperty("project.build.sourceEncoding", "UTF-8");
        model.addProperty("project.reporting.outputEncoding", "UTF-8");
        model.addProperty("h2.scope", "test");
        model.addProperty("mapstruct.version", "1.4.2.Final");
        model.addProperty("mapstruct-binding.version", "0.2.0");
    }

    private static void addDependencies(Model model) {
        model.addDependency(getDependency("com.h2database", "h2", null, "${h2.scope}"));
        model.addDependency(getDependency("org.apiaddicts.apitools.apigen", "boot-starter", "0.1.0-SNAPSHOT", null));
        Dependency springTestDependency = getDependency("org.springframework.boot", "spring-boot-starter-test", null, "test");
        Exclusion exclusion = new Exclusion();
        exclusion.setGroupId("org.junit.vintage");
        exclusion.setArtifactId("junit-vintage-engine");
        springTestDependency.addExclusion(exclusion);
        model.addDependency(springTestDependency);
        // TODO test dep add
    }

    private static Dependency getDependency(String groupId, String artifactId, String version, String scope) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        dependency.setScope(scope);
        return dependency;
    }

    private static void addProfiles(Model model) {
        Profile h2Profile = new Profile();
        h2Profile.setId("h2");
        Properties properties = new Properties();
        properties.setProperty("h2.scope", "runtime");
        h2Profile.setProperties(properties);
        model.addProfile(h2Profile);
    }

    private static void addPlugins(Model model) {
        Build build = new Build();
        build.addPlugin(getSpringBootPlugin());
        build.addPlugin(getMavenCompilerPlugin());
        model.setBuild(build);
    }

    private static Plugin getSpringBootPlugin() {
        Plugin plugin = new Plugin();
        plugin.setGroupId("org.springframework.boot");
        plugin.setArtifactId("spring-boot-maven-plugin");
        return plugin;
    }

    private static Plugin getMavenCompilerPlugin() {
        Plugin plugin = new Plugin();
        plugin.setGroupId("org.apache.maven.plugins");
        plugin.setArtifactId("maven-compiler-plugin");
        plugin.setVersion("3.8.1");

        final Xpp3Dom configuration = new Xpp3Dom( "configuration" );

        final Xpp3Dom source = new Xpp3Dom( "source" );
        source.setValue("${java.version}");
        configuration.addChild(source);

        final Xpp3Dom target = new Xpp3Dom( "target" );
        target.setValue("${java.version}");
        configuration.addChild(target);

        final Xpp3Dom annotationProcessorPaths = new Xpp3Dom( "annotationProcessorPaths" );
        annotationProcessorPaths.addChild(getMavenCompilerPluginConfigPath("org.mapstruct", "mapstruct-processor", "${mapstruct.version}"));
        annotationProcessorPaths.addChild(getMavenCompilerPluginConfigPath("org.projectlombok", "lombok", "${lombok.version}"));
        annotationProcessorPaths.addChild(getMavenCompilerPluginConfigPath("org.projectlombok", "lombok-mapstruct-binding", "${mapstruct-binding.version}"));
        configuration.addChild(annotationProcessorPaths);

        plugin.setConfiguration(configuration);

        return plugin;
    }

    private static Xpp3Dom getMavenCompilerPluginConfigPath(String groupId, String artifactId, String version) {
        final Xpp3Dom path = new Xpp3Dom( "path" );
        final Xpp3Dom groupIdNode = new Xpp3Dom( "groupId" );
        groupIdNode.setValue(groupId);
        final Xpp3Dom artifactIdNode = new Xpp3Dom( "artifactId" );
        artifactIdNode.setValue(artifactId);
        final Xpp3Dom versionNode = new Xpp3Dom( "version" );
        versionNode.setValue(version);
        path.addChild(groupIdNode);
        path.addChild(artifactIdNode);
        path.addChild(versionNode);
        return path;
    }

    private static void writeFile(Model model, File projectFolder) throws IOException {
        Path pomPath = Paths.get(projectFolder.getPath(), "/pom.xml");
        File pomFile = pomPath.toFile();
        try (FileOutputStream fos = new FileOutputStream(pomFile)) {
            new MavenXpp3Writer().write(fos, model);
        }
    }
}
