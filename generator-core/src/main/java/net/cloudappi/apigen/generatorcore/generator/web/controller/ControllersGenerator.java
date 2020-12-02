package net.cloudappi.apigen.generatorcore.generator.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.generatorcore.config.controller.Controller;
import net.cloudappi.apigen.generatorcore.generator.common.AbstractClassBuilder;
import net.cloudappi.apigen.generatorcore.generator.common.AbstractGenerator;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntitiesData;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class ControllersGenerator extends AbstractGenerator {

    private ArrayList<ControllerBuilder> builders = new ArrayList<>();

    public ControllersGenerator(Collection<Controller> controllers, EntitiesData entitiesData, String basePackage) {
        controllers.stream()
                .map(c -> ControllerBuilderFactory.create(c, entitiesData, basePackage))
                .forEach(builders::add);
    }

    @Override
    protected Collection<AbstractClassBuilder> getBuilders() {
        return new ArrayList<>(builders);
    }
}
