package net.cloudappi.apigen.archetypecore.core.resource;

import net.cloudappi.apigen.archetypecore.core.persistence.filter.Filter;
import net.cloudappi.apigen.archetypecore.core.persistence.filter.FilterOperation;
import net.cloudappi.apigen.archetypecore.core.persistence.filter.PropType;
import net.cloudappi.apigen.archetypecore.core.persistence.filter.Value;
import net.cloudappi.apigen.archetypecore.core.resource.stubs.FakeResourceColor;
import net.cloudappi.apigen.archetypecore.exceptions.InvalidPropertyPath;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceNamingTranslatorByReflectionTests {

    private static final List<String> SELECT = new ArrayList<>();
    private static final List<String> EXCLUDE = new ArrayList<>();
    private static final List<String> EXPAND = new ArrayList<>();
    private static final Filter FILTER = new Filter();
    private static final List<String> ORDER_BY = new ArrayList<>();

    @Test
    void givenSelectWithoutNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> select = new ArrayList<>(Arrays.asList("json_id", "json_name"));
        resourceNamingTranslatorByReflection.translate(select, EXCLUDE, EXPAND, FakeResourceColor.class);
        assertEquals("id", select.get(0));
        assertEquals("name", select.get(1));
    }

    @Test
    void givenSelectWithNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> select = new ArrayList<>(Arrays.asList("json_id", "json_name", "json_form.property_id"));
        resourceNamingTranslatorByReflection.translate(select, EXCLUDE, EXPAND, FakeResourceColor.class);
        assertEquals("id", select.get(0));
        assertEquals("name", select.get(1));
        assertEquals("form.id", select.get(2));
    }

    @Test
    void givenSelectWithSetNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> select = new ArrayList<>(Arrays.asList("json_id", "json_name", "json_forms.property_id"));
        resourceNamingTranslatorByReflection.translate(select, EXCLUDE, EXPAND, FakeResourceColor.class);
        assertEquals("id", select.get(0));
        assertEquals("name", select.get(1));
        assertEquals("forms.id", select.get(2));
    }

    @Test
    void givenIncorrectSelect_whenTranslate_thenHaveError() {
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> select = new ArrayList<>(Arrays.asList("property_id"));
        InvalidPropertyPath thrown = assertThrows(
                InvalidPropertyPath.class,
                () -> resourceNamingTranslatorByReflection.translate(select, EXCLUDE, EXPAND, FakeResourceColor.class)
        );

        assertEquals("property_id", thrown.getInvalidSelectPath().get(0));
    }

    @Test
    void givenIncorrectResourceNameInSelect_whenTranslate_thenHaveError() {
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> select = new ArrayList<>(Arrays.asList("json_id"));
        InvalidPropertyPath thrown = assertThrows(
                InvalidPropertyPath.class,
                () -> resourceNamingTranslatorByReflection.translate(select, EXCLUDE, EXPAND, String.class)
        );

        assertEquals("json_id", thrown.getInvalidSelectPath().get(0));
    }

    @Test
    void givenExcludeWithoutNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> exclude = new ArrayList<>(Arrays.asList("json_id", "json_name"));
        resourceNamingTranslatorByReflection.translate(SELECT, exclude, EXPAND, FakeResourceColor.class);
        assertEquals("id", exclude.get(0));
        assertEquals("name", exclude.get(1));
    }

    @Test
    void givenExcludeWithNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> exclude = new ArrayList<>(Arrays.asList("json_id", "json_name", "json_form.property_id"));
        resourceNamingTranslatorByReflection.translate(SELECT, exclude, EXPAND, FakeResourceColor.class);
        assertEquals("id", exclude.get(0));
        assertEquals("name", exclude.get(1));
        assertEquals("form.id", exclude.get(2));
    }

    @Test
    void givenExcludeWithSetNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> exclude = new ArrayList<>(Arrays.asList("json_id", "json_name", "json_forms.property_id"));
        resourceNamingTranslatorByReflection.translate(SELECT, exclude, EXPAND, FakeResourceColor.class);
        assertEquals("id", exclude.get(0));
        assertEquals("name", exclude.get(1));
        assertEquals("forms.id", exclude.get(2));
    }

    @Test
    void givenExpandWithSetNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> expand = new ArrayList<>(Arrays.asList("json_forms"));
        resourceNamingTranslatorByReflection.translate(SELECT, EXCLUDE, expand, FakeResourceColor.class);
        assertEquals("forms", expand.get(0));
    }

    @Test
    void givenExpandWithNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> expand = new ArrayList<>(Arrays.asList("json_form"));
        resourceNamingTranslatorByReflection.translate(SELECT, EXCLUDE, expand, FakeResourceColor.class);
        assertEquals("form", expand.get(0));
    }

    @Test
    void givenOrderByPositiveWithoutNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> orderBy = new ArrayList<>(Arrays.asList("+json_id"));
        resourceNamingTranslatorByReflection.translate(SELECT, EXCLUDE, EXPAND, orderBy, FakeResourceColor.class);
        assertEquals("+id", orderBy.get(0));
    }

    @Test
    void givenOrderByNegativeWithoutNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> orderBy = new ArrayList<>(Arrays.asList("-json_id"));
        resourceNamingTranslatorByReflection.translate(SELECT, EXCLUDE, EXPAND, orderBy, FakeResourceColor.class);
        assertEquals("-id", orderBy.get(0));
    }

    @Test
    void givenOrderByPositiveWithNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> orderBy = new ArrayList<>(Arrays.asList("+json_id", "+json_form.property_id"));
        resourceNamingTranslatorByReflection.translate(SELECT, EXCLUDE, EXPAND, orderBy, FakeResourceColor.class);
        assertEquals("+id", orderBy.get(0));
        assertEquals("+form.id", orderBy.get(1));
    }

    @Test
    void givenOrderByNegativeeWithNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> orderBy = new ArrayList<>(Arrays.asList("-json_id", "-json_form.property_id"));
        resourceNamingTranslatorByReflection.translate(SELECT, EXCLUDE, EXPAND, orderBy, FakeResourceColor.class);
        assertEquals("-id", orderBy.get(0));
        assertEquals("-form.id", orderBy.get(1));
    }

    @Test
    void givenOrderByPositiveWithSetNestedId_whenTranslate_thenHaveError() {
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> orderBy = new ArrayList<>(Arrays.asList("+json_id", "+json_forms.property_id"));
        InvalidPropertyPath thrown = assertThrows(
                InvalidPropertyPath.class,
                () -> resourceNamingTranslatorByReflection.translate(SELECT, EXCLUDE, EXPAND, orderBy, FakeResourceColor.class)
        );

        assertEquals("+json_forms.property_id", thrown.getInvalidOrderByToManyPath().get(0));
    }

    @Test
    void givenOrderByNegativeWithSetNestedId_whenTranslate_thenHaveError() {
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> orderBy = new ArrayList<>(Arrays.asList("-json_id", "-json_forms.property_id"));
        InvalidPropertyPath thrown = assertThrows(
                InvalidPropertyPath.class,
                () -> resourceNamingTranslatorByReflection.translate(SELECT, EXCLUDE, EXPAND, orderBy, FakeResourceColor.class)
        );

        assertEquals("-json_forms.property_id", thrown.getInvalidOrderByToManyPath().get(0));
    }

    @Test
    void givenIncorrectOrderBy_whenTranslate_thenHaveError() {
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        List<String> orderBy = new ArrayList<>(Arrays.asList("json_form"));
        InvalidPropertyPath thrown = assertThrows(
                InvalidPropertyPath.class,
                () -> resourceNamingTranslatorByReflection.translate(SELECT, EXCLUDE, EXPAND, orderBy, FakeResourceColor.class)
        );

        assertEquals("[]", thrown.getInvalidExpandPath().toString());
    }

    @Test
    void givenFilterWithoutNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        Filter filter = new Filter();
        filter.setOperation(FilterOperation.LT);
        Value value = new Value();
        value.setType(PropType.INTEGER);
        value.setProperty("json_id");
        value.setValue("10");
        filter.setValues(Collections.singletonList(value));
        resourceNamingTranslatorByReflection.translate(filter, FakeResourceColor.class);
        assertEquals("id", filter.getValues().get(0).getProperty());
    }

    @Test
    void givenFilterWithNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        Filter filter = new Filter();
        filter.setOperation(FilterOperation.LT);
        Value value = new Value();
        value.setType(PropType.INTEGER);
        value.setProperty("json_form.property_id");
        value.setValue("10");
        filter.setValues(Collections.singletonList(value));
        resourceNamingTranslatorByReflection.translate(filter, FakeResourceColor.class);
        assertEquals("form.id", filter.getValues().get(0).getProperty());
    }

    @Test
    void givenFilterWithSetNestedId_whenTranslate_thenSuccess(){
        String packageName = FakeResourceColor.class.getPackage().getName();
        ResourceNamingTranslatorByReflection resourceNamingTranslatorByReflection = new ResourceNamingTranslatorByReflection(packageName);
        Filter filter = new Filter();
        filter.setOperation(FilterOperation.LT);
        Value value = new Value();
        value.setType(PropType.INTEGER);
        value.setProperty("json_forms.property_id");
        value.setValue("10");
        filter.setValues(Collections.singletonList(value));
        resourceNamingTranslatorByReflection.translate(filter, FakeResourceColor.class);
        assertEquals("forms.id", filter.getValues().get(0).getProperty());
    }
}
