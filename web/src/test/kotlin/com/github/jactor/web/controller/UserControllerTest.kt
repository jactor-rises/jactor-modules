package com.github.jactor.web.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.view.InternalResourceViewResolver
import com.github.jactor.shared.dto.UserDto
import com.github.jactor.web.consumer.UserConsumer
import com.github.jactor.web.menu.MenuFacade
import com.github.jactor.web.menu.MenuItem
import com.ninjasquad.springmockk.MockkBean
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.mockk.called
import io.mockk.every
import io.mockk.verify

@SpringBootTest
internal class UserControllerTest {

    companion object {
        private const val REQUEST_USER = "choose"
        private const val USER_ENDPOINT = "/user"
        private const val USER_JACTOR = "jactor"
    }

    private lateinit var mockMvc: MockMvc

    @MockkBean
    @Qualifier("userConsumer")
    private lateinit var userConsumerMock: UserConsumer

    @Autowired
    private lateinit var menuFacade: MenuFacade

    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath: String

    @Value("\${spring.mvc.view.prefix}")
    private lateinit var prefix: String

    @Value("\${spring.mvc.view.suffix}")
    private lateinit var suffix: String

    @BeforeEach
    fun `mock mvc with view resolver`() {
        val internalResourceViewResolver = InternalResourceViewResolver()

        internalResourceViewResolver.setPrefix(prefix)
        internalResourceViewResolver.setSuffix(suffix)

        mockMvc = MockMvcBuilders.standaloneSetup(UserController(userConsumerMock, menuFacade, contextPath))
            .setViewResolvers(internalResourceViewResolver)
            .build()
    }

    @Test
    fun `should not fetch user by username if the username is missing from the request`() {
        every { userConsumerMock.findAllUsernames() } returns emptyList()
        mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT)).andExpect(MockMvcResultMatchers.status().isOk)
        verify { userConsumerMock.find(any())?.wasNot(called) }
    }

    @Test
    fun `should not fetch user by username when the username is requested, but is only whitespace`() {
        every { userConsumerMock.findAllUsernames() } returns emptyList()

        mockMvc.perform(
            MockMvcRequestBuilders.get(USER_ENDPOINT).requestAttr(REQUEST_USER, " \n \t")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        )

        verify { userConsumerMock.find(any())?.wasNot(called) }
    }

    @Test
    fun `should fetch user by username when the username is requested`() {
        every { userConsumerMock.find(USER_JACTOR) } returns UserDto()
        every { userConsumerMock.findAllUsernames() } returns emptyList()

        val modelAndView = mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT).param(REQUEST_USER, USER_JACTOR))
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn().modelAndView

        val model = modelAndView?.model ?: HashMap<Any, Any>()

        assertThat(model["user"]).isNotNull()
    }

    @Test
    fun `should fetch user by username, but not find user`() {
        every { userConsumerMock.find(any()) } returns null
        every { userConsumerMock.findAllUsernames() } returns emptyList()

        val modelAndView = mockMvc.perform(
            MockMvcRequestBuilders.get(USER_ENDPOINT).param(REQUEST_USER, "someone")
        ).andExpect(MockMvcResultMatchers.status().isOk).andReturn().modelAndView

        assertThat(modelAndView?.model).isNotNull()
        assertThat(modelAndView?.model!!["unknownUser"]).isEqualTo("someone")
    }

    @Test
    fun `should add context path to target of the user names`() {
        every { userConsumerMock.findAllUsernames() } returns listOf("jactor")

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
