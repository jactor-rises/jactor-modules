package com.github.jactor.web

import com.github.jactor.web.menu.Menu
import com.github.jactor.web.menu.MenuFacade
import com.github.jactor.web.menu.MenuItem
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.Scope
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.UriTemplateHandler

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
            .addItem(MenuItem(itemName = "menu.users.default"))
            .addItem(
                MenuItem(
                    itemName = "jactor",
                    target = "$contextPath/user?choose=jactor",
                    description = "menu.users.jactor.desc"
                )
            )
            .addItem(
                MenuItem(
                    itemName = "tip",
                    target = "$contextPath/user?choose=tip",
                    description = "menu.users.tip.desc"
                )
            )
    }

    @Bean
    fun jactorWebUriTemplateHandler(
        @Value("\${jactor-persistence.url.root}") rootUrlPersistence: String?
    ): JactorWebUriTemplateHandler = rootUrlPersistence?.let {
        val uriTemplateHandler = DefaultUriBuilderFactory(it)

        JactorWebUriTemplateHandler(
            uriTemplateHandler = uriTemplateHandler
        )
    } ?: throw IllegalArgumentException("No root url given!")

    @Bean
    @Scope("prototype")
    fun restTemplate(jactorWebUriTemplateHandler: JactorWebUriTemplateHandler): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.uriTemplateHandler = jactorWebUriTemplateHandler.uriTemplateHandler

        return restTemplate
    }

    @JvmRecord
    data class JactorWebUriTemplateHandler(val uriTemplateHandler: UriTemplateHandler)
}
