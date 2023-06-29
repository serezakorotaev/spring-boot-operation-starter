package inova.korotaev.maven.operation;

public interface OperationProvider<R> {
    Operation<R> like();
    Operation<R> eq();
    Operation<R> notEq();
    Operation<R> in();
    Operation<R> notIn();
    Operation<R> isNull();
}
