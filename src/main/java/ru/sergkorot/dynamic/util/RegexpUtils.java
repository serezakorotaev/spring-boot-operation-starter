package ru.sergkorot.dynamic.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author Sergey Korotaev
 * Util is used for transforming string by pattern to list strings with strings for further paging
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegexpUtils {

    /**
     * Regexp pattern to remove all spaces
     */
    public static final String REGEXP_DELETE_ALL_WHITESPACES = "\\s+";

    /**
     * Regexp pattern corresponding to the example: "-id,name,-hello".
     */
    public static final String REGEXP_VALIDATION_SORT_BY_VALUES = "^(-?[A-z]*\\,)*(-?[A-z]+)$";

    /**
     * transforming string by pattern to list strings with strings for further paging
     *
     * @param fieldsNames - string with fields for sorting
     * @return - list with fields for sorting
     */
    public static List<String> transformToArrayFieldsNames(final String fieldsNames) {
        final List<String> fieldsNamesWithoutWhiteSpace = Arrays.asList(fieldsNames.replaceAll(REGEXP_DELETE_ALL_WHITESPACES, "").split(","));

        fieldsNamesWithoutWhiteSpace.forEach(field -> {
            final boolean isValidValue = field.matches(REGEXP_VALIDATION_SORT_BY_VALUES);
            if (!isValidValue) {
                throw new IllegalArgumentException(String.format("Fields names: [%s] - don't match with regexp pattern", fieldsNamesWithoutWhiteSpace));
            }
        });

        return fieldsNamesWithoutWhiteSpace;
    }
}
