package ru.sergkorot.dynamic.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sergey Korotaev
 * @see RegexpUtils
 * Util is used for transforming string by pattern inside (uses RegexpUtils) to list org.springframework.data.domain.Sort.Order class
 * and checking by validNames if it can build Sort.Order by these sortValue names
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SortUtils {

    /**
     * Default offset number
     */
    public static final int DEFAULT_OFFSET = 0;

    /**
     * Default offset number
     */
    public static final int DEFAULT_LIMIT = 200;

    /**
     * Method for getting sort order
     *
     * @param validNames list of parameter names that match sorting
     * @param sortValues string with parameters for sorting, type: "id,-name"
     *                   "-value" - sort by DESC;
     *                   "value" - sort by ASC
     * @return Sort
     */
    public static Sort makeSort(final Collection<String> validNames, final String sortValues) {
        return Sort.by(makeSortOrders(validNames, sortValues));
    }

    /**
     * Method for getting sort order
     *
     * @param validNames list of parameter names that match sorting
     * @param sortValues string with parameters for sorting, type: "id,-name"
     *                   "-value" - sort by DESC;
     *                   "value" - sort by ASC
     * @return Sort.Order - list with sorting parameters
     */
    public static List<Sort.Order> makeSortOrders(final Collection<String> validNames, final String sortValues) {
        if (sortValues == null || sortValues.isBlank()) {
            return Collections.emptyList();
        }

        final List<String> splitSortValues = RegexpUtils.transformToArrayFieldsNames(sortValues);

        return splitSortValues.stream()
                .map(name -> checkName(validNames, name))
                .map(SortUtils::getOrder)
                .collect(Collectors.toList());
    }

    private static Sort.Order getOrder(String name) {
        if (name.contains("-")) {
            return Sort.Order.desc(name.substring(1));
        } else {
            return Sort.Order.asc(name);
        }
    }

    private static String checkName(final Collection<String> validNames, String name) {
        if (!validNames.contains(getName(name))) {
            throw new IllegalArgumentException(String.format("Not found parameter with name: %s", name));
        }
        return name;
    }

    private static String getName(String name) {
        return name.startsWith("-") ? name.substring(1) : name;
    }
}
