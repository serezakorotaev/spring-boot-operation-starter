package ru.sergkorot.dynamic.operation.base;

import ru.sergkorot.dynamic.model.BaseSearchParam;
import ru.sergkorot.dynamic.model.ComplexSearchParam;
import ru.sergkorot.dynamic.model.enums.GlueOperation;
import ru.sergkorot.dynamic.model.enums.OperationType;

import java.util.List;

/**
 * Base interface for building requests into the databases with different parameters and glue option
 *
 * @param <T> - interface for data access (for example, Specification)
 */
@SuppressWarnings("unused")
public interface OperationService<T> {

    /**
     * Method for building request with base request parameters
     *
     * @param baseSearchParams - model for base search request
     * @param glue             - condition for gluing parameters for request
     * @return - interface for data access (for example, Specification)
     */
    T buildBaseByParams(List<BaseSearchParam> baseSearchParams, GlueOperation glue);

    /**
     * Method for building request with complex request parameters
     *
     * @param complexSearchParams - model for complex search request
     * @param externalGlue        - condition for gluing complex parameters between each other
     * @return - interface for data access (for example, Specification)
     */
    T buildComplexByParams(List<ComplexSearchParam> complexSearchParams, GlueOperation externalGlue);

    /**
     * @param param             - model for base search request
     * @param operationProvider - interface for providing a different operations
     * @return - interface for data access (for example, Specification)
     * @see OperationProvider
     */
    default T buildOperation(BaseSearchParam param, OperationProvider<T> operationProvider) {
        return OperationType.of(param.getOperation()).getOperation(operationProvider).buildOperation(param);
    }
}
