# spring-boot-operation-starter

### Library for dynamic search into the database

## 1. About Library

Library helps to developers to build more flexible requests and page settings.
Using a few library's methods you will have opportunity to build simple and complex requests,
including conjunction and disjunction with different operations such as *equals*, *like*, *lessThan*,
*in* and others.

## 2. OperationService

This class is main class in lib for using it. For using you should define the entity for which
will use this class

`OperationService<Entity> operationService`

after that, you can build queries for this entity.

`OperationService` has methods for creating simple and complex requests into the database 
and also for creating page settings.

 - a. `Specification<T> buildBaseSpecificationByParams(List<BaseSearchParam> baseSearchParams, GlueOperation glue)`

Method for building base request using search parameters (baseSearchParams) 
and condition for linking parameters (glue). `BaseSearchParam` and `GlueOperation` classes will be described
below.

 - b. `Specification<T> buildComplexSpecificationByParams(List<ComplexSearchParam> complexSearchParams, GlueOperation externalGlue)`

Method for building complex request using structure with base search parameters (complexSearchParams)
and condition for linking base parameters with each other (externalGlue). `ComplexSearchParam` class will be described
below.

 - c. `PageRequestWithOffset buildPageSettings(PageAttribute pageAttribute, List<String> searchSortFields)`

Method for building page settings (limit, offset and sorting) using class with page parameters (pageAttribute)
and list with fields for which will be applied sorting (searchSortFields). `PageAttribute` class will be described
below.

## 3. Supported operations