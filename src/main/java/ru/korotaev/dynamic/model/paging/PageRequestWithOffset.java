package ru.korotaev.dynamic.model.paging;

import ru.korotaev.dynamic.util.SortUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author Sergey Korotaev
 * Implementing PageRequest with offset and without using page
 */
public class PageRequestWithOffset extends PageRequest {

    private final int offset;

    public PageRequestWithOffset(final int offset, final int size, final Sort sort) {
        super(SortUtils.DISABLE_PAGE_VALUE, size, sort);
        this.offset = offset;
    }

    /**
     * Static method for building PageRequest extension
     *
     * @param offset     - Shift relative to the beginning of the list
     * @param limit      - Number of list items to return
     * @param sortOrders - list with settings for sort
     * @return - PageRequestWithOffset
     */
    public static PageRequestWithOffset of(Integer offset, Integer limit,
                                           final List<Sort.Order> sortOrders) {
        if (offset == null) {
            offset = SortUtils.DEFAULT_OFFSET;
        }
        if (limit == null) {
            limit = SortUtils.DEFAULT_LIMIT;
        }
        final Sort sort = (sortOrders == null || sortOrders.isEmpty())
                ? Sort.unsorted()
                : Sort.by(sortOrders);

        return new PageRequestWithOffset(offset, limit, sort);
    }

    /**
     * Static method for building PageRequest extension
     *
     * @param offset - Shift relative to the beginning of the list
     * @param limit  - Number of list items to return
     * @param sort   - settings for sort
     * @return - PageRequestWithOffset
     */
    public static PageRequestWithOffset of(final int offset, final int limit, final Sort sort) {
        return new PageRequestWithOffset(offset, limit, sort);
    }

    @Override
    public long getOffset() {
        return offset;
    }
}
