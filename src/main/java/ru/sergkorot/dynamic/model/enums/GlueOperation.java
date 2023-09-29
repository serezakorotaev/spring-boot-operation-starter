package ru.sergkorot.dynamic.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Criteria;

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
        public Criteria glueCriteriaOperation(Criteria c1, Criteria c2) {
            return c1.andOperator(c2);
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
        public Criteria glueCriteriaOperation(Criteria c1, Criteria c2) {
            return c1.orOperator(c2);
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
     * Method for gluing two criteria with each other
     *
     * @param c1 - first criteria for gluing
     * @param c2 - second criteria for gluing
     * @return - criteria constructed from two other
     */
    public abstract Criteria glueCriteriaOperation(Criteria c1, Criteria c2);
}
