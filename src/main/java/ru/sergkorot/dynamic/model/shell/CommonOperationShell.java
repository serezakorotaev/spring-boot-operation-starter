package ru.sergkorot.dynamic.model.shell;

import ru.sergkorot.dynamic.model.BaseSearchParam;
import ru.sergkorot.dynamic.model.PageAttribute;
import ru.sergkorot.dynamic.model.enums.GlueOperation;
import lombok.Value;

import java.util.List;

import static ru.sergkorot.dynamic.model.enums.GlueOperation.AND;

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
