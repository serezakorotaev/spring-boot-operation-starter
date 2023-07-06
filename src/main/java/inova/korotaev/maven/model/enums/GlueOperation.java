package inova.korotaev.maven.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.function.BiFunction;

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

    public abstract <T> Specification<T> glueOperation(Specification<T> first, Specification<T> second);

    private static <T> Specification<T> parse(BiFunction<Specification<T>, Specification<T>, Specification<T>> biFunction, Specification<T> first, Specification<T> second) {
       return biFunction.apply(first, second);
    }
}
