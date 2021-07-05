package org.apiaddicts.apitools.apigen.generatorcore.generator.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GitIgnoreGenerator {

    private GitIgnoreGenerator() {
        // Intentional blank
    }

    public static void generate(File projectFolder) throws IOException {
        String folder = projectFolder.getPath();
        String gitignore = ".gitignore";
        Path folderPath = Paths.get(folder);
        Path gitignorePath = folderPath.resolve(gitignore);

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

        Files.write(gitignorePath, lines);
    }
}
