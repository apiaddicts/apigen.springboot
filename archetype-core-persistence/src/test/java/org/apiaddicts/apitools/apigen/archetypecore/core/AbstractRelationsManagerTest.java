package org.apiaddicts.apitools.apigen.archetypecore.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenRepository;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrors;
import org.apiaddicts.apitools.apigen.archetypecore.exceptions.RelationalErrorsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AbstractRelationsManagerTest {

    private RelationsManager relationsManager;
    private OtherEntityService otherEntityService;

    @BeforeEach
    void beforeEach() {
        otherEntityService = mock(OtherEntityService.class);
        relationsManager = new RelationsManager(otherEntityService);
    }

    @Test
    void givenWithoutRelation_whenCreate_thenSuccess() {
        Entity entity = new Entity();
        relationsManager.createOrRetrieveRelations(entity);
        verifyNoInteractions(otherEntityService);
    }

    @Test
    void givenNonPersistedSimpleRelation_whenCreate_thenSuccess() {
        Entity entity = new Entity();
        entity.setOtherEntity(new OtherEntity());
        relationsManager.createOrRetrieveRelations(entity);
        verify(otherEntityService).create(any(OtherEntity.class));
        verifyNoMoreInteractions(otherEntityService);
    }

    @Test
    void givenPersistedSimpleRelation_whenCreate_thenSuccess() {
        Entity entity = new Entity();
        entity.setOtherEntity(new OtherEntity(1L, null));
        when(otherEntityService.getOne(eq(1L))).thenReturn(Optional.of(new OtherEntity(1L, "Test")));
        relationsManager.createOrRetrieveRelations(entity);
        verify(otherEntityService).getOne(any());
        verifyNoMoreInteractions(otherEntityService);
    }

    @Test
    void givenNonExistentPersistedSimpleRelation_whenCreate_thenSuccess() {
        Entity entity = new Entity();
        entity.setOtherEntity(new OtherEntity(1L, null));
        when(otherEntityService.getOne(eq(1L))).thenReturn(Optional.empty());
        RelationalErrorsException exception = assertThrows(RelationalErrorsException.class, () -> {
            relationsManager.createOrRetrieveRelations(entity);
        });
        assertEquals(1, exception.getRelationalErrors().getErrors().size());
        assertEquals(OtherEntity.class, exception.getRelationalErrors().getErrors().get(0).getClazz());
        assertEquals(1L, exception.getRelationalErrors().getErrors().get(0).getId());
        verify(otherEntityService).getOne(any());
        verifyNoMoreInteractions(otherEntityService);
    }

    @Test
    void givenNonPersistedListRelation_whenCreate_thenSuccess() {
        Entity entity = new Entity();
        entity.setOtherEntityList(new HashSet<>(Arrays.asList(new OtherEntity(), new OtherEntity())));
        relationsManager.createOrRetrieveRelations(entity);
        verify(otherEntityService, times(2)).create(any(OtherEntity.class));
        verifyNoMoreInteractions(otherEntityService);
    }

    @Test
    void givenPersistedListRelation_whenCreate_thenSuccess() {
        Entity entity = new Entity();
        entity.setOtherEntityList(new HashSet<>(Arrays.asList(new OtherEntity(1L, null), new OtherEntity(2L, null))));
        when(otherEntityService.getOne(eq(1L))).thenReturn(Optional.of(new OtherEntity(1L, "Test")));
        when(otherEntityService.getOne(eq(2L))).thenReturn(Optional.of(new OtherEntity(2L, "Test")));
        relationsManager.createOrRetrieveRelations(entity);
        verify(otherEntityService, times(2)).getOne(any());
        verifyNoMoreInteractions(otherEntityService);
    }

    @Test
    void givenNonExistentPersistedListRelation_whenCreate_thenSuccess() {
        Entity entity = new Entity();
        entity.setOtherEntityList(new HashSet<>(Arrays.asList(new OtherEntity(1L, null), new OtherEntity(2L, null))));
        when(otherEntityService.getOne(eq(1L))).thenReturn(Optional.empty());
        when(otherEntityService.getOne(eq(2L))).thenReturn(Optional.empty());

        RelationalErrorsException exception = assertThrows(RelationalErrorsException.class, () -> {
            relationsManager.createOrRetrieveRelations(entity);
        });
        assertEquals(2, exception.getRelationalErrors().getErrors().size());
        assertEquals(OtherEntity.class, exception.getRelationalErrors().getErrors().get(0).getClazz());
        assertEquals(1L, exception.getRelationalErrors().getErrors().get(0).getId());
        assertEquals(OtherEntity.class, exception.getRelationalErrors().getErrors().get(1).getClazz());
        assertEquals(2L, exception.getRelationalErrors().getErrors().get(1).getId());
        verify(otherEntityService, times(2)).getOne(any());
        verifyNoMoreInteractions(otherEntityService);
    }

    @Test
    void givenWithoutRelation_whenUpdate_thenSuccess() {
        Entity entity = new Entity();
        Entity persistedEntity = new Entity();
        relationsManager.updateRelations(persistedEntity, entity, new HashSet<>());
        verifyNoInteractions(otherEntityService);
    }

    @Test
    void givenPersistedSimpleRelation_whenUpdate_thenSuccess() {
        Entity entity = new Entity();
        entity.setOtherEntity(new OtherEntity(1L, null));
        Entity persistedEntity = new Entity();
        when(otherEntityService.getOne(eq(1L))).thenReturn(Optional.of(new OtherEntity(1L, "Test")));
        relationsManager.updateRelations(persistedEntity, entity, new HashSet<>(Arrays.asList("otherEntity")));
        verify(otherEntityService).getOne(any());
        verifyNoMoreInteractions(otherEntityService);
    }

    @Test
    void givenNonExistentPersistedSimpleRelation_whenUpdate_thenSuccess() {
        Entity entity = new Entity();
        entity.setOtherEntity(new OtherEntity(1L, null));
        Entity persistedEntity = new Entity();
        when(otherEntityService.getOne(eq(1L))).thenReturn(Optional.empty());
        RelationalErrorsException exception = assertThrows(RelationalErrorsException.class, () -> {
            relationsManager.updateRelations(persistedEntity, entity, new HashSet<>(Arrays.asList("otherEntity")));
        });
        assertEquals(1, exception.getRelationalErrors().getErrors().size());
        assertEquals(OtherEntity.class, exception.getRelationalErrors().getErrors().get(0).getClazz());
        assertEquals(1L, exception.getRelationalErrors().getErrors().get(0).getId());
        verify(otherEntityService).getOne(any());
        verifyNoMoreInteractions(otherEntityService);
    }

    // TODO add list tests and combination error

    @AllArgsConstructor
    private static class RelationsManager extends AbstractRelationsManager<Entity> {

        private OtherEntityService otherEntityService;

        @Transactional(propagation = Propagation.MANDATORY)
        public void createOrRetrieveRelations(Entity entity) {
            RelationalErrors errors = new RelationalErrors();
            createOrRetrieveRelationsOtherEntity(entity, errors);
            createOrRetrieveRelationsOtherEntityList(entity, errors);
            if (!errors.isEmpty()) {
                throw new RelationalErrorsException(errors);
            }
        }

        protected void createOrRetrieveRelationsOtherEntity(Entity entity, RelationalErrors errors) {
            entity.setOtherEntity(createOrRetrieve(entity.getOtherEntity(), otherEntityService, errors));
        }

        protected void createOrRetrieveRelationsOtherEntityList(Entity entity, RelationalErrors errors) {
            entity.setOtherEntityList(createOrRetrieve(entity.getOtherEntityList(), otherEntityService, errors));
        }

        @Transactional(propagation = Propagation.MANDATORY)
        public void updateRelations(Entity persistedEntity, Entity entity, Set<String> fields) {
            RelationalErrors errors = new RelationalErrors();
            boolean updateAll = (fields == null);
            if (updateAll || fields.contains("otherEntity")) {
                updateRelationsOtherEntity(persistedEntity, entity, fields, errors);
            }
            if (updateAll || fields.contains("owherEntityList")) {
                updateRelationsOtherEntityList(persistedEntity, entity, fields, errors);
            }
            if (!errors.isEmpty()) {
                throw new RelationalErrorsException(errors);
            }
        }

        protected void updateRelationsOtherEntity(
                Entity persistedEntity, Entity entity, Set<String> fields, RelationalErrors errors
        ) {
            persistedEntity.setOtherEntity(retrieve(entity.getOtherEntity(), otherEntityService, errors));
        }

        protected void updateRelationsOtherEntityList(
                Entity persistedEntity, Entity entity, Set<String> fields, RelationalErrors errors
        ) {
            persistedEntity.getOtherEntityList().clear();
            persistedEntity.getOtherEntityList()
                    .addAll(retrieve(entity.getOtherEntityList(), otherEntityService, errors));
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Entity extends ApigenAbstractPersistable<Long> {
        Long id;
        OtherEntity otherEntity;
        Set<OtherEntity> otherEntityList;

        public boolean isReference() {
            return getId() != null;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class OtherEntity extends ApigenAbstractPersistable<Long> {
        Long id;
        String name;

        public boolean isReference() {
            return getId() != null;
        }
    }

    private static class OtherEntityService
            extends AbstractCrudService<OtherEntity, Long, ApigenRepository<OtherEntity, Long>> {
        public OtherEntityService(
                ApigenRepository<OtherEntity, Long> repository, AbstractRelationsManager<OtherEntity> relationsManager,
                ApigenMapper<OtherEntity> mapper
        ) {
            super(repository, relationsManager, mapper);
        }

        @Override
        protected void updateBasicDataPartially(OtherEntity persistedEntity, OtherEntity entity, Set<String> fields) {

        }
    }
}
