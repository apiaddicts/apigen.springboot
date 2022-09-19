package org.apiaddicts.apitools.apigen.generatorcore.config.extractors;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.extractors.context.ExtractorContext;
import org.apiaddicts.apitools.apigen.generatorcore.exceptions.DefinitionException;
import org.apiaddicts.apitools.apigen.generatorcore.exceptions.ExtractorException;
import org.apiaddicts.apitools.apigen.generatorcore.spec.OpenAPIExtended;
import org.apiaddicts.apitools.apigen.generatorcore.spec.components.ApigenProject;

@Slf4j
public class ConfigurationExtractor extends AbstractExtractor {

    private final OpenAPIExtended openAPIExtended;
    private final EntitiesExtractor entitiesExtractor;
    private final ControllersExtractor controllersExtractor;

    public ConfigurationExtractor(OpenAPIExtended openAPIExtended) {
        super(new ExtractorContext());
        this.openAPIExtended = openAPIExtended;
        this.entitiesExtractor = new EntitiesExtractor(context);
        this.controllersExtractor = new ControllersExtractor(openAPIExtended.getSchemas());
    }

    public Configuration extract() {
        try {
            Configuration configuration = extractProject(openAPIExtended.getProject());
            configuration.setEntities(entitiesExtractor.getEntities(openAPIExtended.getModels()));
            configuration.setControllers(
                    controllersExtractor.getControllers(
                            openAPIExtended.getPaths(),
                            openAPIExtended.getPathBindings(),
                            configuration.getEntities()
                    )
            );
            context.validate();
            return configuration;
        } catch (ExtractorException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error: ", e);
            throw new DefinitionException();
        }
    }

    private Configuration extractProject(ApigenProject project) {
        Configuration configuration = new Configuration();
        configuration.setName(project.getName());
        configuration.setDescription(project.getDescription());
        configuration.setGroup(project.getJavaProperties().getGroupId());
        configuration.setArtifact(project.getJavaProperties().getArtifactId());
        configuration.setVersion(project.getVersion());
        configuration.setBasePackage(project.getJavaProperties().getBasePackage());
        if (configuration.getBasePackage() == null) {
            configuration.setBasePackage(configuration.getGroup() + "." + configuration.getArtifact());
        }
        configuration.setPartial(project.getPartial());
        configuration.setStandardResponseOperations(project.getStandardResponseOperations());
        return configuration;
    }
}
