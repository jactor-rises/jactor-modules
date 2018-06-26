# README #

Source code for jactor-rises (client, module, persistence, and front end)

### What is this repository for? ###

* Src code and issues regarding jactor-rises
* Code will be released on [GitHub](https://github.com/jactor-rises)
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* a couple of spring-boot applications are made when you build (build.sh = shell building with maven) this application
    * jactor-persistence-orm which is a standalone rest application which handles the persistence
    * jactor-web which is a standalone web application on apache tomcat
* run these application side by side to get a full working copy (using `mvn spring-boot:run` on each application)
* persistence in jactor-rises is not done with h2

### Some artifacts used in jactor-rises ###

* [spring framwork](https://spring.io/projects/spring-framework)
* [spring-boot](https://spring.io/projects/spring-boot)
* [hibernate](http://hibernate.org/orm/)
* [h2](http://h2database.com)
* [junit](https://junit.org/junit5/)
* [assertj](https://joel-costigliola.github.io/assertj/)
* [mockito](http://site.mockito.org)
* [thymeleaf](https://www.thymeleaf.org)
