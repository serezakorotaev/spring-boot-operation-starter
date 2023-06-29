package inova.korotaev.maven.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpecificationUtils {

    @NonNull
    public static <T> Specification<T> findByColumnEquals(Object value, @NonNull String columnName) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(columnName), value);
    }

    @NonNull
    public static <T> Specification<T> findByColumnNotEquals(Object value, @NonNull String columnName) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get(columnName), value);
    }

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

    @NonNull
    public static <T, Y extends Comparable<Y>> Specification<T> findByColumnBetween(Y valueFrom, Y valueTo, @NonNull String columnName) {
        if ((valueFrom == null) && (valueTo == null)) {
            return Specification.where(null);
        }
        if (valueTo == null) {
            return (root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(columnName), valueFrom);
        }
        if (valueFrom == null) {
            return (root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get(columnName), valueTo);
        }
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get(columnName), valueFrom, valueTo);
    }

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

    @NonNull
    public static <T> Specification<T> findByColumnIsNull(@NonNull String columnName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get(columnName));
    }

    @NonNull
    public static <T> Specification<T> findNothing() {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.conjunction().not();
    }

    @NonNull
    public static <T> Specification<T> findAll() {
        return (root, query, criteriaBuilder) ->
                query.where(criteriaBuilder.conjunction()).getRestriction();
    }
}
