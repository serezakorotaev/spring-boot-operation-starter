package inova.korotaev.maven.operation;

import inova.korotaev.maven.model.enums.GlueOperation;
import inova.korotaev.maven.model.enums.OperationType;
import inova.korotaev.maven.model.paging.PageRequestBuilder;
import inova.korotaev.maven.model.paging.PageRequestWithOffset;
import inova.korotaev.maven.model.SearchParam;
import inova.korotaev.maven.util.SortUtils;
import inova.korotaev.maven.util.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static inova.korotaev.maven.model.enums.OperationType.OperationProcess.PAGING;
import static inova.korotaev.maven.model.enums.OperationType.OperationProcess.SEARCHING;

@Service
@RequiredArgsConstructor
public class OperationService<T> {

    private final OperationProvider<Specification<T>> operationProvider;

    public Specification<T> buildRequestByFilters(List<SearchParam> searchParams) {
        if (CollectionUtils.isEmpty(searchParams)) {
            return SpecificationUtils.findAll();
        }

        return buildSpecification(searchParams, GlueOperation.AND)
                .and(buildSpecification(searchParams, GlueOperation.OR));
    }

    public PageRequestWithOffset buildPageSettings(List<SearchParam> searchParams, List<String> searchSortFields) {
        if (CollectionUtils.isEmpty(searchParams)) {
            return PageRequestWithOffset.of(SortUtils.DEFAULT_OFFSET, SortUtils.DEFAULT_LIMIT, List.of());
        }

        PageRequestBuilder pageRequestBuilder = new PageRequestBuilder();
        searchParams
                .stream()
                .filter(param -> OperationType.of(param.getOperation()).getProcess().equals(PAGING))
                .forEach(param -> {
                    switch (OperationType.of(param.getOperation())) {
                        case LIMIT -> pageRequestBuilder.setLimit((Integer) param.getValue());
                        case OFFSET -> pageRequestBuilder.setOffset((Integer) param.getValue());
                        case SORT_BY ->
                                pageRequestBuilder.setSortOrders(SortUtils.makeSortOrders(searchSortFields, param.getValue().toString()));
                    }
                });
        return PageRequestWithOffset.of(pageRequestBuilder.getOffset(), pageRequestBuilder.getLimit(), pageRequestBuilder.getSortOrders());
    }

    private Specification<T> buildSpecification(List<SearchParam> searchParams, GlueOperation glueOperation) {
        return searchParams
                .stream()
                .filter(param -> OperationType.of(param.getOperation()).getProcess().equals(SEARCHING))
                .filter(param -> glueOperation.equals(param.getGlue()))
                .map(param -> OperationType.of(param.getOperation()).getOperation(operationProvider).buildOperation(param))
                .reduce(glueOperation::glueOperation)
                .orElse(SpecificationUtils.findAll());
    }
}
