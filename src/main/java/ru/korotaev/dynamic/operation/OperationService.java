package ru.korotaev.dynamic.operation;

import ru.korotaev.dynamic.model.ComplexSearchParam;
import ru.korotaev.dynamic.model.PageAttribute;
import ru.korotaev.dynamic.model.BaseSearchParam;
import ru.korotaev.dynamic.model.enums.GlueOperation;
import ru.korotaev.dynamic.model.enums.OperationType;
import ru.korotaev.dynamic.model.paging.PageRequestWithOffset;
import ru.korotaev.dynamic.util.SortUtils;
import ru.korotaev.dynamic.util.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @param <T>
 * @author Sergey Korotaev
 * Service for building specification for base and complex requests.
 * Also for building page request settings
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class OperationService<T> {

    private final OperationProvider<Specification<T>> operationProvider;

    /**
     * Create specification for base search request
     *
     * @param baseSearchParams - model for base search request
     * @param glue             - condition for gluing specifications
     * @return - specification for data request
     * @see BaseSearchParam
     * @see GlueOperation
     */
    public Specification<T> buildBaseSpecificationByParams(List<BaseSearchParam> baseSearchParams, GlueOperation glue) {
        if (CollectionUtils.isEmpty(baseSearchParams)) {
            return SpecificationUtils.findAll();
        }

        return baseSearchParams
                .stream()
                .map(param -> OperationType.of(param.getOperation()).getOperation(operationProvider).buildOperation(param))
                .reduce(glue::glueOperation)
                .orElse(SpecificationUtils.findAll());
    }

    /**
     * Create PageRequest extension for paging and sorting settings
     *
     * @param pageAttribute    - attribute class for pagination and sorting
     * @param searchSortFields - fields by which sorting is possible in the database
     * @return - PageRequestWithOffset
     * @see PageRequestWithOffset
     * @see PageAttribute
     */
    public PageRequestWithOffset buildPageSettings(PageAttribute pageAttribute, List<String> searchSortFields) {
        if (Objects.isNull(pageAttribute)) {
            return PageRequestWithOffset.of(SortUtils.DEFAULT_OFFSET, SortUtils.DEFAULT_LIMIT, List.of());
        }
        return PageRequestWithOffset.of(
                pageAttribute.getOffset(),
                pageAttribute.getLimit(),
                SortUtils.makeSortOrders(searchSortFields, pageAttribute.getSortBy())
        );
    }

    /**
     * Create specification for complex search request
     *
     * @param complexSearchParams - model for complex search request
     * @param externalGlue        - condition for gluing complex specification between each other
     * @return - specification for data request
     * @see ComplexSearchParam
     * @see GlueOperation
     */
    public Specification<T> buildComplexSpecificationByParams(List<ComplexSearchParam> complexSearchParams, GlueOperation externalGlue) {
        return complexSearchParams.stream()
                .map(complexSearchParam ->
                        buildBaseSpecificationByParams(
                                complexSearchParam.getBaseSearchParams(),
                                complexSearchParam.getInternalGlue()
                        )
                )
                .reduce(externalGlue::glueOperation)
                .orElse(SpecificationUtils.findAll());
    }
}
