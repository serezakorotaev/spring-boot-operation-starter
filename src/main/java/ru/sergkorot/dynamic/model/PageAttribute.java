package ru.sergkorot.dynamic.model;

import lombok.Data;

/**
 * @author Sergey Korotaev
 * Class for paging and sorting settings
 */
@Data
public class PageAttribute {
    private Integer limit;
    private Integer offset;
    private String sortBy;
}
