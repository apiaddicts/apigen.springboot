package org.apiaddicts.apitools.apigen.generatorcli.cmd;

import tools.jackson.databind.json.JsonMapper;
import org.apiaddicts.apitools.apigen.generatorcore.generator.ProjectGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenGenerationStrategy;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.CommandGroup;
import org.springframework.shell.core.command.annotation.Option;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@CommandGroup(name = "Config generation")
public class GenerateConfigCmd {

    @Command(name = "generate-config", alias = "gc", description = "Generates config json from OpenAPI specification", exitStatusExceptionMapper = "customExceptionResolver")
    public String generate(
            @Option(longName = "file", shortName = 'f', required = true, description = "OpenAPI file path") String file,
            @Option(longName = "output", shortName = 'o', defaultValue = "./output", description = "Folder where config is generated") String output
    ) throws Exception {
        Map<String, Object> globalConfig = new HashMap<>();
        globalConfig.put("parentGroup", "org.apiaddicts.apitools.apigen");
        globalConfig.put("parentArtifact", "archetype-parent-spring-boot");
        globalConfig.put("parentVersion", "2.0.1");
        var generator = new ProjectGenerator<>(new ApigenGenerationStrategy(), globalConfig);
        var f = new File(file);
        if (!f.exists()) {
            throw new IllegalArgumentException("File " + file + " does not exist");
        }
        var config = generator.getConfiguration((Files.readAllBytes(f.toPath())));
        var mapper = JsonMapper.builder().build();
        var configJson = mapper.writeValueAsBytes(config);
        File outputDir = new File(output);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File outputFile = new File(output, "config.json");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(configJson);
        }
        return "Configuration generated successfully in " + output;
    }
}
