package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence;

import com.squareup.javapoet.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Deprecated
public class EntitiesData {

    private static final Set<TypeName> TO_ONE_RELATION = new HashSet<>(Arrays.asList(TypeName.get(OneToOne.class), TypeName.get(ManyToOne.class)));
    private static final Set<TypeName> TO_MANY_RELATION = new HashSet<>(Arrays.asList(TypeName.get(OneToMany.class), TypeName.get(ManyToMany.class)));
    private static final TypeName ID = TypeName.get(Id.class);
    private static final TypeName ID_E = TypeName.get(EmbeddedId.class);
    private Map<String, Map<String, AttributeData>> entitiesAttributes = new HashMap<>();

    public EntitiesData(Collection<TypeSpec> spec) {
        for (TypeSpec s : spec) {
            String entityName = s.name;
            Map<String, AttributeData> attributes = new HashMap<>();
            for (FieldSpec f : s.fieldSpecs) {
                String attributeName = f.name;
                attributes.put(attributeName, getRelationalAnnotation(f));
            }
            entitiesAttributes.put(entityName, attributes);
        }
    }

    private AttributeData getRelationalAnnotation(FieldSpec fieldSpec) {
        boolean isCollection = false;
        boolean isOwner = false;
        String relatedEntity = null;
        boolean isId = false;
        for (AnnotationSpec annotationSpec : fieldSpec.annotations) {
            TypeName annotationType = annotationSpec.type;
            if (TO_ONE_RELATION.contains(annotationType)) {
                isCollection = false;
                isOwner = !annotationSpec.members.containsKey("mappedBy");
                relatedEntity = getClassName(fieldSpec.type);
            } else if (TO_MANY_RELATION.contains(annotationType)) {
                isCollection = true;
                isOwner = !annotationSpec.members.containsKey("mappedBy");
                ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) fieldSpec.type;
                relatedEntity = getClassName(parameterizedTypeName.typeArguments.get(0));
            } else if (ID.equals(annotationType) || ID_E.equals(annotationType)) {
                isId = true;
            }
        }
        return new AttributeData(isCollection, relatedEntity, isOwner, isId, fieldSpec.type);
    }

    private String getClassName(TypeName typeName) {
        return ((ClassName) typeName).simpleName();
    }

    public Set<String> getRelatedEntities(String entityName) {
        return entitiesAttributes.get(entityName).values().stream()
                .map(attributeData -> attributeData.relatedEntity)
                .filter(Objects::nonNull)
                .filter(name -> !name.equals(entityName))
                .collect(Collectors.toSet());
    }

    public Set<String> getBasicAttributes(String entityName) {
        return entitiesAttributes.get(entityName).entrySet().stream()
                .filter(e -> isNull(e.getValue().relatedEntity))
                .filter(e -> !e.getValue().isId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public TypeName getIDType(String entityName) {
        return entitiesAttributes.get(entityName).values().stream()
                .filter(AttributeData::isId).map(AttributeData::getTypeName).findFirst().orElse(null);
    }

    public TypeName getComposedIDType(String entityName) {
        TypeName typeName = getIDType(entityName);
        if (typeName.toString().startsWith("java")) return null;
        return typeName;
    }

    public Map<String, AttributeData> getAttributes(String entityName) {
        return entitiesAttributes.get(entityName);
    }

    @Getter
    @AllArgsConstructor
    public static class AttributeData {
        private boolean isCollection;
        private String relatedEntity;
        private boolean isOwned;
        private boolean isId;
        private TypeName typeName;
    }
}
