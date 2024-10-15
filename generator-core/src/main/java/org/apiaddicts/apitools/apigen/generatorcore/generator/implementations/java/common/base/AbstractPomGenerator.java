package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.base;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.AbstractGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.JavaContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractPomGenerator<C extends JavaContext> extends AbstractGenerator<C> {

    public AbstractPomGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    public void generate(Path projectPath) throws IOException {
        Model model = buildModel();
        addParent(model);
        addProperties(model);
        addDependencies(model);
        addProfiles(model);
        addPlugins(model);
        writeFile(model, projectPath);
    }

    protected Model buildModel() {
        Model model = new Model();
        model.setModelVersion("4.0.0");
        model.setArtifactId(cfg.getArtifact());
        model.setGroupId(cfg.getGroup());
        model.setVersion(cfg.getVersion());
        model.setName(cfg.getName());
        model.setDescription(cfg.getDescription());
        return model;
    }

    protected void addParent(Model model) {
        Parent parent = new Parent();
        parent.setGroupId(ctx.getParentGroup());
        parent.setArtifactId(ctx.getParentArtifact());
        parent.setVersion(ctx.getParentVersion());
        model.setParent(parent);
    }

    protected void addProperties(Model model) {
        model.addProperty("java.version", "21");
        model.addProperty("maven.compiler.source", "21");
        model.addProperty("maven.compiler.target", "21");
        model.addProperty("project.build.sourceEncoding", "UTF-8");
        model.addProperty("project.reporting.outputEncoding", "UTF-8");
    }

    protected void addDependencies(Model model) {
        // Intentional blank
    }

    protected void addProfiles(Model model) {
        // Intentional blank
    }

    protected void addPlugins(Model model) {
        // Intentional blank
    }

    protected void writeFile(Model model, Path path) throws IOException {
        Path pomPath = Paths.get(path.toString(), "/pom.xml");
        File pomFile = pomPath.toFile();
        try (FileOutputStream fos = new FileOutputStream(pomFile)) {
            new MavenXpp3Writer().write(fos, model);
        }
    }
}
