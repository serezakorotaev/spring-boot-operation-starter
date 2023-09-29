package ru.sergkorot.dynamic.operation.base;

import ru.sergkorot.dynamic.model.BaseSearchParam;
import ru.sergkorot.dynamic.model.ComplexSearchParam;
import ru.sergkorot.dynamic.model.PageAttribute;
import ru.sergkorot.dynamic.model.enums.GlueOperation;
import ru.sergkorot.dynamic.model.enums.OperationType;
import ru.sergkorot.dynamic.model.paging.PageRequestWithOffset;
import ru.sergkorot.dynamic.util.SortUtils;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public interface OperationService<T> {

     T buildBaseByParams(List<BaseSearchParam> baseSearchParams, GlueOperation glue);


    T buildComplexByParams(List<ComplexSearchParam> complexSearchParams, GlueOperation externalGlue);

    /**
     * Create PageRequest extension for paging and sorting settings
     *
     * @param pageAttribute    - attribute class for pagination and sorting
     * @param searchSortFields - fields by which sorting is possible in the database
     * @return - PageRequestWithOffset
     * @see PageRequestWithOffset
     * @see PageAttribute
     */
    default PageRequestWithOffset buildPageSettings(PageAttribute pageAttribute, List<String> searchSortFields) {
        if (Objects.isNull(pageAttribute)) {
            return PageRequestWithOffset.of(SortUtils.DEFAULT_OFFSET, SortUtils.DEFAULT_LIMIT, List.of());
        }
        return PageRequestWithOffset.of(
                pageAttribute.getOffset(),
                pageAttribute.getLimit(),
                SortUtils.makeSortOrders(searchSortFields, pageAttribute.getSortBy())
        );
    }

    default T buildOperation(BaseSearchParam param, OperationProvider<T> operationProvider) {
        return OperationType.of(param.getOperation()).getOperation(operationProvider).buildOperation(param);
    }
}
