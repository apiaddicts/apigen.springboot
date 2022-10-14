# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [unreleased]
### Added
- Support for nullable in OpenAPI file interpretation
### Changed
- Enhance the code generation mechanism to allow to extend and replace by custom code generation strategies
- Generated non paginated controller endpoints for list and search if the OpenAPI file specifies it
- Allow to customize the standard api response
- Allow to customize the field implementation type for properties
- Honor default OpenAPI object type in file interpretation
- Allow independence between basePackage and artifactId in OpenAPI extension
- Updated spring boot version to `2.7.4`
- Updated `mapstruct` version to `1.5.3.Final`
- Updated `springdoc` version to `1.6.11`
- Updated `swagger-parser` version to `2.1.3`
- Updated `maven-model` version to `3.8.6`
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

[unreleased]: https://github.com/apiaddicts/apigen/releases/tag/v0.2.1...HEAD
[0.2.1]: https://github.com/apiaddicts/apigen/releases/tag/v0.2.1
[0.2.0]: https://github.com/apiaddicts/apigen/releases/tag/v0.2.0
[0.1.1]: https://github.com/apiaddicts/apigen/releases/tag/v0.1.1
[0.1.0]: https://github.com/apiaddicts/apigen/releases/tag/v0.1.0
