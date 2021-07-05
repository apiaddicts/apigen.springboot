package org.apiaddicts.apitools.apigen.generatorcore.generator.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import org.apiaddicts.apitools.apigen.generatorcore.config.entity.Entity;
import org.apiaddicts.apitools.apigen.generatorcore.config.entity.EntityObjectMother;

public class ComposedIdBuilderTest {
	
	private static Entity entity;
	private static TypeSpec entityTypeSpec;
	private static String BASE_PACKAGE = "org.test";
	
	@BeforeAll
    static void prepareTest() {
        entity = EntityObjectMother.createSimpleEntityWithComposedID();
        ComposedIdBuilder composedIdbuilder = new ComposedIdBuilder(entity, BASE_PACKAGE);
        entityTypeSpec = composedIdbuilder.build();
    }
	
	@Test
    void givenEntityWithComposedID_whenGenerated_thenModifierIsPublic() {
        assertTrue(entityTypeSpec.hasModifier(Modifier.PUBLIC));
    }

    @Test
    void givenEntityWithComposedID_whenGenerated_thenKindIsClass() {
        assertEquals("CLASS", entityTypeSpec.kind.toString());
    }
	
	@Test
	void givenEntityWithComposedID_whenGenerated_thenAnnotationsAreCorrect() {
		assertEquals(6, entityTypeSpec.annotations.size(), "Number of annotations is wrong");
		assertEquals("@lombok.Getter",entityTypeSpec.annotations.get(0).toString(), "Checking if the Entity contains @Setter annotation:");
		assertEquals("@lombok.Setter",entityTypeSpec.annotations.get(1).toString(), "Checking if the Entity contains @Getter annotation:");
		assertEquals("@javax.persistence.Embeddable",entityTypeSpec.annotations.get(2).toString(), "Checking if the Entity contains @Embeddable annotation:");
		assertEquals("@lombok.NoArgsConstructor",entityTypeSpec.annotations.get(3).toString(), "Checking if the Entity contains @NoArgsConstructor annotation:");
		assertEquals("@lombok.AllArgsConstructor",entityTypeSpec.annotations.get(4).toString(), "Checking if the Entity contains @AllArgsConstructor annotation:");
		assertEquals("@lombok.EqualsAndHashCode",entityTypeSpec.annotations.get(5).toString(), "Checking if the Entity contains @EqualsAndHashCode annotation:");
	}
	
	@Test
    void givenEntityWithComposedID_whenGenerated_thenNameIsCorrect() {
        assertEquals("TestEntityID", entityTypeSpec.name,"Checking if the Entity has the correct name: TestEntityID");
    }
	
	@Test
	void givenEntityWithComposedID_whenGenerated_thenPropertiesAreCorrect() {
        assertEquals("@javax.persistence.Column(\n" + 
        		"    name = \"id_s\"\n" + 
        		")\n" + 
        		"private java.lang.String idS;\n" + 
        		"",entityTypeSpec.fieldSpecs.get(0).toString());
        assertEquals("@javax.persistence.Column(\n" + 
        		"    name = \"id_n\"\n" + 
        		")\n" + 
        		"private java.lang.Long idN;\n" + 
        		"",entityTypeSpec.fieldSpecs.get(1).toString());
	}
	
	@Test
	void givenEntityWithComposedID_whenGenerated_thenMethodsAreCorrect() {
		
		assertEquals("public static org.test.testentity.TestEntityID from(java.lang.String str) {\n" + 
				"  if (str == null) return null;\n" + 
				"  java.lang.String[] parts = str.split(\"_\");\n" + 
				"  if (parts.length != 2) throw new java.lang.IllegalArgumentException();\n" + 
				"  return new org.test.testentity.TestEntityID(parts[0], java.lang.Long.valueOf(parts[1]));\n" + 
				"}\n" + 
				"",entityTypeSpec.methodSpecs.get(0).toString(),"Checking if the Entity contains the method: from");
		assertEquals("@java.lang.Override\n" + 
				"public java.lang.String toString() {\n" + 
				"  return idS + \"_\" + idN;\n" + 
				"}\n" + 
				"",entityTypeSpec.methodSpecs.get(1).toString(),"Checking if the Entity contains the method: toString");
		assertEquals("@java.lang.Override\n" + 
				"public int compareTo(org.test.testentity.TestEntityID o) {\n" + 
				"  int c;\n" + 
				"  c = idS.compareTo(o.idS);\n" + 
				"  if (c != 0) return c;\n" + 
				"  c = idN.compareTo(o.idN);\n" + 
				"  if (c != 0) return c;\n" + 
				"  return c;\n" + 
				"}\n" + 
				"",entityTypeSpec.methodSpecs.get(2).toString(),"Checking if the Entity contains the method: compareTo");
	}
}
