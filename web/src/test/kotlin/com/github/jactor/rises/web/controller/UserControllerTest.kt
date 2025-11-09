package com.github.jactor.rises.web.controller

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.github.jactor.rises.shared.api.UserDto
import com.github.jactor.rises.web.menu.MenuItem
import com.github.jactor.rises.web.test.AbstractSpringMockMvcTest
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.view.InternalResourceViewResolver

internal class UserControllerTest : AbstractSpringMockMvcTest() {
    override val initMockMvc: (InternalResourceViewResolver) -> MockMvc = {
        MockMvcBuilders.standaloneSetup(UserController(userClientMock, menuFacade, contextPath))
            .setViewResolvers(it).build()
    }

    companion object {
        private const val REQUEST_USER = "choose"
        private const val USER_ENDPOINT = "/user"
        private const val USER_JACTOR = "jactor"
    }

    @Test
    fun `should not fetch user by username if the username is missing from the request`() {
        every { userClientMock.findAllUsernames() } returns emptyList()
        mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT)).andExpect(MockMvcResultMatchers.status().isOk)
        verify(exactly = 0) { userClientMock.find(any()) }
    }

    @Test
    fun `should not fetch user by username when the username is requested, but is only whitespace`() {
        every { userClientMock.findAllUsernames() } returns emptyList()

        mockMvc.perform(
            MockMvcRequestBuilders.get(USER_ENDPOINT).requestAttr(REQUEST_USER, " \n \t")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        )

        verify(exactly = 0) { userClientMock.find(any()) }
    }

    @Test
    fun `should fetch user by username when the username is requested`() {
        every { userClientMock.find(USER_JACTOR) } returns UserDto()
        every { userClientMock.findAllUsernames() } returns emptyList()

        val modelAndView = mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT).param(REQUEST_USER, USER_JACTOR))
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn().modelAndView

        val model = modelAndView?.model ?: HashMap<Any, Any>()

        assertThat(model["user"]).isNotNull()
    }

    @Test
    fun `should fetch user by username, but not find user`() {
        every { userClientMock.find(any()) } returns null
        every { userClientMock.findAllUsernames() } returns emptyList()

        val modelAndView = mockMvc.perform(
            MockMvcRequestBuilders.get(USER_ENDPOINT).param(REQUEST_USER, "someone")
        ).andExpect(MockMvcResultMatchers.status().isOk).andReturn().modelAndView

        assertThat(modelAndView?.model).isNotNull()
        assertThat(modelAndView?.model!!["unknownUser"]).isEqualTo("someone")
    }

    @Test
    fun `should add context path to target of the user names`() {
        every { userClientMock.findAllUsernames() } returns listOf("jactor")

        val modelAndView = mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT))
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn().modelAndView

        val model = modelAndView?.model
        assertThat(model).isNotNull()

        val menuItem = model!!["usersMenu"]

        assertThat(menuItem).isEqualTo(
            listOf(
                MenuItem(
                    itemName = "menu.users.choose", children = mutableListOf(
                        MenuItem(
                            itemName = "jactor",
                            target = "$contextPath/user?choose=jactor",
                            description = "user.choose.desc"
                        )
                    )
                )
            )
        )
    }
}
