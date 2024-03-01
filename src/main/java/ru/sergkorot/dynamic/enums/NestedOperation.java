package ru.sergkorot.dynamic.enums;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Subquery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import ru.sergkorot.dynamic.util.SpecificationUtils;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Getter
@AllArgsConstructor
public enum NestedOperation {

    /**
     * Element of enum for constructing in operation
     */
    IN("in") {
        @Override
        public <T> Specification<T> buildQuery(String fieldName, Subquery<Object> subquery, CriteriaBuilder criteriaBuilder) {
            return (root, query, criteriaBuilder1) -> criteriaBuilder.in(root.get(fieldName)).value(subquery);
        }
    },

    /**
     * Element of enum for constructing not in operation
     */
    NOT_IN("notIn") {
        @Override
        public <T> Specification<T> buildQuery(String fieldName, Subquery<Object> subquery, CriteriaBuilder criteriaBuilder) {
            return (root, query, criteriaBuilder1) -> criteriaBuilder.not(criteriaBuilder.in(root.get(fieldName)).value(subquery));
        }
    },

    /**
     * Element of enum for constructing equals operation
     */
    EQUAL("eq") {
        @Override
        public <T> Specification<T> buildQuery(String fieldName, Subquery<Object> subquery, CriteriaBuilder criteriaBuilder) {
            return SpecificationUtils.findByColumnEquals(subquery, fieldName);
        }
    },

    /**
     * Element of enum for constructing not equals operation
     */
    NOT_EQUAL("notEq") {
        @Override
        public <T> Specification<T> buildQuery(String fieldName, Subquery<Object> subquery, CriteriaBuilder criteriaBuilder) {
            return SpecificationUtils.findByColumnNotEquals(subquery, fieldName);
        }
    },

    /**
     * Element of enum for constructing is null operation
     */
    IS_NULL("isNull") {
        @Override
        public <T> Specification<T> buildQuery(String fieldName, Subquery<Object> subquery, CriteriaBuilder criteriaBuilder) {
            return SpecificationUtils.findByColumnIsNull(fieldName);
        }
    },

    /**
     * Element of enum for constructing less than operation
     */
    LESS_THAN("lt") {
        @Override
        public <T> Specification<T> buildQuery(String fieldName, Subquery<Object> subquery, CriteriaBuilder criteriaBuilder) {
            return SpecificationUtils.lessThan(subquery.getSelection().as(String.class), fieldName);
        }
    },

    /**
     * Element of enum for constructing greater than operation
     */
    GREATER_THAN("gt") {
        @Override
        public <T> Specification<T> buildQuery(String fieldName, Subquery<Object> subquery, CriteriaBuilder criteriaBuilder) {
            return SpecificationUtils.greaterThan(subquery.getSelection().as(String.class), fieldName);
        }
    },

    /**
     * Element of enum for constructing less than or equals operation
     */
    LESS_THAN_OR_EQUALS("le") {
        @Override
        public <T> Specification<T> buildQuery(String fieldName, Subquery<Object> subquery, CriteriaBuilder criteriaBuilder) {
            return SpecificationUtils.lessThanOrEqual(subquery.getSelection().as(String.class), fieldName);
        }
    },

    /**
     * Element of enum for constructing greater than or equals operation
     */
    GREATER_THAN_OR_EQUALS("ge") {
        @Override
        public <T> Specification<T> buildQuery(String fieldName, Subquery<Object> subquery, CriteriaBuilder criteriaBuilder) {
            return SpecificationUtils.greaterThanOrEqual(subquery.getSelection().as(String.class), fieldName);
        }
    },

    /**
     * Element of enum for constructing contains operation
     */
    CONTAINS("contains") {
        @Override
        public <T> Specification<T> buildQuery(String fieldName, Subquery<Object> subquery, CriteriaBuilder criteriaBuilder) {
            return SpecificationUtils.contains(subquery.getSelection(), fieldName);
        }
    };


    private final String operationName;

    private static final Map<String, NestedOperation> operationMap = Stream.of(values())
            .collect(toUnmodifiableMap(
                    NestedOperation::getOperationName,
                    type -> type));

    /**
     * Get NestedOperation by operation name
     *
     * @param typeName - operation name
     * @return NestedOperation
     */
    public static NestedOperation of(String typeName) {
        return Optional.ofNullable(operationMap.get(typeName))
                .orElseThrow(() -> new IllegalArgumentException(String.format("operation %s not supported", typeName)));
    }

    public abstract <T> Specification<T> buildQuery(String fieldName,
                                                    Subquery<Object> subquery,
                                                    CriteriaBuilder criteriaBuilder);
}
