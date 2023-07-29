package inova.korotaev.maven.operation;

import inova.korotaev.maven.util.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class SpecificationOperationProviderImpl<T> implements OperationProvider<Specification<T>> {

    @Override
    public Operation<Specification<T>> like() {
        return param -> where(SpecificationUtils.findByColumnsLike(param.getValue().toString(), Collections.singleton(param.getName())));
    }

    @Override
    public Operation<Specification<T>> eq() {
        return param -> where(SpecificationUtils.findByColumnEquals(param.getValue(), param.getName()));
    }

    @Override
    public Operation<Specification<T>> notEq() {
        return param -> where(SpecificationUtils.findByColumnNotEquals(param.getValue(), param.getName()));
    }

    @Override
    public Operation<Specification<T>> in() {
        return param ->
                where(SpecificationUtils.findByCollectionIn(Arrays.asList(param.getValue().toString().split(",")), param.getName()));
    }

    @Override
    public Operation<Specification<T>> notIn() {
        return param ->
                where(SpecificationUtils.findByCollectionNotIn(Arrays.asList(param.getValue().toString().split(",")), param.getName()));
    }

    @Override
    public Operation<Specification<T>> isNull() {
        return param -> where(SpecificationUtils.findByColumnIsNull(param.getName()));
    }

    @Override
    public Operation<Specification<T>> lessThan() {
        return param -> where(SpecificationUtils.lessThan(param.getValue().toString(), param.getName()));
    }

    @Override
    public Operation<Specification<T>> greaterThan() {
        return param -> where(SpecificationUtils.greaterThan(param.getValue().toString(), param.getName()));
    }

    @Override
    public Operation<Specification<T>> greaterThanOrEquals() {
        return param -> where(SpecificationUtils.greaterThanOrEqual(param.getValue().toString(), param.getName()));
    }

    @Override
    public Operation<Specification<T>> lessThanOrEquals() {
        return param -> where(SpecificationUtils.lessThanOrEqual(param.getValue().toString(), param.getName()));
    }

    public Operation<Specification<T>> contains() {
        return param -> where(SpecificationUtils.contains(param.getValue(), param.getName()));
    }
}
