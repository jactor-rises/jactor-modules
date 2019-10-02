# jactor-persistence #

### What is this repository for? ###

The main purpose is to learn about programming microservices (with REST and spring jpa).
I also use github to get a deeper understanding of ci (github actions) and docker

This project is a microservice dealing with persistence to an database using
jpa (java persistence api) via spring-data-jpa and is a microservice to use under
`com.github.jactor-rises` (formerly as part of the archived project `jactor-rises`)

### Set up ###

* a spring-boot 2 application is created when building (`mvn install`)
    * is using h2 (in-memory database)
    * run it with `mvn spring-boot:run` or run
     `com.github.jactor.persistence.JactorPersistence` as any other java-application
* this application is documented with swagger. After startup, use links:
    * <http://localhost:1099/jactor-persistence/v2/api-docsto> (json documentation)
    * <http://localhost:1099/jactor-persistence/swagger-ui.html> (swagger ui)

### Some technologies used ###

* [spring-boot 2.1.x](https://spring.io/projects/spring-boot)
    * with spring-data-jpa
* [h2](http://h2database.com)
* [junit 5.x](https://junit.org/junit5/)
* [assertj](https://joel-costigliola.github.io/assertj/)
* [mockito](http://site.mockito.org)
* [kotlin](https://kotlinlang.org)
* [swagger-ui](https://swagger.io/tools/swagger-ui/)
