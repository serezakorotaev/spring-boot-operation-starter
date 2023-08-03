package ru.korotaev.dynamic.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.function.BiFunction;

/**
 * @author Sergey Korotaev
 * Enum for And/or glue operation
 */
@Getter
@AllArgsConstructor
public enum GlueOperation {
    AND {
        @Override
        public <T> Specification<T> glueOperation(Specification<T> first, Specification<T> second) {
            return parse(Specification::and, first, second);
        }
    },
    OR {
        @Override
        public <T> Specification<T> glueOperation(Specification<T> first, Specification<T> second) {
            return parse(Specification::or, first, second);
        }
    };

    /**
     * Method for gluing two specification with each other
     *
     * @param first  - first specification for gluing
     * @param second - second specification for gluing
     * @return - specification constructed from two other
     */
    public abstract <T> Specification<T> glueOperation(Specification<T> first, Specification<T> second);

    private static <T> Specification<T> parse(BiFunction<Specification<T>, Specification<T>, Specification<T>> biFunction, Specification<T> first, Specification<T> second) {
        return biFunction.apply(first, second);
    }
}
