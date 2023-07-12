package inova.korotaev.maven.operation;

import inova.korotaev.maven.model.PageAttribute;
import inova.korotaev.maven.model.SearchParam;
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

    public Specification<T> buildRequestByFilters(List<SearchParam> searchParams) {
        if (CollectionUtils.isEmpty(searchParams)) {
            return SpecificationUtils.findAll();
        }

        return buildSpecification(searchParams, GlueOperation.AND)
                .and(buildSpecification(searchParams, GlueOperation.OR));
    }

    public PageRequestWithOffset buildPageSettings(PageAttribute pageAttribute, List<String> searchSortFields) {
        if (Objects.isNull(pageAttribute)) {
            return PageRequestWithOffset.of(SortUtils.DEFAULT_OFFSET, SortUtils.DEFAULT_LIMIT, List.of());
        }
        return PageRequestWithOffset.of(pageAttribute.getOffset(),
                pageAttribute.getLimit(),
                SortUtils.makeSortOrders(searchSortFields, pageAttribute.getSortBy()));
    }

    private Specification<T> buildSpecification(List<SearchParam> searchParams, GlueOperation glueOperation) {
        return searchParams
                .stream()
                .filter(param -> glueOperation.equals(param.getGlue()))
                .map(param -> OperationType.of(param.getOperation()).getOperation(operationProvider).buildOperation(param))
                .reduce(glueOperation::glueOperation)
                .orElse(SpecificationUtils.findAll());
    }
}
