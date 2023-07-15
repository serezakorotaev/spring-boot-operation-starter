package inova.korotaev.maven.operation;

import inova.korotaev.maven.model.ComplexSearchParam;
import inova.korotaev.maven.model.PageAttribute;
import inova.korotaev.maven.model.BaseSearchParam;
import inova.korotaev.maven.model.enums.GlueOperation;
import inova.korotaev.maven.model.enums.OperationType;
import inova.korotaev.maven.model.paging.PageRequestWithOffset;
import inova.korotaev.maven.util.SortUtils;
import inova.korotaev.maven.util.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class OperationService<T> {

    private final OperationProvider<Specification<T>> operationProvider;

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
