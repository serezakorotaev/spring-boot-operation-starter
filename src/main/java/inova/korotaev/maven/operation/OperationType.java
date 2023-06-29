package inova.korotaev.maven.operation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Getter
@AllArgsConstructor
public enum OperationType {
    IN("in", OperationProvider::in, OperationProcess.SEARCHING),
    NOT_IN("notIn", OperationProvider::notIn, OperationProcess.SEARCHING),
    LIKE("like", OperationProvider::like, OperationProcess.SEARCHING),
    EQUAL("eq", OperationProvider::eq, OperationProcess.SEARCHING),
    NOT_EQUAL("notEq", OperationProvider::notEq, OperationProcess.SEARCHING),
    IS_NULL("isNull", OperationProvider::isNull, OperationProcess.SEARCHING),
    LIMIT("limit", null, OperationProcess.PAGING),
    OFFSET("offset", null, OperationProcess.PAGING),
    SORT_BY("sortBy", null, OperationProcess.PAGING);

    private final String operationName;
    private final Function<OperationProvider<?>, Operation<?>> linkToOperation;
    private final OperationProcess process;


    private static final Map<String, OperationType> operationMap = Stream.of(values())
            .collect(toUnmodifiableMap(
                    OperationType::getOperationName,
                    type -> type));

    public static OperationType of(String typeName) {
        if (!StringUtils.hasText(typeName)) {
            return OperationType.EQUAL;
        }
        return Optional.ofNullable(operationMap.get(typeName))
                .orElseThrow(() -> new IllegalArgumentException(String.format("operation %s not found", typeName)));
    }

    /**
     * Определяем дальнейшую операцию
     * @param operationProvider см интерфейс OperationProvider
     * @return какую операцию мы будем выполнять Operation<R>
     * @param <R> обычно у нас Criteria
     */
    @SuppressWarnings("unchecked")
    public <R> Operation<R> getOperation(OperationProvider<R> operationProvider) {
        return (Operation<R>) linkToOperation.apply(operationProvider);
    }

    public enum OperationProcess {
        SEARCHING, PAGING
    }
}
