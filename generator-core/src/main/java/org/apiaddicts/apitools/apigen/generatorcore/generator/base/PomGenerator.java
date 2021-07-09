package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Profile;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

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
    }

    private static void addDependencies(Model model) {
        Dependency h2Dependency = new Dependency();
        h2Dependency.setGroupId("com.h2database");
        h2Dependency.setArtifactId("h2");
        h2Dependency.setScope("${h2.scope}");
        model.addDependency(h2Dependency);
    }

    private static void addProfiles(Model model) {
        Profile h2Profile = new Profile();
        h2Profile.setId("h2");
        Properties properties = new Properties();
        properties.setProperty("h2.scope", "runtime");
        h2Profile.setProperties(properties);
        model.addProfile(h2Profile);
    }

    private static void writeFile(Model model, File projectFolder) throws IOException {
        Path pomPath = Paths.get(projectFolder.getPath(), "/pom.xml");
        File pomFile = pomPath.toFile();
        try (FileOutputStream fos = new FileOutputStream(pomFile)) {
            new MavenXpp3Writer().write(fos, model);
        }
    }
}
