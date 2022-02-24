package com.github.jactor.web.controller

import com.github.jactor.web.JactorWebBeans
import com.github.jactor.web.consumer.UserConsumer
import com.github.jactor.web.dto.UserModel
import com.github.jactor.web.menu.MenuFacade
import com.github.jactor.web.menu.MenuItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class UserController @Autowired constructor(
    private val userConsumer: UserConsumer,
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
        val user = userConsumer.find(username!!)
        val modelMap = modelAndView.model

        if (user.isPresent) {
            modelMap["user"] = UserModel(user.get())
        } else {
            modelMap["unknownUser"] = username
        }
    }

    private fun populateUserMenu(modelAndView: ModelAndView) {
        val menuItems = userConsumer.findAllUsernames()
            .map { chooseUserItem(it) }

        modelAndView.addObject("usersMenu", listOf(MenuItem(itemName = "menu.users.choose", children = menuItems as MutableList<MenuItem>)))
    }

    private fun chooseUserItem(username: String): MenuItem {
        return MenuItem(username, String.format("%s/user?choose=%s", contextPath, username), "user.choose.desc")
    }

    private fun populateDefaultUsers(modelAndView: ModelAndView) {
        val menuItems = menuFacade.fetchMenuItemsByName(JactorWebBeans.USERS_MENU_NAME)
        modelAndView.addObject("defaultUsers", menuItems)
    }
}
