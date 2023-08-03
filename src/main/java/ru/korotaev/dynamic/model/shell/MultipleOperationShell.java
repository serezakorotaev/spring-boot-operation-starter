package ru.korotaev.dynamic.model.shell;

import ru.korotaev.dynamic.model.ComplexSearchParam;
import ru.korotaev.dynamic.model.PageAttribute;
import ru.korotaev.dynamic.model.enums.GlueOperation;
import lombok.Value;

import java.util.List;

import static ru.korotaev.dynamic.model.enums.GlueOperation.AND;

/**
 * @author Sergey Korotaev
 * Shell for complex search requests
 */
@Value
public class MultipleOperationShell {
    List<ComplexSearchParam> search;
    GlueOperation externalGlue = AND;
    PageAttribute pageAttribute;
}
