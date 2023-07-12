package inova.korotaev.maven.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchParamShell {
    private List<SearchParam> searchParams;
    private PageAttribute pageAttribute;
}
