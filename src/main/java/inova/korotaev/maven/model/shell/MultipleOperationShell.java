package inova.korotaev.maven.model.shell;

import inova.korotaev.maven.model.ComplexSearchParam;
import inova.korotaev.maven.model.PageAttribute;
import inova.korotaev.maven.model.enums.GlueOperation;
import lombok.Value;

import java.util.List;

import static inova.korotaev.maven.model.enums.GlueOperation.AND;

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
