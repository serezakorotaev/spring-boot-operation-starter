package ru.sergkorot.dynamic.operation;

import ru.sergkorot.dynamic.model.BaseSearchParam;

/**
 * @author Sergey Korotaev
 * Common interface for buiding baseSearchParam to searching operation realization
 * @param <R> - interface for data access (for example, Specification)
 */
public interface Operation<R> {

    /**
     * Base method from base interface for building conditions
     * @see BaseSearchParam
     * @param baseSearchParam - model for base search request
     * @return - data access implementation (for example, Specification)
     */
    R buildOperation(BaseSearchParam baseSearchParam);
}
