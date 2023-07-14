package inova.korotaev.maven.model;

import inova.korotaev.maven.model.enums.GlueOperation;
import lombok.Data;

import java.util.List;

import static inova.korotaev.maven.model.enums.GlueOperation.AND;

@Data
public class MultipleOperationSearchParamShell {
    List<ComplexSearchParam> search;
    GlueOperation externalGlue = AND;
    PageAttribute pageAttribute;
}
