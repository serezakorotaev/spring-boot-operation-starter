package inova.korotaev.maven.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.BooleanUtils;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Enum, который содержит типы, предназначенные для каста.
 * Когда объект приходит в запросе, определяется его тип и производится каст коллекции в его тип для поиска
 */
@Getter
@AllArgsConstructor
public enum ValueType {

    STRING_VARIABLE(String.class) {
        @Override
        public <U> Boolean checkValueType(U u) {
            return u instanceof String && !checkInstant(u);
        }

        @Override
        public Collection<?> castCollection(Object obj) {
            return Arrays.asList(obj.toString().split(","));
        }

        @Override
        public Object simpleCast(Object obj) {
            return obj;
        }

    },
    LONG_VARIABLE(Long.class) {
        @Override
        public <U> Boolean checkValueType(U u) {
            try {
                Long.parseLong(String.valueOf(u));
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }

        @Override
        public Collection<?> castCollection(Object obj) {
            return Arrays.asList(obj.toString().split(","));
        }

        @Override
        public Object simpleCast(Object obj) {
            return Long.parseLong(obj.toString());
        }

    },
    DOUBLE_VARIABLE(Double.class) {
        @Override
        public <U> Boolean checkValueType(U u) {
            try {
                double v = Double.parseDouble(String.valueOf(u));
                if (Double.isInfinite(v) || Double.isNaN(v)) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }

        @Override
        public Collection<?> castCollection(Object obj) {
            return Arrays.asList(obj.toString().split(","));
        }

        @Override
        public Object simpleCast(Object obj) {
            return Double.parseDouble(obj.toString());
        }

    },
    BOOLEAN_VARIABLE(Boolean.class) {
        @Override
        public <U> Boolean checkValueType(U u) {
            return BooleanUtils.toBooleanObject(String.valueOf(u)) != null;
        }

        @Override
        public Collection<?> castCollection(Object obj) {
            return Arrays.stream(obj.toString().split(","))
                    .map(BooleanUtils::toBooleanObject)
                    .collect(Collectors.toList());
        }

        @Override
        public Object simpleCast(Object obj) {
            return BooleanUtils.toBooleanObject(obj.toString());
        }


    },
    DATETIME_VARIABLE(Instant.class) {
        @Override
        public <U> Boolean checkValueType(U u) {
            return u instanceof Instant || u instanceof String && checkInstant(u);
        }

        @Override
        public Collection<?> castCollection(Object obj) {
            return Arrays.stream(obj.toString().split(","))
                    .map(Instant::parse)
                    .collect(Collectors.toList());
        }

        @Override
        public Object simpleCast(Object obj) {
            return Instant.parse(obj.toString());
        }

    };

    private final Class<?> clazz;

    /**
     * Метод, который производит проверку типа приходящего объекта, для дальнейшей фильтрации по условию
     *
     * @param u   - объект, у которого определяется тип для дальнейшей вставки
     * @param <U> - generic приходящего объекта
     * @return - true/false в зависимости от выполнения условия
     */
    public abstract <U> Boolean checkValueType(U u);

    public abstract Collection<?> castCollection(Object obj);

    public abstract Object simpleCast(Object obj);

    public static Object cast(Object value) {
        return Arrays.stream(ValueType.values())
                .filter(valueType -> valueType.checkValueType(value))
                .findFirst()
                .map(valueType -> valueType.simpleCast(value))
                .orElseThrow();
    }

    public static Collection<?> collectionCast(Object value) {
        String firstElement = value.toString().split(",")[0];
        return Arrays.stream(ValueType.values())
                .filter(valueType -> valueType.checkValueType(firstElement))
                .findFirst()
                .map(valueType -> valueType.castCollection(value))
                .orElseThrow();
    }

    /**
     * Проверка строки на возможность десериализации в инстант
     *
     * @param value - приходящая строка для проверки
     * @return - true/false в зависимости от выполнения условия
     */
    private static boolean checkInstant(Object value) {
        try {
            Instant.parse((String) value);
        } catch (DateTimeException e) {
            return false;
        }
        return true;
    }
}
