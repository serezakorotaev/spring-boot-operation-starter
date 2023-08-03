package ru.korotaev.dynamic.model.shell;

import ru.korotaev.dynamic.model.BaseSearchParam;
import ru.korotaev.dynamic.model.PageAttribute;
import ru.korotaev.dynamic.model.enums.GlueOperation;
import lombok.Value;

import java.util.List;

import static ru.korotaev.dynamic.model.enums.GlueOperation.AND;

/**
 * @author Sergey Korotaev
 * Shell for base search requests
 */
@Value
public class CommonOperationShell {
    List<BaseSearchParam> baseSearchParams;
    GlueOperation glue = AND;
    PageAttribute pageAttribute;
}
