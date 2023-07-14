package inova.korotaev.maven.model;

import lombok.Data;

@Data
public class BaseSearchParam {
    private String name;
    private Object value;
    private String operation;
}
