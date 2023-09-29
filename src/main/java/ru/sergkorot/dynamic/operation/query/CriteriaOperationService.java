package ru.sergkorot.dynamic.operation.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.sergkorot.dynamic.model.BaseSearchParam;
import ru.sergkorot.dynamic.model.ComplexSearchParam;
import ru.sergkorot.dynamic.model.enums.GlueOperation;
import ru.sergkorot.dynamic.model.enums.OperationType;
import ru.sergkorot.dynamic.operation.base.OperationProvider;
import ru.sergkorot.dynamic.operation.base.OperationService;

import java.util.List;

import static ru.sergkorot.dynamic.operation.query.QueryOperationProviderImpl.findAll;

@Service
@RequiredArgsConstructor
public class CriteriaOperationService implements OperationService<Criteria> {

    private final OperationProvider<Criteria> operationProvider;

    @Override
    public Criteria buildBaseByParams(List<BaseSearchParam> baseSearchParams, GlueOperation glue) {
        if (CollectionUtils.isEmpty(baseSearchParams)) {
            return findAll();
        }
        return baseSearchParams
                .stream()
                .map(param -> OperationType.of(param.getOperation()).getOperation(operationProvider).buildOperation(param))
                .reduce(glue::glueCriteriaOperation)
                .orElse(findAll());
    }

    @Override
    public Criteria buildComplexByParams(List<ComplexSearchParam> complexSearchParams, GlueOperation externalGlue) {
        return complexSearchParams.stream()
                .map(complexSearchParam ->
                        buildBaseByParams(
                                complexSearchParam.getBaseSearchParams(),
                                complexSearchParam.getInternalGlue()
                        ))
                .reduce(externalGlue::glueCriteriaOperation)
                .orElse(findAll());
    }
}
