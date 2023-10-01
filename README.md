# spring-boot-operation-starter

### Library for dynamic search into the database
From maven central <br>
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ru.sergkorot/spring-boot-operation-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/ru.sergkorot.dynamic/spring-boot-operation-starter)


# Content list
 
 [About library](#1-about-library)


 [OperationService](#2-operationservice)
 - [SpecificationOperationService](#21-specificationoperationservice)
 - [CriteriaOperationService](#22-criteriaoperationservice)

 [Supported operations](#3-supported-operations)
- [IN](#in)
- [NOT IN](#notin)
- [LIKE](#like)
- [EQUAL](#equal)
- [NOT EQUAL](#notequal)
- [IS NULL](#isnull)
- [LESS THAN](#lessthan)
- [GREATER THAN](#greaterthan)
- [LESS THAN OR EQUALS](#lessthanorequals)
- [GREATER THAN OR EQUALS](#greaterthanorequals)
- [CONTAINS](#contains)

 [Models for searching and paging](#4-models-for-searching-and-paging)
- [BaseSearchParam](#basesearchparam)
- [ComplexSearchParam](#complexsearchparam)
- [PageAttribute](#pageattribute)

 [Request examples](#5-request-examples)

 [Other useful classes](#6-other-useful-classes)
- [Utils](#utils)
- [RegexpUtils](#regexputils)
- [SortUtils](#sortutils)
- [SpecificationUtils](#specificationutils)
- [PageRequestWithOffset](#pagerequestwithoffset)

## 1. [About Library](#content-list)

Library helps to developers to build more flexible requests and page settings.
Using a few library's methods you will have opportunity to build simple and complex requests,
including conjunction and disjunction with different operations such as *equals*, *like*, *lessThan*,
*in* and others.

```xml
<dependency>
    <groupId>ru.sergkorot.dynamic</groupId>
    <artifactId>spring-boot-operation-starter</artifactId>
    <version>x.x.x</version>
</dependency>
```

```gradle
implementation 'ru.sergkorot.dynamic:spring-boot-operation-starter:x.x.x'
```


## 2. [OperationService](#content-list)

This is an interface for building requests into the databases with different parameters and glue option.
At the moment are existed two implementation of this interface: `SpecificationOperationService`
and `CriteriaOperationService`

### 2.1 [SpecificationOperationService](#content-list)

This class is main class in lib for build request into the relation databases. For using you should define the entity
for which will use this class

`SpecificationOperationService<Entity> operationService`

after that, you can build queries for this entity.

`SpecificationOperationService` implements methods for creating simple and complex requests into the database
and also has method for creating page settings.

- a. `Specification<T> buildBaseByParams(List<BaseSearchParam> baseSearchParams, GlueOperation glue)`

Method for building base request using search parameters (baseSearchParams)
and condition for linking parameters (glue). `BaseSearchParam` and `GlueOperation` classes will be described
below.

- b. `Specification<T> buildComplexByParams(List<ComplexSearchParam> complexSearchParams, GlueOperation externalGlue)`

Method for building complex request using structure with base search parameters (complexSearchParams)
and condition for linking base parameters with each other (externalGlue). `ComplexSearchParam` class will be described
below.

- c. `PageRequestWithOffset buildPageSettings(PageAttribute pageAttribute, List<String> searchSortFields)`

Method for building page settings (limit, offset and sorting) using class with page parameters (pageAttribute)
and list with fields for which will be applied sorting (searchSortFields). `PageAttribute` class will be described
below.

### 2.2 [CriteriaOperationService](#content-list)

This class is main class in lib for build request into the MongoDb. For using you do not need to define the entity
for which will use this class kind of with `SpecificationOperationService`

`CriteriaOperationService criteriaOperationService`

After that, you can build Query for searching.

`CriteriaOperationService` implements methods for creating simple and complex requests into the database
and also has method for creating page settings.

- a. `Criteria buildBaseByParams(List<BaseSearchParam> baseSearchParams, GlueOperation glue)`

Method for building base request using search parameters (baseSearchParams)
and condition for linking parameters (glue). `BaseSearchParam` and `GlueOperation` classes will be described
below.

- b. `Criteria buildComplexByParams(List<ComplexSearchParam> complexSearchParams, GlueOperation externalGlue)`

Method for building complex request using structure with base search parameters (complexSearchParams)
and condition for linking base parameters with each other (externalGlue). `ComplexSearchParam` class will be described
below.

- c. `Query buildPageSettings(Query query, PageAttribute pageAttribute, List<String> searchSortFields)`

Method for building query with paging settings (limit, offset and sorting) using class with page parameters (pageAttribute)
and list with fields for which will be applied sorting (searchSortFields). `PageAttribute` class will be described
below. Recommended use this method for creating query with already defining criteria settings because if you want to add criteria after
then you will spend more time under the box (internal method addCriteria). For example,

 `operationService.buildPageSettings(new Query(criteria), shell.getPageAttribute(), SORTED_FIELDS);`


## 3. [Supported operations](#content-list)

In library has different operations for searching. User need to select operation in field "operation" and it will be
processed.
All operations are in `OperationType` and interface for their implementation in `OperationProvider<R>`.

### [IN](#content-list)

IN operation is used for searching records by specified elements

    Example:
            {
            "name": "name",
            "value": "John,Max",
            "operation": "in"
            }

In example above, predicate will be built with condition (find all by name in (John,Max))

### [NOT_IN](#content-list)

NOT_IN operation is used for searching records without specified elements

    Example:
            {
            "name": "name",
            "value": "John,Max",
            "operation": "notIn"
            }

In example above, predicate will be built with condition (find all by name not in (John,Max))

### [LIKE](#content-list)

Like operation is used for searching records where contains specified string

    Example:
            {
            "name": "name",
            "value": "Jo",
            "operation": "like"
            }

In example above, predicate will be built with condition (find all by name like (%Jo%))

### [EQUAL](#content-list)

Equal operation is used for searching records by strict match

    Example:
            {
            "name": "varsion",
            "value": 1,
            "operation": "eq"
            }

In example above, predicate will be built with condition (find all where version = 1)

### [NOT_EQUAL](#content-list)

Not equal operation is used for searching records where elements haven't specified value

    Example:
            {
            "name": "version",
            "value": 1,
            "operation": "notEq"
            }

In example above, predicate will be built with condition (find all where version != 1)

### [IS_NULL](#content-list)

Is null operation is used for searching records where elements haven't null value

    Example:
            {
            "name": "version",
            "operation": "isNull"
            }

In example above, predicate will be built with condition (find all where version is null)

### [LESS_THAN](#content-list)

Less than operation is used for searching comparing records where elements less than specified

    Example:
            {
            "name": "age",
            "value": 20,
            "operation": "lt"
            }

In example above, predicate will be built with condition (find all where age < 20)

### [GREATER_THAN](#content-list)

Greater than operation is used for searching comparing records where elements greater than specified

    Example:
            {
            "name": "age",
            "value": 20,
            "operation": "gt"
            }

In example above, predicate will be built with condition (find all where age > 20)

### [LESS_THAN_OR_EQUALS](#content-list)

Less than operation is used for searching comparing records where elements less than or equal specified

    Example:
            {
            "name": "age",
            "value": 21,
            "operation": "le"
            }

In example above, predicate will be built with condition (find all where age <= 21)

### [GREATER_THAN_OR_EQUALS](#content-list)

Less than operation is used for searching comparing records where elements greater than or equal specified

    Example:
            {
            "name": "age",
            "value": 21,
            "operation": "ge"
            }

In example above, predicate will be built with condition (find all where age >= 21)

### [CONTAINS](#content-list)

Contains operation is used for searching records where elements contains specified values (operation also is used for
jsonb fields)

    Example:
          {
          "name": "description",
          "value": "a,is",
          "operation": "contains"
          }

In example above, predicate will be built with condition (find all where description contains (a and is strings))

## 4. [Models for searching and paging](#content-list)

For searching and paging are three base models - `BaseSearchParam`, `ComplexSearchParam` and `PageAttribute`.
And Also shell for them are `CommonOperationShell` and `MultipleOperationShell`.

### [`BaseSearchParam`](#content-list)

  ```
  class BaseSearchParam {
    private String name;
    private Object value;
    private String operation;
  }
  ```

Is base class for searching. it has

- `name` - field's name which need to search
- `value` - value which need to find
- `operation` - operation name which describe above

```
    Example:
        {
            "name": "name",
            "value": "Ian.Hessel",
            "operation": "eq"
        }
```

### [`ComplexSearchParam`](#content-list)

```
  class ComplexSearchParam {
      List<BaseSearchParam> baseSearchParams;
      GlueOperation internalGlue = AND;
  }
```

is more complex class for searching. It has

- list `baseSearchParams` which has fields and operations for searching
- `internalGlue` which allows you to glue all given conditions. AND/OR value

```
    Example:
    {
      "baseSearchParams": [
          {
              "name": "name",
              "value": "Ian.Hessel",
              "operation": "eq"
          },
          {
              "name": "description",
              "value": "withdrawal",
              "operation": "eq"
          }
      ],
      "glue" : "OR"
    }
```

### [`PageAttribute`](#content-list)

  ```
  class PageAttribute {
      private Integer limit;
      private Integer offset;
      private String sortBy;
  }
  ```

Is used for building page settings for paging

- `limit` Number of list items to return
- `offset` Shift relative to the beginning of the list
- `sortBy` Parameter for sorting. Perhaps multiple sorting through comma (name,-surname),
  which means name ASC and surname DESC

## 5. [Request examples](#content-list)

base search:

  ```
  {
    "baseSearchParams": [
        {
            "name": "name",
            "value": "Ian.Hessel",
            "operation": "eq"
        },
        {
            "name": "description",
            "value": "withdrawal",
            "operation": "eq"
        }
    ],
    "glue" : "OR",
    "pageAttribute" : {
        "limit" : 10,
        "offset" : 0,
        "sortBy" : "name"
    }
  }
  ```

In example above, is used `CommonOperationShell` for simple request

  ```
(
Find all where name.equals("Ian.Hessel") or description.equals("withdrawal")
with limit=10 and offset=0 and sort by name ASC
)
  ```

complex search:

  ```
  {
    "search": [
        {
            "baseSearchParams": [
                {
                    "name": "name",
                    "value": "Rhett14",
                    "operation": "eq"
                },
                {
                    "name": "version",
                    "value": "0,2",
                    "operation": "in"
                }
            ],
            "internalGlue": "AND"
        },
        {
            "baseSearchParams": [
                {
                    "name": "name",
                    "value": "Reggie19",
                    "operation": "eq"
                },
                {
                    "name": "description",
                    "value": "Up-sized",
                    "operation": "like"
                }
            ],
            "internalGlue": "AND"
        }
    ],
    "externalGlue": "OR",
    "pageAttribute": {
        "limit": 10
    }
  }
  ```

In example above, is used `MultipleOperationShell` for complex request

  ```
(
find all where (name.equals("Rhett14") and version in(0, 2)) or (name.equals("Reggie19") and description like ("%Up-sized%"))
)
  ```

## 6. [Other useful classes](#content-list)

### [Utils](#content-list)

#### [`RegexpUtils`](#content-list)
 - `RegexpUtils.transformToArrayFieldsNames(String fieldsNames)`

Util is used for transforming string by pattern to list strings with strings for further paging

  ```
  Example:
  name,-surname,version -> ["name", "-surname", "version]
  
  ```

#### [`SortUtils`](#content-list)

- `SortUtils.makeSortOrders(final Collection<String> validNames, final String sortValues)`

Util is used for transforming string by pattern inside (uses RegexpUtils) to list
org.springframework.data.domain.Sort.Order class
and checking by validNames if it can build Sort.Order by these sortValue names

- `SortUtils.makeSort(final Collection<String> validNames, final String sortValues)`

Util is used for transforming string by pattern inside (uses RegexpUtils) to list
org.springframework.data.domain.Sort class
and checking by validNames if it can build Sort.Order by these sortValue names

  ```
  Example:
  validNames : name,-surname
  sortValues: name,-surname,version -> Not found parameter with name: version
  
  ```

#### [`SpecificationUtils`](#content-list)

Util is used for building different specifications for request. Contains a lot of static methods:

- `findByColumnEquals(Object value, String columnName)`
- `findByColumnNotEquals(Object value, String columnName)`
- `findByColumnsLike(String value, Collection<String> columnName)`
- `lessThan(Y value, String columnName)`
- `greaterThan(Y value, String columnName)`
- `lessThanOrEqual(Y value, String columnName)`
- `greaterThanOrEqual(Y value, String columnName)`
- `findByCollectionIn(Collection<?> collection, String columnName)`
- `findByCollectionNotIn(Collection<?> collection, String columnName)`
- `findByColumnIsNull(String columnName)`
- `findNothing()`
- `findAll()`
- `contains(Object value, String columnName)`

More detail in javadoc

#### [`PageRequestWithOffset`](#content-list)

PageRequest extension for building page settings

  ```
  Example:
  PageRequest request = PageRequestWithOffset.of(offset: 0, size: 10, List<Sort.Order>: List.of(Order.by(name).asc()))
  
  ```