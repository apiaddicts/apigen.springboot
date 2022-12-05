package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.mapper;

import com.squareup.javapoet.TypeName;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.config.ConfigurationObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContextObjectMother;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.persistence.JavaEntitiesData;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.JavaResourceDataSubEntity;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.web.resource.JavaResourcesData;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;

public class ApigenMapperBuilderObjectMother {

    public static ApigenMapperBuilder<ApigenContext> create(
            Entity entity,
            TypeName idType,
            Set<String> relatedEntities,
            Set<String> basicAttributes,
            Set<TypeName> inputResources,
            Set<TypeName> outputResources,
            List<JavaResourceDataSubEntity> resourceDataSubEntity
    ) {
        Configuration cfg = ConfigurationObjectMother.create();

        JavaEntitiesData entitiesData = Mockito.mock(JavaEntitiesData.class);
        Mockito.when(entitiesData.getIDType(entity.getName())).thenReturn(idType);
        Mockito.when(entitiesData.getRelatedEntities(entity.getName())).thenReturn(relatedEntities);
        Mockito.when(entitiesData.getBasicAttributes(anyString())).thenReturn(basicAttributes);

        JavaResourcesData resourcesData = Mockito.mock(JavaResourcesData.class);
        Mockito.when(resourcesData.getInputResources(entity.getName())).thenReturn(inputResources);
        Mockito.when(resourcesData.getOutputResources(entity.getName())).thenReturn(outputResources);
        Mockito.when(resourcesData.getResourceDataSubEntity(entity.getName())).thenReturn(resourceDataSubEntity);

        ApigenContext ctx = ApigenContextObjectMother.create();
        ctx.setEntitiesData(entitiesData);
        ctx.setResourcesData(resourcesData);

        return new ApigenMapperBuilder<>(entity, ctx, cfg);
    }
}
