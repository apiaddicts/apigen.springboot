package net.cloudappi.apigen.generatorcore.generator.service;

import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.generatorcore.config.entity.Entity;
import net.cloudappi.apigen.generatorcore.generator.common.AbstractClassBuilder;
import net.cloudappi.apigen.generatorcore.generator.common.AbstractGenerator;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntitiesData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class RelationManagersGenerator extends AbstractGenerator {

    private List<RelationManagerBuilder> builders = new ArrayList<>();

    public RelationManagersGenerator(Collection<Entity> entities, EntitiesData entitiesData, String basePackage) {
        for (Entity entity : entities) {
            builders.add(new RelationManagerBuilder(entity.getName(), basePackage, entitiesData.getAttributes(entity.getName())));
        }
    }

    @Override
    protected Collection<AbstractClassBuilder> getBuilders() {
        return new ArrayList<>(builders);
    }
}
