package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.common.base;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.AbstractGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GitIgnoreFileGenerator<C extends Context> extends AbstractGenerator<C> {

    public GitIgnoreFileGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg);
    }

    @Override
    public void generate(Path projectPath) throws IOException {
        String gitignore = ".gitignore";
        Path gitignorePath = projectPath.resolve(gitignore);
        Files.write(gitignorePath, getLines());
    }

    protected List<String> getLines() {
        List<String> lines = new ArrayList<>();
        lines.add("/target");
        lines.add("");
        lines.add("### STS ###");
        lines.add(".apt_generated");
        lines.add(".classpath");
        lines.add(".factorypath");
        lines.add(".project");
        lines.add(".settings");
        lines.add(".springBeans");
        lines.add(".sts4-cache");
        lines.add("");
        lines.add("### IntelliJ IDEA ###");
        lines.add(".idea");
        lines.add("*.iws");
        lines.add("*.iml");
        lines.add("*.ipr");
        lines.add("");
        lines.add("### NetBeans ###");
        lines.add("/nbproject/private/");
        lines.add("/nbbuild/");
        lines.add("/dist/");
        lines.add("/nbdist/");
        lines.add("/.nb-gradle/");
        lines.add("build/");
        lines.add("");
        lines.add("### VS Code ###");
        lines.add(".vscode/");
        return lines;
    }
}
