package inova.korotaev.maven.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SortUtils {

    public static final int DISABLE_PAGE_VALUE = 0;
    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_LIMIT = 200;

    /**
     * Метод для получения порядка сортировки.
     *
     * @param validNames список имен параметров, которые подходят под сортировку
     * @param sortValues строка с параметрами для сортировки, вида: "id,-name"
     *                   "-value" - сортировка по DESC;
     *                   "value" - сортировка по ASC
     */
    public static List<Sort.Order> makeSortOrders(final List<String> validNames, final String sortValues) {
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

    private static String checkName(final List<String> validNames, String name) {
        if (!validNames.contains(name.substring(1))) {
            throw new IllegalArgumentException(String.format("Not found parameter with name: %s", name));
        }
        return name;
    }
}
