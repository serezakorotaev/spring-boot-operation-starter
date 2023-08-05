package ru.sergkorot.dynamic.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.BooleanUtils;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Sergey Korotaev
 * Enum which has types for casts
 * When an object comes in a request, its type is determined and the collection or the object itself is cast into its type for searching
 */
@Getter
@AllArgsConstructor
public enum ValueType {

    /**
     * Element of enum for casting object to the string type
     */
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

    /**
     * Element of enum for casting object to the long type
     */
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

    /**
     * Element of enum for casting object to the double type
     */
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

    /**
     * Element of enum for casting object to the boolean type
     */
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

    /**
     * Element of enum for casting object to the instant type
     */
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
     * Method that checks the type of the incoming object for further filtering by condition
     *
     * @param u   - an object whose type is determined for further insertion
     * @param <U> - generic by incoming object
     * @return - true/false depending on the condition
     */
    public abstract <U> Boolean checkValueType(U u);

    /**
     * Method that casts the entire collection to the desired type
     *
     * @param obj - an object, which is then parsed into a collection of the desired type
     * @return - collection of the desired type
     */
    public abstract Collection<?> castCollection(Object obj);

    /**
     * Method that casts an object to the desired type
     *
     * @param obj - object, which is then cast to the desired type
     * @return - Object of the desired type
     */
    public abstract Object simpleCast(Object obj);

    /**
     * Static method that is called on an object to determine its type
     * and cast to the appropriate type
     *
     * @param value - incoming object
     * @return - Object of the desired type
     */
    public static Object cast(Object value) {
        return Arrays.stream(ValueType.values())
                .filter(valueType -> valueType.checkValueType(value))
                .findFirst()
                .map(valueType -> valueType.simpleCast(value))
                .orElseThrow();
    }

    /**
     * A static method that is called on a collection to
     * determining the types of elements in the collection and casting the collection to the appropriate type
     *
     * @param value - incoming collection as an object
     * @return - collection of the desired type
     */
    public static Collection<?> collectionCast(Object value) {
        String firstElement = value.toString().split(",")[0];
        return Arrays.stream(ValueType.values())
                .filter(valueType -> valueType.checkValueType(firstElement))
                .findFirst()
                .map(valueType -> valueType.castCollection(value))
                .orElseThrow();
    }

    /**
     * Checking a string for the possibility of deserialization to an instance
     *
     * @param value - incoming string to check
     * @return - true/false dependence on condition
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
