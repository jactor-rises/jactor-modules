# README #

Source code for jactor-rises (client, model, back-end, and front-end)

### What is this repository for? ###

* Src code and issues regarding jactor-rises
* Code will be released on [GitHub](https://github.com/jactor-rises)
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* a couple of spring-boot applications are created when building (`./build.sh` using maven) these modules
    * jactor-persistence-orm which is a standalone rest application which handles the persistence
    * jactor-web which is a web application on apache tomcat running via spring-boot
* run these application side by side to get a full working application (using `mvn spring-boot:run` on each application)
* persistence in jactor-rises is done using h2 (in-memory database), and is not finite

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
