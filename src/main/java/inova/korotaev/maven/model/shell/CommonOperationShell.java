package inova.korotaev.maven.model.shell;

import inova.korotaev.maven.model.BaseSearchParam;
import inova.korotaev.maven.model.PageAttribute;
import inova.korotaev.maven.model.enums.GlueOperation;
import lombok.Value;

import java.util.List;

import static inova.korotaev.maven.model.enums.GlueOperation.AND;

@Value
public class CommonOperationShell {
    List<BaseSearchParam> baseSearchParams;
    GlueOperation glue = AND;
    PageAttribute pageAttribute;
}
