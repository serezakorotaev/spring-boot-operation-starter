package inova.korotaev.maven.model;

import inova.korotaev.maven.model.enums.GlueOperation;
import lombok.Data;

import java.util.List;

import static inova.korotaev.maven.model.enums.GlueOperation.AND;

/**
 * @author Sergey Korotaev
 * Class for complex search params
 */
@Data
public class ComplexSearchParam {
    List<BaseSearchParam> baseSearchParams;
    GlueOperation internalGlue = AND;
}
