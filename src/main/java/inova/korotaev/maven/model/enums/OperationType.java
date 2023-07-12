package inova.korotaev.maven.model.enums;

import inova.korotaev.maven.operation.Operation;
import inova.korotaev.maven.operation.OperationProvider;
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
    IN("in", OperationProvider::in),
    NOT_IN("notIn", OperationProvider::notIn),
    LIKE("like", OperationProvider::like),
    EQUAL("eq", OperationProvider::eq),
    NOT_EQUAL("notEq", OperationProvider::notEq),
    IS_NULL("isNull", OperationProvider::isNull);

    private final String operationName;
    private final Function<OperationProvider<?>, Operation<?>> linkToOperation;


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
     * @param <R> Specification
     */
    @SuppressWarnings("unchecked")
    public <R> Operation<R> getOperation(OperationProvider<R> operationProvider) {
        return (Operation<R>) linkToOperation.apply(operationProvider);
    }
}
