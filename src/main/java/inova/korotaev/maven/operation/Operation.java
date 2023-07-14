package inova.korotaev.maven.operation;

import inova.korotaev.maven.model.BaseSearchParam;

public interface Operation<R> {
    R buildOperation(BaseSearchParam baseSearchParam);
}
