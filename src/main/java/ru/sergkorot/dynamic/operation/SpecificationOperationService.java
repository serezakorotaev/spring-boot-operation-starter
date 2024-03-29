package ru.sergkorot.dynamic.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.sergkorot.dynamic.enums.NestedOperation;
import ru.sergkorot.dynamic.glue.GlueOperationProvider;
import ru.sergkorot.dynamic.model.BaseSearchParam;
import ru.sergkorot.dynamic.model.ComplexSearchParam;
import ru.sergkorot.dynamic.model.PageAttribute;
import ru.sergkorot.dynamic.model.enums.GlueOperation;
import ru.sergkorot.dynamic.model.paging.PageRequestWithOffset;
import ru.sergkorot.dynamic.util.SortUtils;
import ru.sergkorot.dynamic.util.SpecificationUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @param <T> - entity for which building condition
 * @author Sergey Korotaev
 * Service for building specification for base and complex requests.
 * Also for building page request settings
 */
@Service
@SuppressWarnings("unused")
public class SpecificationOperationService<T> implements OperationService<Specification<T>> {

    private static final String NESTED = "nst:";
    private final OperationProvider<Specification<T>> operationProvider;
    private final GlueOperationProvider<Specification<T>> glueOperationProvider;
    private final Map<String, ManualOperationProvider<Specification<T>>> manualOperationProviderMap;
    private final ObjectMapper objectMapper;


    public SpecificationOperationService(OperationProvider<Specification<T>> operationProvider,
                                         GlueOperationProvider<Specification<T>> glueOperationProvider,
                                         List<ManualOperationProvider<Specification<T>>> manualOperationProviders,
                                         ObjectMapper objectMapper) {
        this.operationProvider = operationProvider;
        this.glueOperationProvider = glueOperationProvider;
        this.manualOperationProviderMap = CollectionUtils.isEmpty(manualOperationProviders)
                ? null
                : manualOperationProviders.stream().collect(Collectors.toMap(ManualOperationProvider::fieldName, Function.identity()));
        this.objectMapper = objectMapper;
    }

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

        List<Specification<T>> specifications = baseSearchParams
                .stream()
                .map(this::constructSpecification)
                .toList();

        return buildGlue(glueOperationProvider, specifications, glue);
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

        List<Specification<T>> specifications = complexSearchParams.stream()
                .map(complexSearchParam ->
                        buildBaseByParams(
                                complexSearchParam.getBaseSearchParams(),
                                complexSearchParam.getInternalGlue()
                        )
                )
                .toList();

        return buildGlue(glueOperationProvider, specifications, externalGlue);
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

    private Specification<T> constructSpecification(BaseSearchParam param) {
        if (!CollectionUtils.isEmpty(manualOperationProviderMap) && manualOperationProviderMap.containsKey(param.getName())) {
            return manualOperationProviderMap.get(param.getName()).buildOperation(param);
        }

        if (param.getOperation().startsWith(NESTED)) {
            param.setOperation(param.getOperation().replace(NESTED, ""));
            return buildNestedOperation(param);
        }

        return buildOperation(param, operationProvider);
    }

    private Specification<T> buildNestedOperation(BaseSearchParam param) {
        return (root, query, criteriaBuilder) -> {

            Subquery<Object> subquery = query.subquery(Object.class);
            Root<T> subroot = subquery.from(root.getModel());

            subquery.select(subroot.get(param.getName()));

            ComplexSearchParam nestedParam = objectMapper.convertValue(param.getValue(), ComplexSearchParam.class);

            Predicate predicate = buildBaseByParams(nestedParam.getBaseSearchParams(), nestedParam.getInternalGlue())
                    .toPredicate(subroot, query, criteriaBuilder);

            subquery.where(predicate);

            return NestedOperation.of(param.getOperation())
                    .<T>buildQuery(
                            param.getName(),
                            subquery
                    )
                    .toPredicate(root, query, criteriaBuilder);
        };
    }
}
