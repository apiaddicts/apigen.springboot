package net.cloudappi.apigen.generatorcore.generator.persistence.repository;

import com.squareup.javapoet.TypeName;
import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.generatorcore.config.entity.Entity;
import net.cloudappi.apigen.generatorcore.generator.common.AbstractClassBuilder;
import net.cloudappi.apigen.generatorcore.generator.common.AbstractGenerator;
import net.cloudappi.apigen.generatorcore.generator.persistence.EntitiesData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RepositoriesGenerator extends AbstractGenerator {

    private Map<String, RepositoryBuilder> builders = new HashMap<>();

    public RepositoriesGenerator(Collection<Entity> entities, EntitiesData entitiesData, String basePackage) {
        entities.forEach(entity -> {
            String name = entity.getName();
            TypeName idTypeName = entitiesData.getIDType(name);
            builders.put(name, new RepositoryBuilder(name, basePackage, idTypeName));
        });
    }

    @Override
    protected Collection<AbstractClassBuilder> getBuilders() {
        return new ArrayList<>(builders.values());
    }

}
