# Migration guide

All changes required to migrate generated Apigen projects to new versions will be documented in this file.

## From [2.1.1] to [2.2.0]

### Spring Boot 4.1.0 → 4.1.1

Patch release, no changes required. See the [Spring Boot 4.1.1 release notes](https://github.com/spring-projects/spring-boot/releases/tag/v4.1.1).

### Jackson 3 (complete)

This version removes the last Jackson 2 usage from `archetype-core`. The JSON Patch library used by the standard response transformation (`apigen.standard-response.operations`) has moved to new Maven coordinates with Jackson 3 support:

| Before | After |
|---|---|
| `com.flipkart.zjsonpatch:zjsonpatch:0.4.16` | `io.github.vishwakarma:zjsonpatch:0.6.3` |

**Impact on generated projects**:

- If your project's `pom.xml` declares `com.flipkart.zjsonpatch:zjsonpatch` directly, change it to `io.github.vishwakarma:zjsonpatch`. The Java package (`com.flipkart.zjsonpatch`) is unchanged, but with Jackson 3 nodes (`tools.jackson.databind.JsonNode`) use `Jackson3JsonPatch` / `Jackson3JsonDiff` instead of `JsonPatch` / `JsonDiff`.
- Since `0.6.0`, zjsonpatch declares its Jackson dependencies as `optional`. Jackson 3 is provided by `spring-boot-starter-web`; if your project relied on archetype-core bringing `com.fasterxml.jackson.core:jackson-databind` 2.x transitively for its own code, migrate that code to `tools.jackson.*` (see [2.1.0](#from-203-to-210)) or declare the Jackson 2 dependency explicitly.
- `JsonNode.asText()` is deprecated in Jackson 3; use `JsonNode.asString()` instead.
- Jackson 2 still appears transitively through `springdoc` (`swagger-core`), which has not released a Jackson 3 version yet. No action is needed.

### springdoc 3.0.x → 3.1.1

- The springdoc MCP integration is now opt-in (`springdoc.ai.mcp.enabled=true`). Only relevant if you were using it.

## From [2.1.0] to [2.1.1]

No migration required for generated projects.

## From [2.0.3] to [2.1.0]

### Spring Boot 4.0.x → 4.1.0

Perform the [Spring Boot 4.1 migration](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.1-Release-Notes). Key points:

- If your project sets `spring.data.jpa.repositories.bootstrap-mode=deferred`, you must now provide an `AsyncTaskExecutor` bean; otherwise startup will fail.
- The deprecated `layertools` jar mode has been removed. If your `Dockerfile` uses `java -Djarmode=layertools`, replace it with `java -Djarmode=tools`.

### Jackson 3

Spring Boot 4.1 uses Jackson 3 by default. This version completes the Jackson 3 migration in `archetype-core`, `generator-core`, and `generator-cli`.

**Impact on generated projects** (using `archetype-parent-spring-boot`):

- The `spring-boot-jackson2` dependency and all related exclusions have been removed from `archetype-parent-spring-boot`. If your project's `pom.xml` still pins `spring-boot-jackson2` or `jackson.version=2.x`, remove those entries.
- Jackson 3 keeps the annotation package unchanged: `@JsonProperty`, `@JsonIgnore`, `@JsonCreator`, etc. still come from `com.fasterxml.jackson.annotation.*` — **no changes needed** to classes that use these annotations.
- Only the processing infrastructure moved: if your project directly uses `com.fasterxml.jackson.databind.*` or `com.fasterxml.jackson.core.*` APIs (e.g. `ObjectMapper`, `JsonNode`, `ArrayNode`), rename those imports to `tools.jackson.databind.*` / `tools.jackson.core.*`.
- Spring Boot property prefix for Jackson configuration changes from `spring.jackson2.*` to `spring.jackson.*` in `application.properties`.

## From [2.0.2] to [2.0.3]

No migration required

## From [2.0.1] to [2.0.2]

No migration required

## From [2.0.0] to [2.0.1]

No migration required

## From [1.2.5] to [2.0.0]

- Perform the Spring Boot migration from `3.5.x` to `4.0.x`

## From [1.2.4] to [1.2.5]

No migration required

## From [1.2.3] to [1.2.4]

No migration required

## From [1.2.2] to [1.2.3]

No migration required

## From [1.2.1] to [1.2.2]

No migration required

## From [1.2.0] to [1.2.1]

No migration required

## From [1.1.3] to [1.2.0]

- Perform the Spring Boot migration from `3.4.x` to `3.5.x`

## From [1.1.2] to [1.1.3]

No migration required

## From [1.1.1] to [1.1.2]

No migration required

## From [1.1.0] to [1.1.1]

No migration required

## From [1.0.1] to [1.1.0]

Update the management endpoints properties

Old:
```properties
management.endpoints.enabled-by-default=false
management.endpoint.<id>.enabled=true
```

New:
```properties
management.endpoints.access.default=none
management.endpoint.<id>.access=read-only
```

Remove the deprecated `spring.mvc.throw-exception-if-no-handler-found` property

## From [1.0.0] to [1.0.1]

No migration required

## From [0.6.2] to [1.0.0]

Update to Java 21

## From [0.6.1] to [0.6.2]

No migration required

## From [0.6.0] to [0.6.1]

No migration required

## From [0.5.0] to [0.6.0]

This version requires to replace the hibernate uuid generation.

Old:
```java
    @Id
    @GeneratedValue(
            generator = "uuid"
    )
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    @Column(
            name = "id"
    )
    private String id;
```

New:
```java
    @Id
    @UuidGenerator
    @Column(
            name = "id"
    )
    private String id;
```

## From [0.4.1] to [0.5.0]

No migration required

## From [0.4.0] to [0.4.1]

No migration required

## From [0.3.0] to [0.4.0]

In this version Apigen has been updated to use Spring Boot 3, so now you need a java version >= `17`

Support for PATCH endpoints has been added and the PUT endpoint behaviour has changed. 
To maintain the same functionality as in the previous versions you need to use
`AbstractRelationsLegacyManager` and `AbstractCrudLegacyService` instead of `AbstractRelationsManager` and `AbstractCrudService`

## From [0.2.0] to [0.3.0]

No migration required

## From [0.1.0] to [0.2.0]

In this version Apigen has been updated to be auto documented with `spring-doc` and all the dependencies have been updated.

- Replace the autogenerated documentation annotations in controllers from `io.swagger.annotations.Api` to `io.swagger.v3.oas.annotations.tags.Tag`
- Perform the Spring Boot migration from `2.4.x` to `2.6.x`
- Remove the property `apigen.documentation.enabled`, now the documentation is managed by the `spring-doc` official properties

[unreleased]: https://github.com/apiaddicts/apigen/releases/tag/2.2.0...HEAD
[2.2.0]: https://github.com/apiaddicts/apigen/releases/tag/2.2.0
[2.1.1]: https://github.com/apiaddicts/apigen/releases/tag/2.1.1
[2.1.0]: https://github.com/apiaddicts/apigen/releases/tag/2.1.0
[2.0.3]: https://github.com/apiaddicts/apigen/releases/tag/2.0.3
[2.0.2]: https://github.com/apiaddicts/apigen/releases/tag/2.0.2
[2.0.1]: https://github.com/apiaddicts/apigen/releases/tag/2.0.1
[2.0.0]: https://github.com/apiaddicts/apigen/releases/tag/2.0.0
[1.2.5]: https://github.com/apiaddicts/apigen/releases/tag/1.2.5
[1.2.4]: https://github.com/apiaddicts/apigen/releases/tag/1.2.4
[1.2.3]: https://github.com/apiaddicts/apigen/releases/tag/1.2.3
[1.2.2]: https://github.com/apiaddicts/apigen/releases/tag/1.2.2
[1.2.1]: https://github.com/apiaddicts/apigen/releases/tag/1.2.1
[1.2.0]: https://github.com/apiaddicts/apigen/releases/tag/1.2.0
[1.1.3]: https://github.com/apiaddicts/apigen/releases/tag/1.1.3
[1.1.2]: https://github.com/apiaddicts/apigen/releases/tag/1.1.2
[1.1.1]: https://github.com/apiaddicts/apigen/releases/tag/1.1.1
[1.1.0]: https://github.com/apiaddicts/apigen/releases/tag/1.1.0
[1.0.1]: https://github.com/apiaddicts/apigen/releases/tag/1.0.1
[1.0.0]: https://github.com/apiaddicts/apigen/releases/tag/1.0.0
[0.6.2]: https://github.com/apiaddicts/apigen/releases/tag/0.6.2
[0.6.1]: https://github.com/apiaddicts/apigen/releases/tag/0.6.1
[0.6.0]: https://github.com/apiaddicts/apigen/releases/tag/0.6.0
[0.5.0]: https://github.com/apiaddicts/apigen/releases/tag/0.5.0
[0.4.1]: https://github.com/apiaddicts/apigen/releases/tag/v0.4.1
[0.4.0]: https://github.com/apiaddicts/apigen/releases/tag/v0.4.0
[0.3.0]: https://github.com/apiaddicts/apigen/releases/tag/v0.3.0
[0.2.1]: https://github.com/apiaddicts/apigen/releases/tag/v0.2.1
[0.2.0]: https://github.com/apiaddicts/apigen/releases/tag/v0.2.0
[0.1.1]: https://github.com/apiaddicts/apigen/releases/tag/v0.1.1
[0.1.0]: https://github.com/apiaddicts/apigen/releases/tag/v0.1.0
