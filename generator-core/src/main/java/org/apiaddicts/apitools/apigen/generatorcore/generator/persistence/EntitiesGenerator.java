package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractClassBuilder;
import org.apiaddicts.apitools.apigen.generatorcore.generator.common.AbstractGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
@Slf4j
public class EntitiesGenerator extends AbstractGenerator {

    private List<EntityBuilder> builders = new ArrayList<>();
    private List<ComposedIdBuilder> idBuilders = new ArrayList<>();
    private EntitiesData entitiesData;

    public EntitiesGenerator(List<Entity> entities, String basePackage) {
        EntityRelationManager entityRelationManager = new EntityRelationManager(entities);
        entities.forEach(e -> {
            EntityBuilder entityBuilder = new EntityBuilder(e, entityRelationManager, basePackage);
            builders.add(entityBuilder);
            if (entityBuilder.hasComposedID()) idBuilders.add(new ComposedIdBuilder(e, basePackage));
        });
        entitiesData = new EntitiesData(builders.stream().map(AbstractClassBuilder::build).collect(Collectors.toList()));
    }

    @Override
    protected Collection<AbstractClassBuilder> getBuilders() {
        List allBuilders = new ArrayList<>(builders);
        allBuilders.addAll(idBuilders);
        return allBuilders;
    }

    public EntitiesData getEntitiesData() {
        return entitiesData;
    }
}
