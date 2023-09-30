package ru.sergkorot.dynamic.operation.query;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import ru.sergkorot.dynamic.operation.base.Operation;
import ru.sergkorot.dynamic.operation.base.OperationProvider;

import java.util.Arrays;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static ru.sergkorot.dynamic.model.enums.ValueType.cast;
import static ru.sergkorot.dynamic.model.enums.ValueType.collectionCast;

@Component
public class QueryOperationProviderImpl implements OperationProvider<Criteria> {
    @Override
    public Operation<Criteria> like() {
        return param -> where(param.getName()).regex(param.getValue().toString(), "i");
    }

    @Override
    public Operation<Criteria> eq() {
        return param -> where(param.getName()).is(cast(param.getValue()));
    }

    @Override
    public Operation<Criteria> notEq() {
        return param -> where(param.getName()).is(cast(param.getValue())).not();
    }

    @Override
    public Operation<Criteria> in() {
        return param -> where(param.getName()).in(collectionCast(param.getValue()));
    }

    @Override
    public Operation<Criteria> notIn() {
        return param -> where(param.getName()).in(collectionCast(param.getValue())).not();
    }

    @Override
    public Operation<Criteria> isNull() {
        return param -> where(param.getName()).isNull();
    }

    @Override
    public Operation<Criteria> lessThan() {
        return param -> where(param.getName()).lt(cast(param.getValue()));
    }

    @Override
    public Operation<Criteria> greaterThan() {
        return param -> where(param.getName()).gt(cast(param.getValue()));
    }

    @Override
    public Operation<Criteria> greaterThanOrEquals() {
        return param -> where(param.getName()).gte(cast(param.getValue()));
    }

    @Override
    public Operation<Criteria> lessThanOrEquals() {
        return param -> where(param.getName()).lte(cast(param.getValue()));
    }

    @Override
    public Operation<Criteria> contains() {
        return param -> where(param.getName()).andOperator(
                Arrays.stream(param.getValue().toString().split(","))
                        .map(value -> where(param.getName()).regex(value, "i"))
                        .toArray(Criteria[]::new)
        );
    }

    /**
     * Find all documents into the database
     *
     * @return Criteria
     */
    public static Criteria findAll() {
        return Criteria.where("_id").isNull().not();
    }
}
