package ru.sergkorot.dynamic.model.enums;

import ru.sergkorot.dynamic.operation.Operation;
import ru.sergkorot.dynamic.operation.OperationProvider;
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
    IN("in", OperationProvider::in),
    NOT_IN("notIn", OperationProvider::notIn),
    LIKE("like", OperationProvider::like),
    EQUAL("eq", OperationProvider::eq),
    NOT_EQUAL("notEq", OperationProvider::notEq),
    IS_NULL("isNull", OperationProvider::isNull),
    LESS_THAN("lt", OperationProvider::lessThan),
    GREATER_THAN("gt", OperationProvider::greaterThan),
    LESS_THAN_OR_EQUALS("le", OperationProvider::lessThanOrEquals),
    GREATER_THAN_OR_EQUALS("ge", OperationProvider::greaterThanOrEquals),
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
     * @return which one operation will do from Operation<R>
     * @see OperationProvider
     */
    @SuppressWarnings("unchecked")
    public <R> Operation<R> getOperation(OperationProvider<R> operationProvider) {
        return (Operation<R>) linkToOperation.apply(operationProvider);
    }
}
