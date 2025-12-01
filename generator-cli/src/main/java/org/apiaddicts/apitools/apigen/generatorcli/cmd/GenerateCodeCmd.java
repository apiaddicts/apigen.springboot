package org.apiaddicts.apitools.apigen.generatorcli.cmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.Project;
import org.apiaddicts.apitools.apigen.generatorcore.generator.ProjectGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenGenerationStrategy;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Command(group = "Code generation")
public class GenerateCodeCmd {

    @Command(command = "generate", alias = "g", description = "Generates code from OpenAPI specification or json file")
    public String generate(
            @Option(longNames = "file", shortNames = 'f', description = "OpenAPI file path") String file,
            @Option(longNames = "config", shortNames = 'c', description = "Config json file path") String config,
            @Option(longNames = "output", shortNames = 'o', defaultValue = "./output", description = "Folder where code is generated") String output,
            @Option(longNames = "zip", defaultValue = "false", description = "If true, output as zipped content") boolean zip
    ) throws Exception {
        Map<String, Object> globalConfig = new HashMap<>();
        globalConfig.put("parentGroup", "org.apiaddicts.apitools.apigen");
        globalConfig.put("parentArtifact", "properties.getParent().getArtifact()");
        globalConfig.put("parentVersion", "2.0.0");
        var generator = new ProjectGenerator<>(new ApigenGenerationStrategy(), globalConfig);
        Project project;
        if (file == null && config == null) {
            throw new IllegalArgumentException("Either file or config must be provided");
        } else if (file != null) {
            var f = new File(file);
            if (!f.exists()) {
                throw new IllegalArgumentException("File " + file + " does not exist");
            }
            project = generator.generate(Files.readAllBytes(f.toPath()));
        } else {
            var f = new File(config);
            if (!f.exists()) {
                throw new IllegalArgumentException("File " + config + " does not exist");
            }
            ObjectMapper mapper = new ObjectMapper();
            var configuration = mapper.readValue(f, Configuration.class);
            project = generator.generate(configuration);
        }
        if (zip) {
            write(project, output);
        } else {
            unzipAndWrite(project, output);
        }
        return "Code generated successfully in " + output;
    }

    private static void write(Project project, String outputFolder) throws IOException {
        File outputDir = new File(outputFolder);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File outputFile = new File(outputFolder, project.getName());
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(project.getContent().getByteArray());
        }
    }

    private static void unzipAndWrite(Project project, String outputFolder) throws IOException {
        File outputDir = new File(outputFolder);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(project.getContent().getByteArray()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File outFile = new File(outputFolder, entry.getName());
                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    outFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }
}
