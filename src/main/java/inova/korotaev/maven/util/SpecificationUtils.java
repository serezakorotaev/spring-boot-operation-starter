package inova.korotaev.maven.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

/**
 * @author Sergey Korotaev
 * Util is used for building different specifications for request
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpecificationUtils {

    /**
     * Find column equals specified
     *
     * @param value      - the value to which the entry in the database should be equivalent
     * @param columnName - name of column into the database
     * @param <T>        - the entity for which the request is being built
     * @return Specification
     */
    @NonNull
    public static <T> Specification<T> findByColumnEquals(Object value, @NonNull String columnName) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(columnName), value);
    }

    /**
     * Find column not equals specified
     *
     * @param value      - the value to which the entry in the database should not be equivalent
     * @param columnName - name of column into the database
     * @param <T>        - the entity for which the request is being built
     * @return Specification
     */
    @NonNull
    public static <T> Specification<T> findByColumnNotEquals(Object value, @NonNull String columnName) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get(columnName), value);
    }

    /**
     * Find column like specified
     *
     * @param value      - the value to which the entry in the database should be similar
     * @param columnName - name of columns into the database
     * @param <T>        - the entity for which the request is being built
     * @return Specification
     */
    @NonNull
    public static <T> Specification<T> findByColumnsLike(@NonNull String value, @NonNull Collection<String> columnName) {
        if (!value.contains("%")) {
            value = "%" + value + "%";
        }
        String textForSearch = value.toLowerCase();
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.or(columnName.stream()
                        .map(element -> criteriaBuilder.like(criteriaBuilder.lower(root.get(element)), textForSearch))
                        .toArray(Predicate[]::new));
    }

    /**
     * Find column less than specified value
     *
     * @param value      - the value to which the entry in the database should be less
     * @param columnName - name of column into the database
     * @param <T>        - the entity for which the request is being built
     * @param <Y>        - comparable value
     * @return Specification
     */
    @NonNull
    public static <T, Y extends Comparable<Y>> Specification<T> lessThan(Y value, @NonNull String columnName) {
        if (value == null) {
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(columnName), value);
    }

    /**
     * Find column greater than specified value
     *
     * @param value      - the value to which the entry in the database should be greater
     * @param columnName - name of column into the database
     * @param <T>        - the entity for which the request is being built
     * @param <Y>        - comparable value
     * @return Specification
     */
    @NonNull
    public static <T, Y extends Comparable<Y>> Specification<T> greaterThan(Y value, @NonNull String columnName) {
        if (value == null) {
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(columnName), value);
    }

    /**
     * Find column less than or equals specified value
     *
     * @param value      - the value to which the entry in the database should be less or equals
     * @param columnName - name of column into the database
     * @param <T>        - the entity for which the request is being built
     * @param <Y>        - comparable value
     * @return Specification
     */
    @NonNull
    public static <T, Y extends Comparable<Y>> Specification<T> lessThanOrEqual(Y value, @NonNull String columnName) {
        if (value == null) {
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(columnName), value);
    }

    /**
     * Find column greater than or equals specified value
     *
     * @param value      - the value to which the entry in the database should be greater or equals
     * @param columnName - name of column into the database
     * @param <T>        - the entity for which the request is being built
     * @param <Y>        - comparable value
     * @return Specification
     */
    @NonNull
    public static <T, Y extends Comparable<Y>> Specification<T> greaterThanOrEqual(Y value, @NonNull String columnName) {
        if (value == null) {
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(columnName), value);
    }

    /**
     * Find entry where value into the column contains in specified collection
     *
     * @param collection - collection values for searching
     * @param columnName - name of column into the database
     * @param <T>        - the entity for which the request is being built
     * @return Specification
     */
    @NonNull
    public static <T> Specification<T> findByCollectionIn(@NonNull Collection<?> collection, @NonNull String columnName) {
        Specification<T> specification;
        if (CollectionUtils.isEmpty(collection)) {
            specification = findNothing();
        } else {
            specification = (root, criteriaQuery, criteriaBuilder) ->
                    root.get(columnName).in(collection);
        }
        return specification;
    }

    /**
     * Find entry where value into the column not contains in specified collection
     *
     * @param collection - collection values for searching
     * @param columnName - name of column into the database
     * @param <T>        - the entity for which the request is being built
     * @return Specification
     */
    @NonNull
    public static <T> Specification<T> findByCollectionNotIn(@NonNull Collection<?> collection, @NonNull String columnName) {
        Specification<T> specification;
        if (CollectionUtils.isEmpty(collection)) {
            specification = findNothing();
        } else {
            specification = (root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.not(root.get(columnName).in(collection));
        }
        return specification;
    }

    /**
     * Find entry where specified column is null
     *
     * @param columnName - name of column into the database
     * @param <T>        - the entity for which the request is being built
     * @return Specification
     */
    @NonNull
    public static <T> Specification<T> findByColumnIsNull(@NonNull String columnName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get(columnName));
    }

    /**
     * Find nothing into the database
     *
     * @param <T> - the entity for which the request is being built
     * @return Specification
     */
    @NonNull
    public static <T> Specification<T> findNothing() {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.conjunction().not();
    }

    /**
     * Find all entry into the database
     *
     * @param <T> - the entity for which the request is being built
     * @return Specification
     */
    @NonNull
    public static <T> Specification<T> findAll() {
        return (root, query, criteriaBuilder) ->
                query.where(criteriaBuilder.conjunction()).getRestriction();
    }

    /**
     * Find entry which value contains all specified elements
     *
     * @param value      - single or list elements with comma separator
     * @param columnName - name of column into the database
     * @param <T>        -       the entity for which the request is being built
     * @return Specification
     */
    @NonNull
    public static <T> Specification<T> contains(@NonNull Object value, @NonNull String columnName) {
        return (root, query, criteriaBuilder) -> query.where(criteriaBuilder.and(
                Arrays.stream(value.toString().split(","))
                        .map(v ->
                                createPredicate.apply(
                                        trimAndGlue.apply(v),
                                        root.get(columnName),
                                        criteriaBuilder)
                        )
                        .toArray(Predicate[]::new)
        )).getRestriction();

    }

    private static final Function<String, String> trimAndGlue = v -> "%" + v.trim() + "%";
    private static final TriFunction<String, Expression<?>, CriteriaBuilder, Predicate> createPredicate =
            (v, expression, cb) -> cb.like(expression.as(String.class), v);
}
