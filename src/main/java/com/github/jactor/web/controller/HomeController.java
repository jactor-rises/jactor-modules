package com.github.jactor.web.controller;

import com.github.jactor.web.Technology;
import com.github.jactor.web.i18n.MyMessages;
import com.github.jactor.web.model.HomePageModel;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  private static final String HOME_VIEW = "home";

  private final MyMessages myMessages;

  @Autowired
  public HomeController(MyMessages myMessages) {
    this.myMessages = myMessages;
  }

  @GetMapping(value = {"/", HOME_VIEW})
  public ModelAndView get() {
    return new ModelAndView(HOME_VIEW)
        .addObject("homepage", new HomePageModel(
                List.of(
                    new Technology(myMessages.fetchMessage("page.home.tech.gradle"), "Gradle", "http://gradle.org"),
                    new Technology(myMessages.fetchMessage("page.home.tech.maven"), "Maven", "http://maven.apache.org"),
                    new Technology(myMessages.fetchMessage("page.home.tech.kotlin"), "Kotlin", "https://kotlinlang.org"),
                    new Technology(myMessages.fetchMessage("page.home.tech.springboot"), "Spring Boot", "https://spring.io/projects/spring-boot"),
                    new Technology(myMessages.fetchMessage("page.home.tech.thymeleaf"), "Thymeleaf", "https://www.thymeleaf.org"),
                    new Technology(myMessages.fetchMessage("page.home.tech.h2"), "H2 database", "http://h2database.com"),
                    new Technology(myMessages.fetchMessage("page.home.tech.junit"), "Junit", "https://junit.org/junit5/"),
                    new Technology(myMessages.fetchMessage("page.home.tech.mockito"), "Mockito", "http://site.mockito.org"),
                    new Technology(myMessages.fetchMessage("page.home.tech.assertj"), "AssertJ", "https://joel-costigliola.github.io/assertj/"),
                    new Technology(myMessages.fetchMessage("page.home.tech.git"), "Git", "https://git-scm.com")
                )
            )
        );
  }
}
