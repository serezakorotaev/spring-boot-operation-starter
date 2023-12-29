package ru.sergkorot.dynamic.operation;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.sergkorot.dynamic.glue.Glue;
import ru.sergkorot.dynamic.glue.GlueOperationProvider;

@Component
public class SpecificationGlueOperationProviderImpl<T> implements GlueOperationProvider<Specification<T>> {

    @Override
    public Glue<Specification<T>> and() {
        return Specification::allOf;
    }

    @Override
    public Glue<Specification<T>> or() {
        return Specification::anyOf;
    }
}
