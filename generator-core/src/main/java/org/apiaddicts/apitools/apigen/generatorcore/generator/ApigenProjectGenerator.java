package org.apiaddicts.apitools.apigen.generatorcore.generator;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.extractors.ConfigurationExtractor;
import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Controller;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.base.PomGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.base.ProjectStructureGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.base.SpringBootBaseGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.Validator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.base.GitIgnoreGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.base.LombokConfigGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.mapper.MappersGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.EntitiesGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.persistence.repository.RepositoriesGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.service.RelationManagersGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.service.ServicesGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.ControllersGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.ResourcesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.resource.ResourcesGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.web.response.ResponsesGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.spec.OpenAPIExtended;
import org.apiaddicts.apitools.apigen.generatorcore.utils.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ByteArrayResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Slf4j
@Deprecated
public class ApigenProjectGenerator {

    private static final String JAVA_DIR = "src/main/java";

    private final String parentGroup;
    private final String parentArtifact;
    private final String parentVersion;

    public ApigenProjectGenerator(String parentGroup, String parentArtifact, String parentVersion) {
        this.parentGroup = parentGroup;
        this.parentArtifact = parentArtifact;
        this.parentVersion = parentVersion;
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
        File projectFolder = null;
        try {
            projectFolder = ProjectStructureGenerator.generate(config);

            boolean partial = config.getPartial();
            generateCode(projectFolder, config.getBasePackage(), config.getEntities(), config.getControllers());
            if (!partial) {
                generateCoreCode(projectFolder, config);
            }

            Path zipPath = ZipUtils.zip(Paths.get(projectFolder.toURI()));
            response = new ByteArrayResource(Files.readAllBytes(zipPath));
            Files.delete(zipPath);

            return new Project(config.getName() + ".zip", response);
        } finally {
            if (projectFolder != null) FileUtils.deleteDirectory(projectFolder);
        }
    }

    private void generateCode(File projectFolder, String basePackage, List<Entity> entities, List<Controller> controllers) {
        Path filesRootPath = Paths.get(projectFolder.getPath(), JAVA_DIR);

        EntitiesGenerator entitiesGenerator = new EntitiesGenerator(entities, basePackage);
        EntitiesData entitiesData = entitiesGenerator.getEntitiesData();
        entitiesGenerator.generate(filesRootPath);

        new RepositoriesGenerator(entities, entitiesData, basePackage).generate(filesRootPath);
        new ServicesGenerator(entities, entitiesData, basePackage).generate(filesRootPath);
        new RelationManagersGenerator(entities, entitiesData, basePackage).generate(filesRootPath);

        ResourcesGenerator resourcesGenerator = new ResourcesGenerator(controllers, basePackage);
        ResourcesData resourcesData = resourcesGenerator.getResourcesData();
        resourcesGenerator.generate(filesRootPath);

        new MappersGenerator(entities, entitiesData, resourcesData, basePackage).generate(filesRootPath);
        new ResponsesGenerator(controllers, basePackage).generate(filesRootPath);
        new ControllersGenerator(controllers, entitiesData, basePackage).generate(filesRootPath);
    }

    private void generateCoreCode(File projectFolder, Configuration config) throws IOException {
        PomGenerator.generate(config, parentGroup, parentArtifact, parentVersion, projectFolder);
        GitIgnoreGenerator.generate(projectFolder);
        LombokConfigGenerator.generate(projectFolder);
        SpringBootBaseGenerator.generate(config, projectFolder);
    }
}
