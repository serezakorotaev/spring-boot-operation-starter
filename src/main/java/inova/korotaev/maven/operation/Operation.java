package inova.korotaev.maven.operation;

import inova.korotaev.maven.model.SearchParam;

public interface Operation<R> {
    R buildOperation(SearchParam searchParam);
}
