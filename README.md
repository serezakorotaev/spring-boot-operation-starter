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

In library has different operations for searching. User need to select operation in field "operation" and it will be
processed.
All operations are in `OperationType` and interface for their implementation in `OperationProvider<R>`.

### IN

IN operation is used for searching records by specified elements 

    Example:
            {
            "name": "name",
            "value": "John,Max",
            "operation": "in"
            }

In example above, predicate will be built with condition (find all by name in (John,Max))

### NOT_IN

NOT_IN operation is used for searching records without specified elements

    Example:
            {
            "name": "name",
            "value": "John,Max",
            "operation": "notIn"
            }
In example above, predicate will be built with condition (find all by name not in (John,Max))

### LIKE

Like operation is used for searching records where contains specified string

    Example:
            {
            "name": "name",
            "value": "Jo",
            "operation": "like"
            }
In example above, predicate will be built with condition (find all by name like (%Jo%))

### EQUAL

Equal operation is used for searching records by strict match

    Example:
            {
            "name": "varsion",
            "value": 1,
            "operation": "eq"
            }
In example above, predicate will be built with condition (find all where version = 1)

### NOT_EQUAL

Not equal operation is used for searching records where elements haven't specified value

    Example:
            {
            "name": "version",
            "value": 1,
            "operation": "notEq"
            }
In example above, predicate will be built with condition (find all where version != 1)

### IS_NULL

Is null operation is used for searching records where elements haven't null value

    Example:
            {
            "name": "version",
            "operation": "isNull"
            }

In example above, predicate will be built with condition (find all where version is null)

### LESS_THAN

Less than operation is used for searching comparing records where elements less than specified

    Example:
            {
            "name": "age",
            "value": 20,
            "operation": "lt"
            }

In example above, predicate will be built with condition (find all where age < 20)

### GREATER_THAN

Greater than operation is used for searching comparing records where elements greater than specified

    Example:
            {
            "name": "age",
            "value": 20,
            "operation": "gt"
            }

In example above, predicate will be built with condition (find all where age > 20)

### LESS_THAN_OR_EQUALS

Less than operation is used for searching comparing records where elements less than or equal specified

    Example:
            {
            "name": "age",
            "value": 21,
            "operation": "le"
            }

In example above, predicate will be built with condition (find all where age <= 21)

### GREATER_THAN_OR_EQUALS

Less than operation is used for searching comparing records where elements greater than or equal specified

    Example:
            {
            "name": "age",
            "value": 21,
            "operation": "ge"
            }

In example above, predicate will be built with condition (find all where age >= 21)

### CONTAINS

Contains operation is used for searching records where elements contains specified values (operation also is used for jsonb fields)

        {
            "name": "description",
            "value": "a,is",
            "operation": "contains"
        }

In example above, predicate will be built with condition (find all where description contains (a and is strings))



