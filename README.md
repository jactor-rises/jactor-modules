# jactor-persistence  ![](https://github.com/jactor-rises/jactor-persistence/workflows/continious%20integration/badge.svg)
 
### What is this repository for?

The main purpose is to learn about programming microservices (with REST and spring jpa).
I also use github to get a deeper understanding of continuous integration/DevOps using
github actions and docker running integration testing with cucumber and Kotlin.

This project is a microservice dealing with persistence to an database using
jpa (java persistence api) via spring-data-jpa and is a microservice to use under
`com.github.jactor.percistence` (formerly as part of the archived project `jactor-rises`)

### Set up

* a spring-boot 2 application
    * build with [maven](https://maven.apache.org).
    * is using [h2](http://h2database.com) (in-memory database)
    * run it with docker, spring-boot, or as any other java-application
* this application is documented with swagger. After startup, use links:
    * <http://localhost:1099/jactor-persistence/v2/api-docsto> (json documentation)
    * <http://localhost:1099/jactor-persistence/swagger-ui.html> (swagger ui)

### Build

This is application is build with with [maven](https://maven.apache.org).

From the root of the source code:
```
mvn clean install
```
### Run
All commands are being executed from the root of the source code.

Run it with [maven](https://maven.apache.org):
```
mvn spring-boot:run
```

After a valid build one can execute the generated jar file as plain java:
```
java -jar target/jactor-persistence-<version>-SNAPSHOT-app.jar
```
or run an image with maven and spring-boot:
```
mvn spring-boot:run
```
or build and run an image with [docker](https://www.docker.com):
```
docker build -t jactor-persistence .
docker run -p 1099:1099 jactor-persistence
```
### Some technologies used

* [spring-boot 2.2.x](https://spring.io/projects/spring-boot)
    * with spring-data-jpa
* [docker](https://www.docker.com)
* [h2](http://h2database.com)
* [junit 5.x](https://junit.org/junit5/)
* [assertj](https://joel-costigliola.github.io/assertj/)
* [mockito](http://site.mockito.org)
* [kotlin](https://kotlinlang.org)
* [swagger-ui](https://swagger.io/tools/swagger-ui/)
