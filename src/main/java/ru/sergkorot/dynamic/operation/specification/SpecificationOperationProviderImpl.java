package ru.sergkorot.dynamic.operation.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.sergkorot.dynamic.operation.base.Operation;
import ru.sergkorot.dynamic.operation.base.OperationProvider;
import ru.sergkorot.dynamic.util.SpecificationUtils;

import java.util.Collections;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.sergkorot.dynamic.model.enums.ValueType.cast;
import static ru.sergkorot.dynamic.model.enums.ValueType.collectionCast;

/**
 * @author Sergey Korotaev
 * Service is realization Operation provider interface for building different specifications
 * @param <T> - the entity for which the request is being built
 * @see OperationProvider
 */
@Component
public class SpecificationOperationProviderImpl<T> implements OperationProvider<Specification<T>> {

    @Override
    public Operation<Specification<T>> like() {
        return param -> where(SpecificationUtils.findByColumnsLike(param.getValue().toString(), Collections.singleton(param.getName())));
    }

    @Override
    public Operation<Specification<T>> eq() {
        return param -> where(SpecificationUtils.findByColumnEquals(cast(param.getValue()), param.getName()));
    }

    @Override
    public Operation<Specification<T>> notEq() {
        return param -> where(SpecificationUtils.findByColumnNotEquals(cast(param.getValue()), param.getName()));
    }

    @Override
    public Operation<Specification<T>> in() {
        return param ->
                where(SpecificationUtils.findByCollectionIn(collectionCast(param.getValue()), param.getName()));
    }

    @Override
    public Operation<Specification<T>> notIn() {
        return param ->
                where(SpecificationUtils.findByCollectionNotIn(collectionCast(param.getValue()), param.getName()));
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
