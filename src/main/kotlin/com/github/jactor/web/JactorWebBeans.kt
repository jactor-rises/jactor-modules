package com.github.jactor.web

import com.github.jactor.web.consumer.DefaultUserConsumer
import com.github.jactor.web.consumer.UserConsumer
import com.github.jactor.web.menu.Menu
import com.github.jactor.web.menu.MenuFacade
import com.github.jactor.web.menu.MenuItem
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RootUriTemplateHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.Scope
import org.springframework.web.client.RestTemplate

@Configuration
@PropertySource("classpath:application.properties")
class JactorWebBeans {

    companion object {
        const val USERS_MENU_NAME = "user"
    }

    @Bean
    fun menuFacade(@Value("\${server.servlet.context-path}") contextPath: String): MenuFacade {
        return MenuFacade(listOf(usersMenu(contextPath)))
    }

    private fun usersMenu(contextPath: String): Menu {
        return Menu(USERS_MENU_NAME)
            .addItem(MenuItem("menu.users.default"))
            .addItem(MenuItem("jactor", "$contextPath/user?choose=jactor", "menu.users.jactor.desc"))
            .addItem(MenuItem("tip", "$contextPath/user?choose=tip", "menu.users.tip.desc"))
    }

    @Bean(name = ["userConsumer"])
    fun userConsumer(restTemplate: RestTemplate, @Value("\${jactor-persistence.url.root}") rootUrlPersistence: String?): UserConsumer {
        restTemplate.uriTemplateHandler = RootUriTemplateHandler(rootUrlPersistence)

        return DefaultUserConsumer(restTemplate)
    }

    @Bean
    @Scope("prototype")
    fun restTemplate(@Value("\${jactor-persistence.url.root}") rootUrlPersistence: String?): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.uriTemplateHandler = RootUriTemplateHandler(rootUrlPersistence)

        return restTemplate
    }
}
