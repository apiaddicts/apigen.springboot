package org.apiaddicts.apitools.apigen.generatorcore.generator;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.extractors.ConfigurationExtractor;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.Validator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Context;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.GenerationStrategy;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.Generator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic.GeneratorsAbstractFactory;
import org.apiaddicts.apitools.apigen.generatorcore.spec.OpenAPIExtended;
import org.apiaddicts.apitools.apigen.generatorcore.utils.ZipUtils;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;


@Slf4j
public class ProjectGenerator<C extends Context> {

    private final GenerationStrategy<C> generationStrategy;
    private final Map<String, Object> globalConfig;

    public ProjectGenerator(GenerationStrategy<C> generationStrategy, Map<String, Object> globalConfig) {
        this.generationStrategy = generationStrategy;
        this.globalConfig = globalConfig;
    }

    public Configuration getConfiguration(Path location) {
        return getConfiguration(location.toString());
    }

    public Configuration getConfiguration(String location) {
        OpenAPI openAPI = new OpenAPIParser().readLocation(location, null, getParseOptions()).getOpenAPI();
        return getConfiguration(openAPI);
    }

    public Configuration getConfiguration(byte[] file) {
        OpenAPI openAPI = new OpenAPIParser().readContents(new String(file), null, getParseOptions()).getOpenAPI();
        return getConfiguration(openAPI);
    }

    private Configuration getConfiguration(OpenAPI openAPI) {
        return new ConfigurationExtractor(new OpenAPIExtended(openAPI)).extract();
    }

    private ParseOptions getParseOptions() {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolveFully(true);
        return parseOptions;
    }

    public Project generate(Path location) throws IOException {
        return generate(location.toString());
    }

    public Project generate(String location) throws IOException {
        return generate(getConfiguration(location));
    }

    public Project generate(byte[] file) throws IOException {
        return generate(getConfiguration(file));
    }

    public Project generate(Configuration config) throws IOException {

        Validator.validate(config);

        ByteArrayResource response;
        Path projectFolder = Files.createTempDirectory("apigen-project-folder-" + System.nanoTime());
        try {
            GeneratorsAbstractFactory<C> generatorsFactory = generationStrategy.getGeneratorsFactory();
            C context = generationStrategy.getContext(globalConfig);

            for (Generator generator : generatorsFactory.createDefault(context, config)) {
                generator.generate(projectFolder);
            }
            boolean partial = config.getPartial();
            if (!partial) {
                for (Generator generator : generatorsFactory.createNonPartial(context, config)) {
                    generator.generate(projectFolder);
                }
            }

            Path zipPath = ZipUtils.zip(projectFolder);
            response = new ByteArrayResource(Files.readAllBytes(zipPath));
            Files.delete(zipPath);

            return new Project(config.getName() + ".zip", response);
        } finally {
            if (projectFolder != null) FileUtils.deleteDirectory(projectFolder.toFile());
        }
    }
}
