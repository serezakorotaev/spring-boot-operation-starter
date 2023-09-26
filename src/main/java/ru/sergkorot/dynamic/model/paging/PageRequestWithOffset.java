package ru.sergkorot.dynamic.model.paging;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import ru.sergkorot.dynamic.util.SortUtils;

import java.util.List;

/**
 * @author Sergey Korotaev
 * Implementing PageRequest with offset and without using page
 */
public class PageRequestWithOffset extends PageRequest {

    /**
     * offset field for getting it if we need
     */
    private final int offset;

    /**
     * Base Constructor for building PageRequest with offset and without using page
     *
     * @param offset - Shift relative to the beginning of the list
     * @param size   - Number of list items to return
     * @param sort   - settings for sort
     */
    public PageRequestWithOffset(final int offset, final int size, final Sort sort) {
        super(SortUtils.DEFAULT_OFFSET, size, sort);
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
    public static PageRequestWithOffset of(final int offset, final int limit, @NonNull final Sort sort) {
        return new PageRequestWithOffset(offset, limit, sort);
    }

    @Override
    public long getOffset() {
        return offset;
    }
}
