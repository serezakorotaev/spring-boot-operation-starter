package ru.sergkorot.dynamic.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

/**
 * @author Sergey Korotaev
 * Enum for And/or glue operation
 */
@Getter
@AllArgsConstructor
public enum GlueOperation {

    /**
     * And gluing for operation
     */
    AND {
        @Override
        public <T> Specification<T> glueSpecOperation(Specification<T> first, Specification<T> second) {
            return first.and(second);
        }

        @Override
        public Criteria glueCriteriaOperation(List<Criteria> criteriaList) {
            return new Criteria().andOperator(criteriaList);
        }

    },

    /**
     * Or gluing for operation
     */
    OR {
        @Override
        public <T> Specification<T> glueSpecOperation(Specification<T> first, Specification<T> second) {
            return first.or(second);
        }

        @Override
        public Criteria glueCriteriaOperation(List<Criteria> criteriaList) {
            return new Criteria().orOperator(criteriaList);
        }

    };

    /**
     * Method for gluing two specification with each other
     *
     * @param <T>    - entity for which building condition
     * @param first  - first specification for gluing
     * @param second - second specification for gluing
     * @return - specification constructed from two other
     */
    public abstract <T> Specification<T> glueSpecOperation(Specification<T> first, Specification<T> second);


    /**
     * Method for gluing criteria with criteria list by condition
     *
     * @param criteriaList - list with criteria parameters
     * @return - criteria constructed from others
     */
    public abstract Criteria glueCriteriaOperation(List<Criteria> criteriaList);
}
