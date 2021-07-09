package org.apiaddicts.apitools.apigen.generatorcore.generator.web.controller.endpoints;

import org.apiaddicts.apitools.apigen.generatorcore.config.controller.Attribute;

import java.util.ArrayList;

public class AttributeObjectMother {

    private AttributeObjectMother() {
        // Intentional blank
    }

    public static Attribute createSimpleStringAttribute(String name) {
        return createSimpleAttribute("string", name, name, null);
    }

    public static Attribute createSimpleAttribute(String type, String name) {
        return createSimpleAttribute(type, name, name, null);
    }

    public static Attribute createSimpleAttribute(String type, String name, String entityFieldName) {
        return createSimpleAttribute(type, name, entityFieldName, null);
    }

    public static Attribute createSimpleAttribute(String type, String name, String entityFieldName, String format) {
        Attribute attribute = new Attribute();
        attribute.setType(type);
        attribute.setName(name);
        attribute.setEntityFieldName(entityFieldName);
        attribute.setFormat(format);
        attribute.setAttributes(new ArrayList<>());
        attribute.setValidations(new ArrayList<>());
        attribute.setCollection(false);
        return attribute;
    }
}
