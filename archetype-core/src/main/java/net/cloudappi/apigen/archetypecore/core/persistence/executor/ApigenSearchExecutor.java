package net.cloudappi.apigen.archetypecore.core.persistence.executor;

import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.archetypecore.core.persistence.ApigenSearchResult;
import net.cloudappi.apigen.archetypecore.core.persistence.filter.Filter;
import net.cloudappi.apigen.archetypecore.core.persistence.filter.FilterOperation;
import net.cloudappi.apigen.archetypecore.core.persistence.filter.Value;
import net.cloudappi.apigen.archetypecore.core.persistence.pagination.Pagination;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static net.cloudappi.apigen.archetypecore.core.persistence.filter.FilterOperation.AND;
import static net.cloudappi.apigen.archetypecore.core.persistence.filter.FilterOperation.OR;
import static net.cloudappi.apigen.archetypecore.core.persistence.functions.ApigenFunctionsMetadataBuilderInitializer.REGEXP_FUNCTION;

@Slf4j
public class ApigenSearchExecutor {

	private EntityManager em;
	private EntityInfo entityData;

	public ApigenSearchExecutor(EntityManager em, EntityInfo entityData) {
		this.em = em;
		this.entityData = entityData;
	}

	public <E, K extends Serializable> Optional<E> searchById(K id, List<String> select, List<String> exclude, List<String> orderBy, List<String> expand, Class<E> clazz) {

		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Tuple> query = builder.createTupleQuery();

		Root<E> root = query.from(clazz);

		Map<String, Join> joins = join(expand, root);
		List<String> enhancedFields = getFields(select, exclude, root, joins);
		List<Selection<?>> selections = select(enhancedFields, root, joins);
		query.multiselect(selections);
		List<Order> order = order(orderBy, builder, root, joins);
		query.orderBy(order);

		String idAttribute = entityData.getIdAttribute(clazz);
		Expression expression = getPath(idAttribute, root, joins);
		Predicate predicate = builder.equal(expression, id);

		if (predicate != null) query.where(predicate);
		TypedQuery<Tuple> createQuery = em.createQuery(query);
		List<Tuple> result = createQuery.getResultList();
		List<E> found = new TupleMapper(enhancedFields, entityData, clazz).map(result);

		if (found.isEmpty()) return Optional.empty();
		return Optional.of(found.get(0));
	}

	public <E> ApigenSearchResult<E> search(List<String> select, List<String> exclude, List<String> orderBy, List<String> expand, Filter filter, Pagination pagination, boolean total, Class<E> clazz) {

		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Tuple> query = builder.createTupleQuery();

		Root<E> root = query.from(clazz);

		Map<String, Join> joins = join(expand, root);
		List<String> enhancedFields = getFields(select, exclude, root, joins);
		List<Selection<?>> selections = select(enhancedFields, root, joins);
		query.multiselect(selections);
		Long count = null;
		if (pagination != null) {
			addPaginationPredicate(query, root, orderBy, expand, filter, pagination, clazz, builder);
		} else {
			addPredicate(query, filter, root, joins, builder);
		}
		if (total) {
			count = count(clazz, expand, filter, builder);
		}
		addOrder(query, orderBy, root, joins, builder);
		List<Tuple> result = em.createQuery(query).getResultList();
		return new ApigenSearchResult<>(new TupleMapper(enhancedFields, entityData, clazz).map(result), count);
	}

	private void addPaginationPredicate(CriteriaQuery<Tuple> query, Root<?> root, List<String> orderBy, List<String> expand, Filter filter, Pagination pagination, Class<?> clazz, CriteriaBuilder builder) {
		List<Object> ids = getMatchingIds(orderBy, expand, filter, pagination, clazz, builder);
		String idAttribute = entityData.getIdAttribute(clazz);
		Path<?> path = root.get(idAttribute);
		Predicate predicate = path.in(ids);
		query.where(predicate);
	}

	private List<Object> getMatchingIds(List<String> orderBy, List<String> expand, Filter filter, Pagination pagination, Class<?> clazz, CriteriaBuilder builder) {
		CriteriaQuery<Tuple> query = builder.createTupleQuery();
		Root<?> root = query.from(clazz);
		Map<String, Join> joins = join(expand, root);
		String idAttribute = entityData.getIdAttribute(clazz);
		List<String> allFields = new LinkedList<>(orderBy);
		if (!allFields.contains(idAttribute)) allFields.add(idAttribute);
		List<Selection<?>> orderSelect = orderFields(allFields, builder, root, joins);
		query.multiselect(orderSelect);
		query.distinct(true);
		addPredicate(query, filter, root, joins, builder);
		addOrder(query, orderBy, root, joins, builder);
		TypedQuery<Tuple> createQuery = em.createQuery(query);
		addPaging(createQuery, pagination);
		List<Tuple> resultId = createQuery.getResultList();
		ArrayList<Object> idValues = new ArrayList<>();
		int i = allFields.indexOf(idAttribute);
		for (Tuple tuple : resultId) {
			idValues.add(tuple.get(i));
		}
		return idValues;
	}

	private Long count(Class<?> clazz, List<String> expand, Filter filter, CriteriaBuilder builder) {
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<?> root = query.from(clazz);
		Map<String, Join> joins = join(expand, root);
		query.select(builder.count(root));
		query.distinct(true);
		addPredicate(query, filter, root, joins, builder);
		return em.createQuery(query).getSingleResult();
	}

	private Map<String, Join> join(List<String> expand, Root<?> root) {
		Map<String, Join> joins = new HashMap<>();
		if (expand == null || expand.isEmpty()) return joins;
		for (String e : expand) {
			String[] path = e.split("\\.");
			String fullPath = path[0];
			Join join = joins.computeIfAbsent(fullPath, k -> root.join(path[0], JoinType.LEFT));
			for (int i = 1; i < path.length; i++) {
				Join parentJoin = join;
				fullPath = fullPath.concat(".").concat(path[i]);
				int finalI = i;
				join = joins.computeIfAbsent(fullPath, k -> parentJoin.join(path[finalI], JoinType.LEFT));
			}
		}
		return joins;
	}

	private List<String> getFields(List<String> select, List<String> exclude, Root<?> root, Map<String, Join> joins) {
		if (select == null || select.isEmpty()) {
			return getFieldsFromExclusion(root.getJavaType(), exclude, joins);
		} else {
			return getFieldsFromInclusion(select, joins, root.getJavaType());
		}
	}

	private List<String> getFieldsFromExclusion(Class clazz, List<String> exclude, Map<String, Join> joins) {
		List<String> fields = entityData.getBasicAttributes(clazz);
		if (joins != null) fields.addAll(getFieldsFromExpands(clazz, joins));
		if (exclude != null) fields.removeAll(exclude);
		return fields;
	}

	private List<String> getFieldsFromExpands(Class clazz, Map<String, Join> joins) {
		return joins.keySet().stream().flatMap(field -> entityData.getBasicAttributes(clazz, field).stream()).collect(Collectors.toList());
	}

	private List<String> getFieldsFromInclusion(List<String> fields, Map<String, Join> joins, Class clazz) {
		List<String> enhancedFields = new ArrayList<>(fields);
		String idAttribute = entityData.getIdAttribute(clazz);
		if (!enhancedFields.contains(idAttribute)) enhancedFields.add(idAttribute);
		joins.keySet().stream().map(f -> f.concat("." + entityData.getIdAttribute(clazz, f))).filter(f -> !enhancedFields.contains(f)).forEach(enhancedFields::add);
		return enhancedFields;
	}

	private List<Selection<?>> select(List<String> fields, Root<?> root, Map<String, Join> joins) {
		return fields.stream().map(f -> getPath(f, root, joins)).collect(Collectors.toList());
	}

	private List<Selection<?>> orderFields(List<String> orderBy, CriteriaBuilder builder, Root<?> root, Map<String, Join> joins) {
		if (orderBy == null || orderBy.isEmpty()) return Collections.emptyList();
		return orderBy.stream().map(o -> {
			boolean asc = true;
			if (o.charAt(0) == '+') {
				o = o.substring(1);
			} else if (o.charAt(0) == '-') {
				asc = false;
				o = o.substring(1);
			}
			return getPath(o, root, joins);
		}).collect(Collectors.toList());
	}

	private List<Order> order(List<String> orderBy, CriteriaBuilder builder, Root<?> root, Map<String, Join> joins) {
		if (orderBy == null || orderBy.isEmpty()) return Collections.emptyList();
		return orderBy.stream().map(o -> {
			boolean asc = true;
			if (o.charAt(0) == '+') {
				o = o.substring(1);
			} else if (o.charAt(0) == '-') {
				asc = false;
				o = o.substring(1);
			}
			Path<?> s = getPath(o, root, joins);
			return asc ? builder.asc(s) : builder.desc(s);
		}).collect(Collectors.toList());
	}

	private Path<?> getPath(String field, Root<?> root, Map<String, Join> joins) {
		int i = field.lastIndexOf('.');
		if (i > -1) {
			String base = field.substring(0, i);
			String simpleField = field.substring(i + 1);
			return joins.get(base).get(simpleField);
		} else {
			return root.get(field);
		}
	}

	private void addOrder(CriteriaQuery<?> query, List<String> orderBy, Root<?> root, Map<String, Join> joins, CriteriaBuilder builder) {
		List<Order> order = order(orderBy, builder, root, joins);
		query.orderBy(order);
	}

	private void addPredicate(CriteriaQuery<?> query, Filter filter, Root<?> root, Map<String, Join> joins, CriteriaBuilder builder) {
		Predicate predicate = filter(filter, builder, root, joins);
		if (predicate != null) query.where(predicate);
	}

	private void addPaging(TypedQuery<Tuple> createQuery, Pagination pagination) {
		int firstResult = getFirstResult(pagination);
		if (firstResult > 0) {
			createQuery.setFirstResult(firstResult);
		}
		if (pagination.getLimit() > 0) {
			createQuery.setMaxResults(pagination.getLimit());
		}
	}


	private static int getFirstResult(Pagination search) {
		int firstResult = search.getInit();
		return Math.max(firstResult, 0);
	}

	@SuppressWarnings("unchecked")
	private Predicate filter(Filter filter, CriteriaBuilder builder, Root<?> root, Map<String, Join> joins) {

		if (filter == null) return null;

		FilterOperation operation = filter.getOperation();

		if (isLogicalFunction(operation)) return getPredicateOfConditions(filter, builder, root, joins);

		Value value = filter.getValues().get(0);
		Expression<Comparable> expression = (Expression<Comparable>) getPath(value.getProperty(), root, joins);

		if (operation.isSingleValue()) {
			return getSingleValueCondition(operation, value, expression, builder);
		} else {
			return getListValueCondition(operation, value, expression, builder);
		}
	}

	private boolean isLogicalFunction(FilterOperation operation) {
		return operation == AND || operation == OR;
	}

	private Predicate getPredicateOfConditions(Filter filter, CriteriaBuilder builder, Root<?> root, Map<String, Join> joins) {
		List<Predicate> filters = new ArrayList<>();
		for (Value v : filter.getValues()) {
			filters.add(filter(v.getFilter(), builder, root, joins));
		}
		Predicate[] predicates = filters.toArray(new Predicate[]{});
		if (filter.getOperation() == AND) return builder.and(predicates);
		else return builder.or(predicates);
	}

	@SuppressWarnings("unchecked")
	private Predicate getSingleValueCondition(FilterOperation operation, Value rootValue, Expression<Comparable> expression, CriteriaBuilder builder) {
		if (operation != FilterOperation.EQ && operation != FilterOperation.NEQ && isNull(rootValue.getValue())) throw new IllegalArgumentException(operation + " requires a single value");
		String valueStr = rootValue.getValue();
		Comparable value = rootValue.getType().convert(rootValue.getValue());

		Class attributeType = expression.getJavaType();
		if (entityData.isEmbeddedId(attributeType)) value = getComposedId(attributeType, rootValue.getValue());

		if (attributeType.isEnum()) {
			value = getEnum(attributeType, rootValue.getValue());
		}
		
		if (attributeType.equals(Instant.class) && value instanceof OffsetDateTime) {
			value = ((OffsetDateTime) value).toInstant();
		}

		switch (operation) {
			case GT:
				return builder.greaterThan(expression, value);
			case LT:
				return builder.lessThan(expression, value);
			case GTEQ:
				return builder.greaterThanOrEqualTo(expression, value);
			case LTEQ:
				return builder.lessThanOrEqualTo(expression, value);
			case EQ:
				return value == null ? builder.isNull(expression) : builder.equal(expression, value);
			case NEQ:
				return value == null ? builder.isNotNull(expression) : builder.notEqual(expression, value);
			case SUBSTRING:
				return builder.like((Expression) expression, "%" + valueStr + "%");
			case LIKE:
				return builder.like((Expression) expression, valueStr);
			case ILIKE:
				return builder.like(builder.lower((Expression) expression), valueStr.toLowerCase());
			case NLIKE:
				return builder.notLike((Expression) expression, valueStr);
			case REGEXP:
				Pattern regexPattern = Pattern.compile(valueStr);
				Expression<String> patternExpression = builder.literal(regexPattern.pattern());
				return builder.isTrue(builder.function(REGEXP_FUNCTION, Boolean.class, expression, patternExpression));
			default:
				throw new IllegalArgumentException("Operation " + operation + " not supported");
		}
	}

	@SuppressWarnings("unchecked")
	private Predicate getListValueCondition(FilterOperation operation, Value rootValue, Expression<Comparable> expression, CriteriaBuilder builder) {
		if (isNull(rootValue.getValues()) || rootValue.getValues().isEmpty())
			throw new IllegalArgumentException(operation + " requires a list of values");

		List<Comparable> values = rootValue.getType().convert(rootValue.getValues());

		Class attributeType = expression.getJavaType();
		if (entityData.isEmbeddedId(attributeType)) values = rootValue.getValues().stream().map(v -> getComposedId(attributeType, v)).collect(Collectors.toList());

		if (attributeType.isEnum()) {
			values = rootValue.getValues().stream().map(v -> getEnum(attributeType, v)).collect(Collectors.toList());
		}

		switch (operation) {
			case IN:
				CriteriaBuilder.In inCb = builder.in(expression);
				values.forEach(inCb::value);
				return inCb;
			case BETWEEN:
				if (values.size() != 2) throw new IllegalArgumentException(operation + " requires 2 values");
				return builder.between(expression, values.get(0), values.get(1));
			default:
				throw new IllegalArgumentException("Operation " + operation + " not supported");
		}
	}

	private Comparable getComposedId(Class clazz, String value) {
		try {
			Method m = clazz.getMethod("from", String.class);
			Comparable composedId = (Comparable) m.invoke(null, (Object) value);
			return composedId;
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			log.error("Error: ", e);
			return null;
		}
	}

	private Comparable getEnum(Class clazz, String value) {
		return Enum.valueOf(clazz, value);
	}
}
