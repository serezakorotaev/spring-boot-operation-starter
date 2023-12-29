package ru.sergkorot.dynamic;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import ru.sergkorot.dynamic.util.SortUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SortUtilsTest {

    @Test
    void makeSortOrdersWithValidNameSuccess() {
        List<String> validNames = List.of("id", "name", "hello");
        String valuesForSorting = "-id,name,-hello";

        List<Sort.Order> orders = SortUtils.makeSortOrders(validNames, valuesForSorting);

        orders.forEach(order -> {
            switch (order.getProperty()) {
               case "id", "hello" -> assertEquals(Sort.Direction.DESC, order.getDirection());
               case "name" -> assertEquals(Sort.Direction.ASC, order.getDirection());
            }
        });
    }

    @Test
    void makeSortOrdersWithExcessNameException() {

        List<String> validNames = List.of("id", "name");
        String valuesForSorting = "-id,name,-hello";

        assertThrows(IllegalArgumentException.class, () -> SortUtils.makeSortOrders(validNames, valuesForSorting));
    }

    @Test
    void makeSortOrdersWithInvalidNameException() {

        List<String> validNames = List.of("id", "name", "hello");
        String valuesForSorting = "-idna,name,-hello";

        assertThrows(IllegalArgumentException.class, () -> SortUtils.makeSortOrders(validNames, valuesForSorting));
    }
}
