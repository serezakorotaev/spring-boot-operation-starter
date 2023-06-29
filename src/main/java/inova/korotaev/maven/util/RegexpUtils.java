package inova.korotaev.maven.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegexpUtils {

    /**
     * Regexp паттерн для удаления всех пробелов.
     */
    public static final String REGEXP_DELETE_ALL_WHITESPACES = "\\s+";

    /**
     * Regexp паттерн, соответствующий примеру: "-id,name,-hello".
     */
    public static final String REGEXP_VALIDATION_SORT_BY_VALUES = "^(-?[A-z]*\\,)*(-?[A-z]+)$";

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
