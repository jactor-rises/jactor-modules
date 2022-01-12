# jactor-persistence

[![continuous integration](https://github.com/jactor-rises/jactor-persistence/actions/workflows/ci.yaml/badge.svg)](https://github.com/jactor-rises/jactor-persistence/actions/workflows/ci.yaml)
[![verify pull request](https://github.com/jactor-rises/jactor-persistence/actions/workflows/pr.yaml/badge.svg)](https://github.com/jactor-rises/jactor-persistence/actions/workflows/pr.yaml)
[![build run docker](https://github.com/jactor-rises/jactor-persistence/actions/workflows/docker-action.yaml/badge.svg)](https://github.com/jactor-rises/jactor-persistence/actions/workflows/docker-action.yaml)

### What is this repository for?

The main purpose is to learn about programming microservices using Kotlin, (with REST and spring JPA). I also use GitHub to get a deeper understanding
of continuous integration/DevOps using GitHub workflow/actions and docker running integration testing with cucumber and Kotlin.

This project is a microservice dealing with persistence to a database using JPA (java persistence api) via spring-data-jpa and is a microservice to
use under `com.github.jactor.percistence` (formerly as part of the archived project [jactor-rises](https://github.com/jactor-rises/jactor-rises))

### Set up

* a spring-boot 2 application
    * build with [gradle](https://gradle.org).
    * is using [h2](http://h2database.com) (in-memory database)
    * run it with docker, spring-boot, or as any other java-application
* this application is documented with swagger. After startup, use link:
    * <http://localhost:1099/jactor-persistence/swagger-ui/index.html?configUrl=/jactor-persistence/v3/api-docs/swagger-config#/> (swagger ui)

### Build

This is application is build with [gradle](https://gradle.org).

From the root of the source code:
```
./gradlew build
```
### Run
All commands are being executed from the root of the source code.

Run it with [gradle](https://gradle.org):
```
./gradlew bootRun
```
After a valid build one can execute the generated jar file as plain java:
```
java -jar build/lib/jactor-persistence-<version>-SNAPSHOT.jar
```
or build and run an image with [docker](https://www.docker.com):
```
docker build -t jactor-persistence .
docker run -p 1099:1099 jactor-persistence
```
### Some technologies used

* [spring-boot 2.6.x](https://spring.io/projects/spring-boot)
    * with [spring-data-jpa](https://spring.io/projects/spring-data-jpa)
    * with [spring-webflow](https://spring.io/projects/spring-webflow) (mvc)
* [docker](https://www.docker.com)
* [h2](http://h2database.com)
* [flyway](https://flywaydb.org)
* [junit jupiter](https://junit.org/)
* [assertj](https://assertj.github.io/doc/)
* [mockito](http://site.mockito.org)
* [kotlin](https://kotlinlang.org)
* [swagger-ui](https://swagger.io/tools/swagger-ui/)
  * herunder [springdoc](https://springdoc.org)
* [github actions](https://docs.github.com/en/actions/learn-github-actions)
