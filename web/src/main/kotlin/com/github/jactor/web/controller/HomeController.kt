package com.github.jactor.web.controller

import com.github.jactor.web.Technology
import com.github.jactor.web.i18n.MyMessages
import com.github.jactor.web.model.HomePageModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class HomeController @Autowired constructor(private val myMessages: MyMessages) {
    @GetMapping(value = ["/", HOME_VIEW])
    fun get(): ModelAndView {
        return ModelAndView(HOME_VIEW)
            .addObject(
                "homepage", HomePageModel(
                    listOf(
                        Technology(myMessages.fetchMessage("page.home.tech.gradle"), "Gradle", "https://gradle.org"),
                        Technology(myMessages.fetchMessage("page.home.tech.maven"), "Maven", "https://maven.apache.org"),
                        Technology(myMessages.fetchMessage("page.home.tech.kotlin"), "Kotlin", "https://kotlinlang.org"),
                        Technology(myMessages.fetchMessage("page.home.tech.springboot"), "Spring Boot", "https://spring.io/projects/spring-boot"),
                        Technology(myMessages.fetchMessage("page.home.tech.thymeleaf"), "Thymeleaf", "https://www.thymeleaf.org"),
                        Technology(myMessages.fetchMessage("page.home.tech.h2"), "H2 database", "https://h2database.com"),
                        Technology(myMessages.fetchMessage("page.home.tech.junit"), "Junit", "https://junit.org/junit5/"),
                        Technology(myMessages.fetchMessage("page.home.tech.mockito"), "Mockito", "https://site.mockito.org"),
                        Technology(myMessages.fetchMessage("page.home.tech.assertj"), "AssertJ", "https://joel-costigliola.github.io/assertj/"),
                        Technology(myMessages.fetchMessage("page.home.tech.git"), "Git", "https://git-scm.com")
                    )
                )
            )
    }

    companion object {
        private const val HOME_VIEW = "home"
    }
}
