package net.cloudappi.apigen.archetypecore.core.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.archetypecore.core.persistence.filter.Filter;
import net.cloudappi.apigen.archetypecore.exceptions.InvalidPropertyPath;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Slf4j
public class ResourceNamingTranslatorByReflection implements ResourceNamingTranslator {

	private Map<String, Map<String, AttributeInfo>> attributesByResource = new HashMap<>();

	public ResourceNamingTranslatorByReflection(ApplicationContext context) {
		this(getApplicationPackage(context));
	}

	public ResourceNamingTranslatorByReflection(String... packageNames) {
		Set<Class<?>> resources = detectResources(packageNames);
		resources.forEach(resource -> analyzeResource(resource, resources));
	}

	private static String getApplicationPackage(ApplicationContext context) {
		return context.getBean(context.getBeanNamesForAnnotation(SpringBootApplication.class)[0])
				.getClass().getPackage().getName();
	}

	private Set<Class<?>> detectResources(String... packageNames) {
		return new Reflections(packageNames, new TypeAnnotationsScanner(), new SubTypesScanner())
				.getTypesAnnotatedWith(ApigenEntityOutResource.class);
	}

	private void analyzeResource(Class<?> resource, Set<Class<?>> resources) {
		String name = resource.getCanonicalName();
		for (Field field : resource.getDeclaredFields()) {
			analyzeField(name, field, resources);
		}
	}

	private void analyzeField(String resourceName, Field field, Set<Class<?>> resources) {
		if (field.isSynthetic()) return;
		String fieldName = field.getName();
		String jsonName = field.getDeclaredAnnotation(JsonProperty.class).value();
		Class<?> fieldType = field.getType();

		boolean isSingle = true;
		if (Collection.class.isAssignableFrom(fieldType)) {
			isSingle = false;
			ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
			fieldType = (Class<?>) stringListType.getActualTypeArguments()[0];
		}

		String fieldResourceType = null;
		if (resources.contains(fieldType)) {
			fieldResourceType = fieldType.getCanonicalName();
		}

		attributesByResource.putIfAbsent(resourceName, new HashMap<>());
		attributesByResource.get(resourceName).put(jsonName, new AttributeInfo(fieldName, fieldResourceType, isSingle));
	}

	@Override
	public void translate(List<String> select, List<String> exclude, List<String> expand, Class resourceClass) {
		translate(select, exclude, expand, null, resourceClass);
	}

	@Override
	public void translate(List<String> select, List<String> exclude, List<String> expand, List<String> orderBy, Class resourceClass) {
		translate(select, exclude, expand, null, orderBy, resourceClass);
	}

	@Override
	public void translate(List<String> select, List<String> exclude, List<String> expand, Filter filter, List<String> orderBy, Class resourceClass) {
		String resourceName = resourceClass.getCanonicalName();
		InvalidPropertyPath errors = new InvalidPropertyPath();
		translateSelect(select, resourceName, errors);
		translateExclude(exclude, resourceName, errors);
		translateExpand(expand, resourceName, errors);
		translateOrderBy(orderBy, resourceName, errors);
		translate(filter, resourceClass, errors);
		if (errors.errorsDetected()) throw errors;
	}

	private void translateSelect(List<String> select, String resourceName, InvalidPropertyPath errors) {
		translate(select, field -> translateField(field, resourceName, errors.getInvalidSelectPath()));
	}

	private void translateExclude(List<String> exclude, String resourceName, InvalidPropertyPath errors) {
		translate(exclude, field -> translateField(field, resourceName, errors.getInvalidExcludePath()));
	}

	private String translateField(String field, String resourceName, List<String> errors) {
		return translate(field, resourceName, false, false, false, errors, null);
	}

	private void translateExpand(List<String> expand, String resourceName, InvalidPropertyPath errors) {
		translate(expand, field -> translateExpand(field, resourceName, errors.getInvalidExpandPath()));
	}

	private String translateExpand(String field, String resourceName, List<String> errors) {
		return translate(field, resourceName, true, false, false, errors, null);
	}

	private void translateOrderBy(List<String> orderBy, String resourceName, InvalidPropertyPath errors) {
		translate(orderBy, field -> translateOrderBy(field, resourceName, errors.getInvalidOrderByPath(), errors.getInvalidOrderByToManyPath()));
	}

	private String translateOrderBy(String field, String resourceName, List<String> errors, List<String> orderByMany) {
		return translate(field, resourceName, false, true, true, errors, orderByMany);
	}

	private void translate(List<String> fields, UnaryOperator<String> translate) {
		if (fields == null) return;
		List<String> newFields = fields.stream()
				.map(translate)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		fields.clear();
		fields.addAll(newFields);
	}

	private String translate(String field, String resourceName, boolean lastIsResource, boolean pathAllSingle, boolean handleOrder, List<String> pathErrors, List<String> orderByManyErrors) {
		String[] fieldParts = field.split("\\.");
		String order = null;
		if (handleOrder) order = cleanOrder(fieldParts);
		String[] newFieldParts = new String[fieldParts.length];
		for (int i = 0; i < fieldParts.length; i++) {
			Map<String, AttributeInfo> attributes = attributesByResource.get(resourceName);
			if (attributes == null) {
				pathErrors.add(field);
				return null;
			}
			AttributeInfo attributeInfo = attributes.get(fieldParts[i]);
			if (attributeInfo == null) {
				pathErrors.add(field);
				return null;
			}
			if (pathAllSingle && !attributeInfo.isSingle()) {
				orderByManyErrors.add(field);
				return null;
			}
			String newFieldPart = attributeInfo.getJavaName();
			resourceName = attributeInfo.getResourceType();
			newFieldParts[i] = newFieldPart;
		}
		if ((lastIsResource && resourceName == null) || (!lastIsResource && resourceName != null)) {
			pathErrors.add(field);
			return null;
		}
		if (handleOrder) newFieldParts[0] = order + newFieldParts[0];
		return String.join(".", newFieldParts);
	}

	private String cleanOrder(String[] pathParts) {
		if (pathParts[0].charAt(0) == '+' || pathParts[0].charAt(0) == ' ') {
			pathParts[0] = pathParts[0].substring(1);
			return "+";
		} else if (pathParts[0].charAt(0) == '-') {
			pathParts[0] = pathParts[0].substring(1);
			return "-";
		}
		return "+";
	}

	@Override
	public void translate(Filter filter, Class resourceClass) {
		InvalidPropertyPath errors = new InvalidPropertyPath();
		translate(filter, resourceClass, errors.getInvalidFilterPath());
		if (errors.errorsDetected()) throw errors;
	}

	public void translate(Filter filter, Class resourceClass, InvalidPropertyPath errors) {
		translate(filter, resourceClass, errors.getInvalidFilterPath());
	}

	private void translate(Filter filter, Class resourceClass, List<String> errors) {
		if (filter == null) return;
		String resourceName = resourceClass.getCanonicalName();
		if (filter.getValues() != null) {
			filter.getValues().forEach(v -> {
				if (v.getProperty() != null) v.setProperty(translateField(v.getProperty(), resourceName, errors));
				translate(v.getFilter(), resourceClass, errors);
			});
		}
	}

	@Getter
	@AllArgsConstructor
	private static class AttributeInfo {
		private String javaName;
		private String resourceType;
		private boolean single;
	}
}
