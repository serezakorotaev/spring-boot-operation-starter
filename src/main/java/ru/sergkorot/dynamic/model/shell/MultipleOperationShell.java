package ru.sergkorot.dynamic.model.shell;

import ru.sergkorot.dynamic.model.ComplexSearchParam;
import ru.sergkorot.dynamic.model.PageAttribute;
import ru.sergkorot.dynamic.model.enums.GlueOperation;
import lombok.Value;

import java.util.List;

import static ru.sergkorot.dynamic.model.enums.GlueOperation.AND;

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
