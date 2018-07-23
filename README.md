# README #

### What is this repository for? ###

* Src code and issues regarding jactor-rises
* [GitLab Flavoured Markdown](https://gitlab.com/help/user/markdown)

The only hard dependencies:

```mermaid
graph TD;
    jactor-commons-->jactor-io;
    jactor-commons-->jactor-module;
    jactor-commons-->jactor-web;
    jactor-io-->jactor-module;
    jactor-module->jactor-facade;
```

### How do I get set up? ###

* spring-boot applications are created when building (`mvn install`)
    * jactor-persistence-orm which is a standalone rest application which handles the persistence in an object relational model
    * jactor-facade is a rest applications which handles io from any ui wanting to use jactor-model
    * jactor-web which is a web application on apache tomcat
* these applications can run side by side to get a full working application (using `mvn spring-boot:run` on each application)
* persistence-orm is using h2 (in-memory database), and is not finite
* after started jactor-web, point a browser to http://localhost:8080/jactor-web/

### Some technologies used on jactor-rises ###

* [spring framwork 5.x](https://spring.io/projects/spring-framework)
* [spring-boot 2.x](https://spring.io/projects/spring-boot)
* [hibernate 5.x](http://hibernate.org/orm/)
* [h2](http://h2database.com)
* [junit 5.x](https://junit.org/junit5/)
* [assertj](https://joel-costigliola.github.io/assertj/)
* [mockito](http://site.mockito.org)
* [thymeleaf](https://www.thymeleaf.org)
* [git](https://git-scm.com)
