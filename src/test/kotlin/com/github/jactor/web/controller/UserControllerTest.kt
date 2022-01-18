package com.github.jactor.web.controller

import com.github.jactor.shared.dto.UserDto
import com.github.jactor.web.consumer.UserConsumer
import com.github.jactor.web.menu.MenuFacade
import com.github.jactor.web.menu.MenuItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.view.InternalResourceViewResolver
import java.util.Optional

@SpringBootTest
@DisplayName("The UserController")
internal class UserControllerTest {

    companion object {
        private const val REQUEST_USER = "choose"
        private const val USER_ENDPOINT = "/user"
        private const val USER_JACTOR = "jactor"
    }

    private lateinit var mockMvc: MockMvc

    @MockBean
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
        mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT)).andExpect(MockMvcResultMatchers.status().isOk)
        verify(userConsumerMock, never()).find(ArgumentMatchers.anyString())
    }

    @Test
    fun `should not fetch user by username when the username is requested, but is only whitespace`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(USER_ENDPOINT).requestAttr(REQUEST_USER, " \n \t")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        )

        verify(userConsumerMock, never()).find(ArgumentMatchers.anyString())
    }

    @Test
    fun `should fetch user by username when the username is requested`() {
        whenever(userConsumerMock.find(USER_JACTOR)).thenReturn(Optional.of(UserDto()))

        val modelAndView = mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT).param(REQUEST_USER, USER_JACTOR))
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn().modelAndView

        val model = modelAndView?.model ?: HashMap<Any, Any>()

        assertThat(model["user"]).isNotNull
    }

    @Test
    fun `should fetch user by username, but not find user`() {
        whenever(userConsumerMock.find(ArgumentMatchers.anyString())).thenReturn(Optional.empty())

        val modelAndView = mockMvc.perform(
            MockMvcRequestBuilders.get(USER_ENDPOINT).param(REQUEST_USER, "someone")
        ).andExpect(MockMvcResultMatchers.status().isOk).andReturn().modelAndView

        assertThat(modelAndView?.model).isNotNull
        assertThat(modelAndView?.model!!["unknownUser"]).isEqualTo("someone")
    }

    @Test
    fun `should add context path to target of the user names`() {
        whenever(userConsumerMock.findAllUsernames()).thenReturn(listOf("jactor"))

        val modelAndView = mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT))
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn().modelAndView

        val model = modelAndView?.model
        assertThat(model).`as`("model").isNotNull

        val menuItem = model!!["usersMenu"]

        assertThat(menuItem).`as`("users menu among: ${model.keys}").isEqualTo(
            listOf(
                MenuItem(
                    "menu.users.choose", listOf(
                        MenuItem("jactor", "$contextPath/user?choose=jactor", "user.choose.desc")
                    )
                )
            )
        )
    }
}
