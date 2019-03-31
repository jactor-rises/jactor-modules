# README #

### What is this repository for? ###

* Src code and issues regarding jactor-web

This project is consisted of a microsevice for web content which use
another mocroservice in order to persist its data through a REST api.

### Set up ###

* a spring-boot application is created when building (`mvn install`)
    * `jactor-web` which is a web application on apache tomcat and which is dependent on `jactor-persistence` to handle persistence 
* after started `jactor-web`, point a browser to http://localhost:8080/jactor-web/

### Disclaimer ###

This application are my playground and is made to demonstrate the
working of `jactor-persistence` and to gain expertise in mocroservices
and kotlin programming language. It is therefore simple and not complete.
And I just want to say that I am not a front-end programmer.

* `Blog`s and `GuestBook`s are only persisted in `jactor-persistence` and though integration tests.
* `User` (with `Person` and `Address`) is integrated with communications from `jactor-web`.
    * The web only do a "read only" access of the persistence, and do not do other CRUD operations. 

### Some technologies used on jactor-web ###

* [spring-boot 2.x](https://spring.io/projects/spring-boot)
* [kotlin](https://kotlinlang.org)
* [junit 5.x](https://junit.org/junit5/)
* [assertj](https://joel-costigliola.github.io/assertj/)
* [mockito](http://site.mockito.org)
* [thymeleaf](https://www.thymeleaf.org)
