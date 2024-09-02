# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [unreleased]

## [0.6.2] - 2024-09-02

### Changed
- Updated spring boot version to `3.3.3`
 
## [0.6.1] - 2024-08-01

### Changed
- Fix traceId propagation to response when its origin is not the request header
- Updated spring boot version to `3.3.2`

## [0.6.0] - 2024-07-16

### Changed
- Updated spring boot version to `3.3.1`
- Updated `jsonpatch` version to `0.4.16`
- Updated `springdoc` version to `2.6.0`
- Updated `swagger-parser` version to `2.1.22`
- Updated `maven-model` version to `3.9.8`
- Updated `commons-io` version to `2.16.1`
- Updated the id annotation field generation strategy when is uuid

## [0.5.0] - 2024-01-16

### Changed
- Updated spring boot version to `3.2.1`
- Updated database metadata detection for custom Apigen functions

## [0.4.1] - 2023-11-22

### Changed
- Updated spring boot version to `3.0.12`

## [0.4.0] - 2023-03-29

### Added
- Filter developer friendly builders
- Partial support to polymorphic requests
- Support for null filters

### Changed
- Fix problem with parent-child controller generation
- Updated spring boot version to `3.0.1`
- Updated `springdoc` version to `2.0.2`

## [0.3.0] - 2023-01-09

### Added
- Support for nullable in OpenAPI file interpretation
- Partial support for non json (`application/json`) mime types
- Support for parent-child entity endpoints generation
- Support for PATCH endpoints
### Changed
- Enhance the code generation mechanism to allow to extend and replace by custom code generation strategies
- Generated non paginated controller endpoints for list and search if the OpenAPI file specifies it
- Allow to customize the standard api response
- Allow to customize the field implementation type for properties
- Honor default OpenAPI object type in file interpretation
- Allow independence between basePackage and artifactId in OpenAPI extension
- Updated spring boot version to `2.7.7`
- Updated `mapstruct` version to `1.5.3.Final`
- Updated `springdoc` version to `1.6.14`
- Updated `swagger-parser` version to `2.1.10`
- Updated `maven-model` version to `3.8.7`
- Separate persistence related classes from the archetype core to another module and add it as a dependency
### Removed
- Remove dependency on `reflections` library

## [0.2.1] - 2022-06-13
### Changed
- Downgraded `reflections` version
- Updated jpa entity relations to always be lazy by default
- Fix delete endpoint generation to be able to generate no content responses 

## [0.2.0] - 2022-04-18
### Changed
- Updated spring boot version to `2.6.6`
- Update other dependencies
- Replace documentation library `springfox` by `spring-doc`
### Removed
- Configuration property `apigen.documentation.enabled`

## [0.1.1] - 2021-11-11
### Fixed
- Solve compilation error with `ApigenMapper` generated classes

## [0.1.0] - 2021-11-11
### Added
- Initial functional version of Apigen

[unreleased]: https://github.com/apiaddicts/apigen/releases/tag/0.6.2...HEAD
[0.6.1]: https://github.com/apiaddicts/apigen/releases/tag/0.6.2
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
