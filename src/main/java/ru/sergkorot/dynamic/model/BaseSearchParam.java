package ru.sergkorot.dynamic.model;

import lombok.Data;

/**
 * @author Sergey Korotaev
 * Base class for search requests
 */
@Data
public class BaseSearchParam {
    private String name;
    private Object value;
    private String operation;
}
