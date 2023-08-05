package ru.sergkorot.dynamic.operation;

/**
 * @param <R> - interface for data access (for example, Specification)
 *            <p>
 *            interface for providing a different operations
 * @author Sergey Korotaev
 */
public interface OperationProvider<R> {

    /**
     * Method for constructing like operation
     *
     * @return condition for data access specified type
     */
    Operation<R> like();

    /**
     * Method for constructing equals operation
     *
     * @return condition for data access specified type
     */
    Operation<R> eq();

    /**
     * Method for constructing not equals operation
     *
     * @return condition for data access specified type
     */
    Operation<R> notEq();

    /**
     * Method for constructing in operation
     *
     * @return condition for data access specified type
     */
    Operation<R> in();

    /**
     * Method for constructing not in operation
     *
     * @return condition for data access specified type
     */
    Operation<R> notIn();

    /**
     * Method for constructing is null operation
     *
     * @return condition for data access specified type
     */
    Operation<R> isNull();

    /**
     * Method for constructing less than operation
     *
     * @return condition for data access specified type
     */
    Operation<R> lessThan();

    /**
     * Method for constructing greater than operation
     *
     * @return condition for data access specified type
     */
    Operation<R> greaterThan();

    /**
     * Method for constructing greater than or equals operation
     *
     * @return condition for data access specified type
     */
    Operation<R> greaterThanOrEquals();

    /**
     * Method for constructing less than or equals operation
     *
     * @return condition for data access specified type
     */
    Operation<R> lessThanOrEquals();

    /**
     * Method for constructing contains operation
     *
     * @return condition for data access specified type
     */
    Operation<R> contains();
}
