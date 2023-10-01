package ru.sergkorot.dynamic.operation.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.sergkorot.dynamic.model.BaseSearchParam;
import ru.sergkorot.dynamic.model.ComplexSearchParam;
import ru.sergkorot.dynamic.model.PageAttribute;
import ru.sergkorot.dynamic.model.enums.GlueOperation;
import ru.sergkorot.dynamic.operation.base.OperationProvider;
import ru.sergkorot.dynamic.operation.base.OperationService;
import ru.sergkorot.dynamic.util.SortUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sykorotaye
 * Service for building requests into the mongodb with pagination
 * @see OperationService
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class CriteriaOperationService implements OperationService<Criteria> {

    private final OperationProvider<Criteria> operationProvider;

    @Override
    public Criteria buildBaseByParams(List<BaseSearchParam> baseSearchParams, GlueOperation glue) {
        if (CollectionUtils.isEmpty(baseSearchParams)) {
            return new Criteria();
        }

        List<Criteria> criteriaList = baseSearchParams
                .stream()
                .map(param -> buildOperation(param, operationProvider))
                .collect(Collectors.toList());

        return glue.glueCriteriaOperation(criteriaList);
    }

    @Override
    public Criteria buildComplexByParams(List<ComplexSearchParam> complexSearchParams, GlueOperation externalGlue) {
        List<Criteria> criteriaList = complexSearchParams.stream()
                .map(complexSearchParam ->
                        buildBaseByParams(
                                complexSearchParam.getBaseSearchParams(),
                                complexSearchParam.getInternalGlue()
                        ))
                .collect(Collectors.toList());
        return externalGlue.glueCriteriaOperation(criteriaList);
    }

    /**
     * Create PageRequest extension for paging and sorting settings
     *
     * @param query            - query for mongodb request
     * @param pageAttribute    - attribute class for pagination and sorting
     * @param searchSortFields - fields by which sorting is possible in the database
     * @see PageAttribute
     * @return Query
     */
    public Query buildPageSettings(Query query, PageAttribute pageAttribute, List<String> searchSortFields) {
        return query
                .limit(pageAttribute.getLimit())
                .skip(pageAttribute.getOffset())
                .with(SortUtils.makeSort(searchSortFields, pageAttribute.getSortBy()));
    }
}
