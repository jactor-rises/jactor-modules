# jactor-shared
[![build and deploy maven artifact](https://github.com/jactor-rises/jactor-shared/actions/workflows/build.yaml/badge.svg)](https://github.com/jactor-rises/jactor-shared/actions/workflows/build.yaml)

Shared beans between [jactor-persistence](https://github.com/jactor-rises/jactor-persistencehttps://github.com/jactor-rises/jactor-web) and
[jactor-web]() (mostly http api)

## Http api

### Dto
Data transfer objects for the data model of jactor-persistence

### Commands
Command objects used for manipulation the data model of jactor-persistence

## Changelog
| version | type of change | description                                                            |
|---------|----------------|------------------------------------------------------------------------|
| 0.3.5   | test of action | No difference, but test of github workflows with tagging and no commit |
| 0.3.4   | changed        | Main branch is protected in workflow and bump springdoc-openapi-ui     |
| 0.3.3   | changed        | `Person.locale` (oas documentation)                                    |
| 0.3.2   | changed        | deploy with new workflow                                               |
| 0.3.1   | changed        | spring-boot, kotlin, and springdoc-openapi-ui compile dependencies     |
| 0.3.0   | changed        | jdk 16->17, kotlin, and spring-boot                                    |
| 0.2.0   | changed        | springfox-swagger -> springdoc-openapi-ui, kotlin, and spring-boot     |
| 0.1.3   | changed        | kotlin and spring-boot version                                         |
| 0.1.2   | changed        | kotlin and java version                                                |
| 0.1.1   | changed        | `CreateUserCommandDto`: added swagger documentation                    |
| 0.1.0   | created        | `CreateUserCommandDto`: used to create a user on `jactor-persistence`  |
| 0.0.1   | created        | `UserDto`, `PersonDto`, `AddressDto`: Main dto's of the data model     |
