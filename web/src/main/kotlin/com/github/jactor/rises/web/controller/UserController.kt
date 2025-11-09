package com.github.jactor.rises.web.controller

import com.github.jactor.rises.web.JactorWebBeans
import com.github.jactor.rises.web.client.UserClient
import com.github.jactor.rises.web.menu.MenuFacade
import com.github.jactor.rises.web.menu.MenuItem
import com.github.jactor.rises.web.model.UserModel
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class UserController(
    @param:Qualifier("defaultUserClient") private val userClient: UserClient,
    private val menuFacade: MenuFacade,
    @param:Value("\${server.servlet.context-path}") private val contextPath: String
) {
    @GetMapping(value = ["/user"])
    operator fun get(@RequestParam(name = "choose", required = false) username: String?): ModelAndView {
        val modelAndView = ModelAndView("user")

        if (username != null && username.trim() != "") {
            populateUser(username, modelAndView)
        }

        populateUserMenu(modelAndView)
        populateDefaultUsers(modelAndView)

        return modelAndView
    }

    private fun populateUser(username: String?, modelAndView: ModelAndView) {
        val user = userClient.find(username!!)
        val modelMap = modelAndView.model

        if (user != null) {
            modelMap["user"] = UserModel(user)
        } else {
            modelMap["unknownUser"] = username
        }
    }

    private fun populateUserMenu(modelAndView: ModelAndView) {
        val menuItems = userClient.findAllUsernames()
            .map { chooseUserItem(it) }

        modelAndView.addObject(
            "usersMenu",
            listOf(MenuItem(itemName = "menu.users.choose", children = menuItems as MutableList<MenuItem>))
        )
    }

    private fun chooseUserItem(username: String): MenuItem {
        return MenuItem(username, String.format("%s/user?choose=%s", contextPath, username), "user.choose.desc")
    }

    private fun populateDefaultUsers(modelAndView: ModelAndView) {
        val menuItems = menuFacade.fetchMenuItemsByName(JactorWebBeans.USERS_MENU_NAME)
        modelAndView.addObject("defaultUsers", menuItems)
    }
}
