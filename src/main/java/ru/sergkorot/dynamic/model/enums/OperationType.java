package ru.sergkorot.dynamic.model.enums;

import ru.sergkorot.dynamic.operation.base.Operation;
import ru.sergkorot.dynamic.operation.base.OperationProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableMap;

/**
 * @author Sergey Korotaev
 * Enum which name of the operation and link for operation method by name
 */
@Getter
@AllArgsConstructor
public enum OperationType {

    /**
     * Element of enum for constructing in operation
     */
    IN("in", OperationProvider::in),

    /**
     * Element of enum for constructing not in operation
     */
    NOT_IN("notIn", OperationProvider::notIn),

    /**
     * Element of enum for constructing like operation
     */
    LIKE("like", OperationProvider::like),

    /**
     * Element of enum for constructing equals operation
     */
    EQUAL("eq", OperationProvider::eq),

    /**
     * Element of enum for constructing not equals operation
     */
    NOT_EQUAL("notEq", OperationProvider::notEq),

    /**
     * Element of enum for constructing is null operation
     */
    IS_NULL("isNull", OperationProvider::isNull),

    /**
     * Element of enum for constructing less than operation
     */
    LESS_THAN("lt", OperationProvider::lessThan),

    /**
     * Element of enum for constructing greater than operation
     */
    GREATER_THAN("gt", OperationProvider::greaterThan),

    /**
     * Element of enum for constructing less than or equals operation
     */
    LESS_THAN_OR_EQUALS("le", OperationProvider::lessThanOrEquals),

    /**
     * Element of enum for constructing greater than or equals operation
     */
    GREATER_THAN_OR_EQUALS("ge", OperationProvider::greaterThanOrEquals),

    /**
     * Element of enum for constructing contains operation
     */
    CONTAINS("contains", OperationProvider::contains);

    private final String operationName;
    private final Function<OperationProvider<?>, Operation<?>> linkToOperation;


    private static final Map<String, OperationType> operationMap = Stream.of(values())
            .collect(toUnmodifiableMap(
                    OperationType::getOperationName,
                    type -> type));

    /**
     * Get OperationType by operation name
     *
     * @param typeName - operation name
     * @return OperationType
     */
    public static OperationType of(String typeName) {
        if (!StringUtils.hasText(typeName)) {
            return OperationType.EQUAL;
        }
        return Optional.ofNullable(operationMap.get(typeName))
                .orElseThrow(() -> new IllegalArgumentException(String.format("operation %s not found", typeName)));
    }

    /**
     * Determine the next operation
     *
     * @param operationProvider OperationProvider
     * @param <R>               interfaces for data access (for example, Specification)
     * @return which one operation will do from Operation
     * @see OperationProvider
     */
    @SuppressWarnings("unchecked")
    public <R> Operation<R> getOperation(OperationProvider<R> operationProvider) {
        return (Operation<R>) linkToOperation.apply(operationProvider);
    }
}
