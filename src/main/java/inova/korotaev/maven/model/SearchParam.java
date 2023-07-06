package inova.korotaev.maven.model;

import inova.korotaev.maven.model.enums.GlueOperation;
import lombok.Data;

@Data
public class SearchParam {
    private String name;
    private Object value;
    private String operation;
    private GlueOperation glue = GlueOperation.OR;
}
