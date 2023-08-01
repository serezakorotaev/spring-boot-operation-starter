package inova.korotaev.maven.operation;

import inova.korotaev.maven.model.BaseSearchParam;

/**
 * @author Sergey Korotaev
 * Common interface for buiding baseSearchParam to searching operation realization
 * @param <R> - interface for data access (for example, Specification)
 */
public interface Operation<R> {
    R buildOperation(BaseSearchParam baseSearchParam);
}
