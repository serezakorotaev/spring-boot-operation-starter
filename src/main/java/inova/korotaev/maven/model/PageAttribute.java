package inova.korotaev.maven.model;

import lombok.Data;

@Data
public class PageAttribute {
    private Integer limit;
    private Integer offset;
    private String sortBy;
}
