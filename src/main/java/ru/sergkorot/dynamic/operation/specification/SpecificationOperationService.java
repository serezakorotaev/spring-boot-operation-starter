package ru.sergkorot.dynamic.operation.specification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.sergkorot.dynamic.model.BaseSearchParam;
import ru.sergkorot.dynamic.model.ComplexSearchParam;
import ru.sergkorot.dynamic.model.PageAttribute;
import ru.sergkorot.dynamic.model.enums.GlueOperation;
import ru.sergkorot.dynamic.model.paging.PageRequestWithOffset;
import ru.sergkorot.dynamic.operation.OperationProvider;
import ru.sergkorot.dynamic.operation.OperationService;
import ru.sergkorot.dynamic.util.SortUtils;
import ru.sergkorot.dynamic.util.SpecificationUtils;

import java.util.List;
import java.util.Objects;

/**
 * @param <T> - entity for which building condition
 * @author Sergey Korotaev
 * Service for building specification for base and complex requests.
 * Also for building page request settings
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SpecificationOperationService<T> implements OperationService<Specification<T>> {

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
    public Specification<T> buildBaseByParams(List<BaseSearchParam> baseSearchParams, GlueOperation glue) {
        if (CollectionUtils.isEmpty(baseSearchParams)) {
            return SpecificationUtils.findAll();
        }

        return baseSearchParams
                .stream()
                .map(param -> buildOperation(param, operationProvider))
                .reduce(glue::glueSpecOperation)
                .orElse(SpecificationUtils.findAll());
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
    public Specification<T> buildComplexByParams(List<ComplexSearchParam> complexSearchParams, GlueOperation externalGlue) {
        return complexSearchParams.stream()
                .map(complexSearchParam ->
                        buildBaseByParams(
                                complexSearchParam.getBaseSearchParams(),
                                complexSearchParam.getInternalGlue()
                        )
                )
                .reduce(externalGlue::glueSpecOperation)
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
}
