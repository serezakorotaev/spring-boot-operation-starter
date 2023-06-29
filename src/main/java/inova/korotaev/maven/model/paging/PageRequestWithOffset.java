package inova.korotaev.maven.model.paging;

import inova.korotaev.maven.util.SortUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Реализация PageRequest с offset и без использования page.
 */
public class PageRequestWithOffset extends PageRequest {

    private final int offset;

    public PageRequestWithOffset(final int offset, final int size, final Sort sort) {
        super(SortUtils.DISABLE_PAGE_VALUE, size, sort);
        this.offset = offset;
    }

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

    @Override
    public long getOffset() {
        return offset;
    }
}
